package es.prodevelop.pui9.alerts.components;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfiguration;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertConfigurationService;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertService;
import es.prodevelop.pui9.alerts.utils.PuiAlertsUtil;
import es.prodevelop.pui9.common.configuration.AlertableModel;
import es.prodevelop.pui9.common.configuration.AlertableModel.AlertableColumnDefinition;
import es.prodevelop.pui9.common.configuration.ModelConfiguration;
import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.model.dto.PuiUserPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiAudit;
import es.prodevelop.pui9.common.service.interfaces.IPuiAuditService;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.mail.PuiMailConfiguration;
import es.prodevelop.pui9.mail.PuiMailSender;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoMailContentException;
import es.prodevelop.pui9.services.exceptions.PuiServiceSendMailException;
import es.prodevelop.pui9.services.exceptions.PuiServiceWrongMailException;
import es.prodevelop.pui9.threads.PuiMultiInstanceProcessBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiConstants;

/**
 * This component has a Timer to periodically check all alerts pending to
 * process. If there exists alerts not processed and the launching datetime has
 * exceeded now, an email will be send and the alert will be marked as
 * processed.
 */
@Component
public class PuiAlertsExecutor {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private IPuiVariableService puiVariableService;

	@Autowired
	private IPuiModelService puiModelService;

	@Autowired
	private IPuiAlertConfigurationService puiAlertConfigurationService;

	@Autowired
	private IPuiAlertService puiAlertService;

	@Autowired
	private IPuiAuditService puiAuditService;

	@Autowired
	private IPuiUserService puiUserService;

	@Autowired
	private PuiMultiInstanceProcessBackgroundExecutors multiInstanceProcessBackExec;

	@PostConstruct
	private void postConstruct() {
		Integer delay = puiVariableService.getVariable(Integer.class, PuiVariableValues.ALERTS_EXECUTOR_DELAY.name());
		delay = delay != null ? delay : 10;

		multiInstanceProcessBackExec.registerNewExecutor("CheckAndNotifyAlerts", 0, delay, TimeUnit.MINUTES,
				this::checkAndNotifyAlerts);
	}

	/**
	 * Check and Notify alerts via email
	 */
	private synchronized void checkAndNotifyAlerts() {
		List<IPuiAlertConfiguration> list;
		try {
			list = puiAlertConfigurationService.getAll();
		} catch (PuiServiceGetException e) {
			list = Collections.emptyList();
		}

		list.forEach(pac -> {
			if (!ObjectUtils.isEmpty(pac.getContent())) {
				new Thread(new CheckAndNotifyAlertsRunnable(pac), "PuiThread_CheckAndNotifyAlerts_" + pac.getId())
						.start();
			} else {
				logger.info("Alert configuration '" + pac.getId() + "' doesn't define content");
			}
		});
	}

	private class CheckAndNotifyAlertsRunnable implements Runnable {

		private IPuiAlertConfiguration puiAlertConfiguration;

		public CheckAndNotifyAlertsRunnable(IPuiAlertConfiguration puiAlertConfiguration) {
			this.puiAlertConfiguration = puiAlertConfiguration;
		}

		public void run() {
			FilterBuilder filter = FilterBuilder.newAndFilter()
					.addEquals(IPuiAlert.ALERT_CONFIG_ID_COLUMN, puiAlertConfiguration.getId())
					.addEquals(IPuiAlert.PROCESSED_COLUMN, PuiConstants.FALSE_INT)
					.addLowerEqualsThan(IPuiAlert.LAUNCHING_DATETIME_COLUMN, Instant.now());

			SearchRequest req = new SearchRequest();
			req.setFilter(filter.asFilterGroup());
			req.setRows(500);

			puiAlertService.getTableDao().executePaginagedOperation(req, alert -> {
				Pair<ITableDto, IViewDto> pair = puiAlertConfigurationService
						.getDtosFromPuiAlertConfiguration(puiAlertConfiguration, alert);

				Set<String> destinationEmails = getDestinationEmails(alert);
				String subject = getEmailSubject(alert, pair != null ? pair.getKey() : null);
				String content = getEmailContent(pair);
				sendMail(destinationEmails, subject, content);

				try {
					puiAlertService.getTableDao().patch(alert.createPk(),
							Collections.singletonMap(IPuiAlert.PROCESSED_COLUMN, PuiConstants.TRUE_INT));
				} catch (PuiDaoSaveException e) {
					// do nothing
				}
			}, null);
		}

		private Set<String> getDestinationEmails(IPuiAlert alert) {
			Set<String> emails = new LinkedHashSet<>();

			String email = getEmailAddressFromModelConfiguration(alert);
			if (email != null) {
				emails.add(email);
			} else if (puiAlertConfiguration.getEmails() != null) {
				if (puiAlertConfiguration.getEmails().contains(";")) {
					emails.addAll(Arrays.asList(puiAlertConfiguration.getEmails().split(";")));
				} else {
					emails.add(puiAlertConfiguration.getEmails());
				}
			}
			return emails;
		}

		private String getEmailAddressFromModelConfiguration(IPuiAlert alert) {
			if (ObjectUtils.isEmpty(puiAlertConfiguration.getModel())) {
				return null;
			}

			ModelConfiguration modelConfiguration = puiModelService
					.getModelConfiguration(puiAlertConfiguration.getModel()).getDefaultConfiguration();
			AlertableModel alertableModel = puiAlertConfigurationService.getAlertableModel(puiAlertConfiguration,
					modelConfiguration);
			if (alertableModel == null) {
				return null;
			}

			AlertableColumnDefinition columnDefinition = puiAlertConfigurationService
					.getAlertableColumnDefinition(puiAlertConfiguration, alertableModel);
			if (columnDefinition == null) {
				return null;
			}

			if (columnDefinition.isNotifyUserModel() == null || !columnDefinition.isNotifyUserModel().booleanValue()) {
				return null;
			}

			try {
				String[] puiAuditTypes = { "insert", "update", "patch" };
				FilterBuilder filter = FilterBuilder.newAndFilter()
						.addEqualsExact(IVPuiAudit.MODEL_COLUMN, puiAlertConfiguration.getModel())
						.addEqualsExact(IVPuiAudit.PK_COLUMN, alert.getPk())
						.addIn(IVPuiAudit.TYPE_COLUMN, Arrays.asList(puiAuditTypes));

				SearchRequest auditReq = new SearchRequest();
				auditReq.setRows(1);
				auditReq.setFilter(filter.asFilterGroup());
				auditReq.setOrder(Collections.singletonList(Order.newOrderDesc(IVPuiAudit.DATETIME_COLUMN)));

				SearchResponse<IVPuiAudit> auditRes = puiAuditService.searchView(auditReq);
				if (auditRes.getData() == null || auditRes.getData().isEmpty()) {
					logger.debug("No data found in PuiAudit for model '" + puiAlertConfiguration.getModel()
							+ "' and alert pk '" + alert.getPk() + "'");
					return null;
				}

				String usr = auditRes.getData().get(0).getUsr();
				IPuiUser puiUser = puiUserService.getByPk(new PuiUserPk(usr));
				if (puiUser == null || puiUser.getEmail() == null) {
					logger.debug("'" + usr + "' hasn't email in PuiUser");
					return null;
				}
				return puiUser.getEmail();
			} catch (PuiServiceGetException e) {
				logger.error(e.getMessage(), e);
			}

			return null;
		}

		private String getEmailSubject(IPuiAlert puiAlert, ITableDto tableDto) {
			if (ObjectUtils.isEmpty(puiAlertConfiguration.getModel())) {
				return puiVariableService.getVariable(PuiVariableValues.APPLICATION_NAME.name());
			}

			String subject = getEmailSubjectFromModelConfiguration(puiAlert, tableDto);

			if (ObjectUtils.isEmpty(subject)) {
				String appName = puiVariableService.getVariable(PuiVariableValues.APPLICATION_NAME.name());
				subject = appName + ": " + puiAlertConfiguration.getColumnname() + " - "
						+ puiAlertConfiguration.getModel() + " - " + puiAlert.getPk();
			}

			return subject;
		}

		private String getEmailSubjectFromModelConfiguration(IPuiAlert puiAlert, ITableDto tableDto) {
			ModelConfiguration modelConfiguration = puiModelService
					.getModelConfiguration(puiAlertConfiguration.getModel()).getDefaultConfiguration();
			AlertableModel alertableModel = puiAlertConfigurationService.getAlertableModel(puiAlertConfiguration,
					modelConfiguration);
			if (alertableModel == null) {
				return null;
			}

			AlertableColumnDefinition columnDefinition = puiAlertConfigurationService
					.getAlertableColumnDefinition(puiAlertConfiguration, alertableModel);
			if (columnDefinition == null) {
				return null;
			}

			if (ObjectUtils.isEmpty(alertableModel.getIdColumns())) {
				logger.debug("idColumns attribute for model '" + puiAlertConfiguration.getModel() + "' doesn't exist");
				return null;
			}

			if (columnDefinition.getNotificationSubject() == null) {
				String appName = puiVariableService.getVariable(PuiVariableValues.APPLICATION_NAME.name());
				return appName + ": " + columnDefinition.getColumnLabel() + " - " + modelConfiguration.getLabel()
						+ " - " + puiAlert.getPk();
			}

			StringBuilder sb = new StringBuilder();
			for (Iterator<String> it = alertableModel.getIdColumns().iterator(); it.hasNext();) {
				String nextIdColumn = it.next();
				Field fieldFromColumn = DtoRegistry.getJavaFieldFromColumnName(tableDto.getClass(), nextIdColumn);
				if (fieldFromColumn == null) {
					logger.debug("Column '" + nextIdColumn + "' doesn't exist in table DTO");
					continue;
				}

				Object value;
				try {
					value = fieldFromColumn.get(tableDto);
					sb.append(value);
				} catch (Exception e) {
					continue;
				}

				if (it.hasNext()) {
					sb.append(PuiAlertsUtil.PK_SEPARATOR);
				}
			}

			String idColumns = sb.toString();
			if (ObjectUtils.isEmpty(idColumns)) {
				logger.debug("IdColumns not found in table DTO");
				return null;
			}

			String subject = columnDefinition.getNotificationSubject();
			subject = subject.replace("{model}", modelConfiguration.getLabel());
			subject = subject.replace("{column}", columnDefinition.getColumnLabel());
			subject = subject.replace("{idColumns}", idColumns);

			return subject;
		}

		private String getEmailContent(Pair<ITableDto, IViewDto> pair) {
			String emailContent = puiAlertConfiguration.getContent();
			if (ObjectUtils.isEmpty(emailContent)) {
				logger.debug("Email content is empty");
				return null;
			}

			if (puiAlertConfiguration.getIscontenthtml().equals(PuiConstants.TRUE_INT)) {
				Map<String, Object> params = obtainParamsFromTemplate(emailContent, pair);
				emailContent = PuiMailConfiguration.compileTemplate(emailContent, params);
			}

			if (ObjectUtils.isEmpty(puiAlertConfiguration.getModel())) {
				return emailContent;
			}

			ModelConfiguration modelConfiguration = puiModelService
					.getModelConfiguration(puiAlertConfiguration.getModel()).getDefaultConfiguration();
			AlertableModel alertableModel = puiAlertConfigurationService.getAlertableModel(puiAlertConfiguration,
					modelConfiguration);
			if (alertableModel == null) {
				return emailContent;
			}
			AlertableColumnDefinition columnDefinition = puiAlertConfigurationService
					.getAlertableColumnDefinition(puiAlertConfiguration, alertableModel);
			if (columnDefinition == null) {
				return emailContent;
			}

			if (columnDefinition.isNotificationContentLink() != null
					&& columnDefinition.isNotificationContentLink().booleanValue()) {
				try {
					// Object casting is needed to prevent ClassCastException
					IDto dtoPk = pair.getKey().createPk();
					String dtoJson = GsonSingleton.getSingleton().getGson().toJson(dtoPk);
					dtoJson = dtoJson.replace(": ", ":").replace(" ", "").replace("\n", "");
					String dtoBase64 = Base64.getEncoder().encodeToString(dtoJson.getBytes());

					StringBuilder url = new StringBuilder();
					url.append(puiVariableService.getVariable(PuiVariableValues.BASE_CLIENT_URL.name()));
					url.append("/").append(puiAlertConfiguration.getModel());
					url.append("/update");
					url.append("/" + dtoBase64);

					emailContent += "\n\n" + url.toString();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

			return emailContent;
		}

		private void sendMail(Set<String> destinationEmails, String subject, String content) {
			if (ObjectUtils.isEmpty(subject) || ObjectUtils.isEmpty(content)) {
				logger.debug("Email subject or content is empty");
				return;
			}

			try {
				PuiMailConfiguration config = PuiMailConfiguration.builder().withTo(destinationEmails)
						.withSubject(subject).withContent(content)
						.withIsHtml(puiAlertConfiguration.getIscontenthtml().equals(PuiConstants.TRUE_INT));
				PuiMailSender.getSingleton().send(config);
			} catch (PuiServiceWrongMailException | PuiServiceSendMailException | PuiServiceNoMailContentException e) {
				logger.error(e.getMessage(), e);
			}
		}

		private Map<String, Object> obtainParamsFromTemplate(String content, Pair<ITableDto, IViewDto> pair) {
			Map<String, Object> params = new LinkedHashMap<>();
			if (pair == null) {
				return params;
			}

			IDto dto = pair.getValue() != null ? pair.getValue() : pair.getKey();

			// look for all the variables
			Pattern pattern = Pattern.compile("\\$([a-zA-Z0-9_]*)");
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				String param = matcher.group(1);
				if (ObjectUtils.isEmpty(param)) {
					continue;
				}

				Field field = DtoRegistry.getJavaFieldFromAllFields(dto.getClass(), param);
				if (field == null) {
					continue;
				}

				Object value;
				try {
					value = FieldUtils.readField(field, dto, true);
				} catch (IllegalAccessException e) {
					value = null;
				}
				params.put(param, value);
			}

			return params;
		}

	}
}
