package es.prodevelop.pui9.alerts.service;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.alerts.enums.AlertConfigTimeBeforeAfterEnum;
import es.prodevelop.pui9.alerts.enums.AlertConfigTimeUnitEnum;
import es.prodevelop.pui9.alerts.enums.AlertConfigTypeEnum;
import es.prodevelop.pui9.alerts.exceptions.PuiAlertsNoConfigurationException;
import es.prodevelop.pui9.alerts.model.dao.interfaces.IPuiAlertConfigurationDao;
import es.prodevelop.pui9.alerts.model.dto.PuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfiguration;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfigurationPk;
import es.prodevelop.pui9.alerts.model.views.dao.interfaces.IVPuiAlertConfigurationDao;
import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlertConfiguration;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertConfigurationService;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertService;
import es.prodevelop.pui9.alerts.utils.PuiAlertsUtil;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.configuration.AlertableModel;
import es.prodevelop.pui9.common.configuration.AlertableModel.AlertableColumnDefinition;
import es.prodevelop.pui9.common.configuration.ModelConfiguration;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiConstants;

@PuiGenerated
@Service
public class PuiAlertConfigurationService extends
		AbstractService<IPuiAlertConfigurationPk, IPuiAlertConfiguration, IVPuiAlertConfiguration, IPuiAlertConfigurationDao, IVPuiAlertConfigurationDao>
		implements IPuiAlertConfigurationService {

	@Autowired
	private IPuiModelService puiModelService;

	@Autowired
	private IPuiAlertService puiAlertService;

	private Map<String, List<IPuiAlertConfiguration>> modelAlertConfigsMap = new HashMap<>();

	@PostConstruct
	private void postConstructPuiAlertConfigurationService() {
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ReloadModelsWithAlertConfigurations", true, 0, 30,
				TimeUnit.MINUTES, this::reloadModelsWithAlertConfigurations);
	}

	@Override
	protected void afterGet(IPuiAlertConfiguration dto) throws PuiServiceException {
		if (AlertConfigTypeEnum.GENERIC.name().equals(dto.getType())) {
			IPuiAlert alert;
			try {
				alert = puiAlertService.getTableDao()
						.findOne(FilterBuilder.newAndFilter().addEquals(IPuiAlert.ALERT_CONFIG_ID_COLUMN, dto.getId()));
			} catch (PuiDaoFindException e) {
				logger.error(e.getMessage(), e);
				alert = null;
			}

			if (alert != null) {
				dto.setLaunchingdatetime(alert.getLaunchingdatetime());
			}
		}
	}

	@Override
	protected void beforeInsert(IPuiAlertConfiguration dto) throws PuiServiceException {
		dto.setUsr(PuiUserSession.getCurrentSession().getUsr());
		dto.setDatetime(Instant.now());

		AlertConfigTypeEnum configType = AlertConfigTypeEnum.getValue(dto.getType());
		switch (configType) {
		case GENERIC:
			dto.setTimeunit(null);
			dto.setTimebeforeafter(null);
			break;
		case MODEL:
			if (notifyUserModel(dto)) {
				dto.setEmails(null);
			}
			break;
		}
	}

	@Override
	protected void afterInsert(IPuiAlertConfiguration dto) throws PuiServiceException {
		reloadModelsWithAlertConfigurations();

		AlertConfigTypeEnum configType = AlertConfigTypeEnum.getValue(dto.getType());
		switch (configType) {
		case GENERIC:
			// Create generic alert
			IPuiAlert puiAlert = new PuiAlert();
			puiAlert.setAlertconfigid(dto.getId());
			puiAlert.setLaunchingdatetime(dto.getLaunchingdatetime());
			puiAlertService.insert(puiAlert);
			break;
		case MODEL:
			// Create model alerts
			createAlertsFromModel(dto);
			break;
		}
	}

	@Override
	protected void beforeUpdate(IPuiAlertConfiguration oldDto, IPuiAlertConfiguration dto) throws PuiServiceException {
		if (AlertConfigTypeEnum.MODEL.name().equals(dto.getType()) && notifyUserModel(dto)) {
			dto.setEmails(null);
		}
	}

	@Override
	protected void afterUpdate(IPuiAlertConfiguration oldDto, IPuiAlertConfiguration dto) throws PuiServiceException {
		reloadModelsWithAlertConfigurations();

		AlertConfigTypeEnum configType = AlertConfigTypeEnum.getValue(dto.getType());
		switch (configType) {
		case GENERIC:
			updateGenericAlerts(oldDto, dto);
			break;
		case MODEL:
			updateModelAlerts(oldDto, dto);
			break;
		}
	}

	@Override
	protected void afterDelete(IPuiAlertConfiguration dto) throws PuiServiceException {
		reloadModelsWithAlertConfigurations();
	}

	@Override
	public Set<String> getModelsWithAlerts() {
		return modelAlertConfigsMap.keySet();
	}

	@Override
	public List<IPuiAlertConfiguration> getPuiAlertConfigurationsFromModel(String model) {
		return modelAlertConfigsMap.get(model);
	}

	@Override
	public IPuiAlert createPuiAlertFromAlertableFieldInTableDto(IPuiAlertConfiguration pac, Field alertableField,
			ITableDto tableDto, Instant dtoInstantValue) {
		Instant launchingdatetime = alertableField != null
				? getLaunchingDatetimeFromAlertableFieldAndDto(pac, alertableField, tableDto)
				: getLaunchingDatetimeFromDtoValue(pac, dtoInstantValue);
		if (launchingdatetime == null) {
			return null;
		}

		IPuiAlert alert = new PuiAlert();
		alert.setAlertconfigid(pac.getId());
		alert.setPk(createAlertPk(tableDto));
		alert.setLaunchingdatetime(launchingdatetime);

		return alert;
	}

	@Override
	public Instant getLaunchingDatetimeFromAlertableFieldAndDto(IPuiAlertConfiguration pac, Field alertableField,
			ITableDto tableDto) {
		if (alertableField == null) {
			return null;
		}

		if (!alertableField.getType().isAssignableFrom(Instant.class)) {
			logger.debug("Alertable field '" + alertableField.getName() + "' is null or isn't Instant type");
			return null;
		}

		Instant dtoValue;
		try {
			dtoValue = (Instant) alertableField.get(tableDto);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			return null;
		}

		return getLaunchingDatetimeFromDtoValue(pac, dtoValue);
	}

	@Override
	public Instant getLaunchingDatetimeFromDtoValue(IPuiAlertConfiguration pac, Instant dtoValue) {
		if (ObjectUtils.isEmpty(pac.getTimeunit()) || ObjectUtils.isEmpty(pac.getTimebeforeafter())
				|| ObjectUtils.isEmpty(pac.getTimevalue()) || dtoValue == null) {
			return dtoValue;
		}

		AlertConfigTimeBeforeAfterEnum beforeAfter = AlertConfigTimeBeforeAfterEnum.getValue(pac.getTimebeforeafter());
		ChronoUnit temporalUnit = AlertConfigTimeUnitEnum.toChronoUnit(pac.getTimeunit());
		Instant newInstant = dtoValue;

		switch (beforeAfter) {
		case BEFORE:
			newInstant = dtoValue.minus(pac.getTimevalue(), temporalUnit);
			break;
		case AFTER:
			newInstant = dtoValue.plus(pac.getTimevalue(), temporalUnit);
			break;
		}

		return newInstant.isAfter(Instant.now()) ? newInstant : null;
	}

	@Override
	public String createAlertPk(IDto tableDto) {
		StringBuilder pk = new StringBuilder();
		for (Iterator<String> it = DtoRegistry.getPkFields(tableDto.getClass()).iterator(); it.hasNext();) {
			String nextPkFieldName = it.next();
			Field nextPkField = DtoRegistry.getJavaFieldFromFieldName(tableDto.getClass(), nextPkFieldName);

			try {
				Object value = nextPkField.get(tableDto);
				pk.append(value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do nothing
			}

			if (it.hasNext()) {
				pk.append(PuiAlertsUtil.PK_SEPARATOR);
			}
		}
		return pk.toString();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Pair<ITableDto, IViewDto> getDtosFromPuiAlertConfiguration(IPuiAlertConfiguration puiAlertConfiguation,
			IPuiAlert puiAlert) {
		if (ObjectUtils.isEmpty(puiAlertConfiguation.getModel())) {
			return null;
		}
		Class<? extends ITableDao> tableDaoClass = getDaoRegistry()
				.getTableDaoFromModelId(puiAlertConfiguation.getModel());
		Class<? extends ITableDto> tableDtoClass = getDaoRegistry().getDtoFromDao(tableDaoClass, false);
		Class<? extends IViewDao> viewDaoClass = getDaoRegistry()
				.getViewDaoFromModelId(puiAlertConfiguation.getModel());
		if (tableDaoClass == null || tableDtoClass == null) {
			return null;
		}

		ITableDao<ITableDto, ITableDto> tableDao = PuiApplicationContext.getInstance().getBean(tableDaoClass);
		IViewDao<IViewDto> viewDao = null;
		if (viewDaoClass != null) {
			viewDao = PuiApplicationContext.getInstance().getBean(viewDaoClass);
		}

		List<String> pkFieldNames = DtoRegistry.getPkFields(tableDtoClass);
		FilterBuilder filter = FilterBuilder.newOrFilter();

		if (!puiAlert.getPk().contains(PuiAlertsUtil.PK_SEPARATOR)) {
			filter.addEqualsExact(pkFieldNames.get(0), puiAlert.getPk());
		} else {
			String[] pks = puiAlert.getPk().split(PuiAlertsUtil.PK_SEPARATOR);
			for (int i = 0; i < pks.length; i++) {
				filter.addEqualsExact(pkFieldNames.get(i), pks[i]);
			}
		}

		try {
			ITableDto tableDto = tableDao.findOne(filter);
			IViewDto viewDto = null;
			if (viewDao != null) {
				viewDto = viewDao.findOne(filter);
			}

			return Pair.of(tableDto, viewDto);
		} catch (PuiDaoFindException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public AlertableModel getAlertableModel(IPuiAlertConfiguration puiAlertConfiguration,
			ModelConfiguration modelConfiguration) {
		AlertableModel alertableModel = modelConfiguration.getAlertable();
		if (alertableModel == null) {
			logger.error("Alertable attribute for model '" + puiAlertConfiguration.getModel() + "' doesn't exist");
			return null;
		}

		if (ObjectUtils.isEmpty(alertableModel.getAlertableColumns())) {
			logger.error(
					"AlertableColumns attribute for model '" + puiAlertConfiguration.getModel() + "' doesn't exist");
			return null;
		}

		return alertableModel;
	}

	@Override
	public AlertableColumnDefinition getAlertableColumnDefinition(IPuiAlertConfiguration puiAlertConfiguration,
			AlertableModel alertableModel) {
		Optional<AlertableColumnDefinition> columnDefinition = alertableModel.getAlertableColumns().stream()
				.filter(acd -> puiAlertConfiguration.getColumnname().equals(acd.getColumnName())).findFirst();
		if (!columnDefinition.isPresent()) {
			logger.debug("AlertableColumn for column '" + puiAlertConfiguration.getColumnname() + "' doesn't exist");
			return null;
		}
		if (ObjectUtils.isEmpty(columnDefinition.get().getColumnLabel())) {
			logger.debug("ColumnLabel for column '" + puiAlertConfiguration.getColumnname() + "' doesn't exist");
			return null;
		}
		return columnDefinition.get();
	}

	private void reloadModelsWithAlertConfigurations() {
		modelAlertConfigsMap.clear();

		try {
			getAllWhere(FilterBuilder.newAndFilter().addIsNotNull(IPuiAlertConfiguration.MODEL_COLUMN)).forEach(pac -> {
				if (!modelAlertConfigsMap.containsKey(pac.getModel())) {
					modelAlertConfigsMap.put(pac.getModel(), new ArrayList<>());
				}
				modelAlertConfigsMap.get(pac.getModel()).add(pac);
			});
		} catch (PuiServiceGetException e) {
			// do nothing
		}
	}

	private boolean notifyUserModel(IPuiAlertConfiguration dto) throws PuiServiceException {
		ModelConfiguration modelConfiguration = puiModelService.getModelConfiguration(dto.getModel())
				.getDefaultConfiguration();
		if (modelConfiguration == null) {
			throw new PuiAlertsNoConfigurationException();
		}
		AlertableModel alertableModel = getAlertableModel(dto, modelConfiguration);
		if (alertableModel == null) {
			throw new PuiAlertsNoConfigurationException();
		}
		AlertableColumnDefinition alertableColumnDefinition = getAlertableColumnDefinition(dto, alertableModel);
		if (alertableColumnDefinition == null) {
			throw new PuiAlertsNoConfigurationException();
		}

		return alertableColumnDefinition.isNotifyUserModel();
	}

	/**
	 * Search all registries from the model and create alerts if the column is an
	 * Instant
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createAlertsFromModel(IPuiAlertConfiguration dto) {
		Class<? extends ITableDao> tableDaoClass = getDaoRegistry().getTableDaoFromModelId(dto.getModel());
		Class<? extends ITableDto> dtoClass = getDaoRegistry().getTableDtoFromModelId(dto.getModel(), false, false);
		if (tableDaoClass == null || dtoClass == null) {
			logger.error("The model '" + dto.getModel() + "' is not registered in the application");
			return;
		}

		Field alertableField = DtoRegistry.getJavaFieldFromColumnName(dtoClass, dto.getColumnname());
		if (alertableField == null) {
			logger.error(
					"The column '" + dto.getColumnname() + "' doesn't belong to the model '" + dto.getModel() + "'");
			return;
		}

		ModelConfiguration modelConfiguration = puiModelService.getModelConfiguration(dto.getModel())
				.getDefaultConfiguration();
		AlertableModel alertableModel = getAlertableModel(dto, modelConfiguration);
		if (alertableModel == null) {
			return;
		}
		AlertableColumnDefinition alertableColumnDefinition = getAlertableColumnDefinition(dto, alertableModel);
		if (alertableColumnDefinition == null) {
			return;
		}

		FilterBuilder filter = FilterBuilder.newAndFilter().addIsNotNull(dto.getColumnname());
		if (alertableColumnDefinition.getFilter() != null) {
			filter.addGroup(FilterBuilder.newFilter(alertableColumnDefinition.getFilter()));
		}

		SearchRequest req = new SearchRequest();
		req.setFilter(filter.asFilterGroup());
		req.setRows(500);

		ITableDao<ITableDto, ITableDto> tableDao = PuiApplicationContext.getInstance().getBean(tableDaoClass);

		tableDao.executePaginagedOperation(req, null, items -> {
			List<IPuiAlert> puiAlertsToCreate = new ArrayList<>();

			for (ITableDto tableDto : items) {
				IPuiAlert puiAlert = createPuiAlertFromAlertableFieldInTableDto(dto, alertableField, tableDto, null);
				if (puiAlert != null) {
					puiAlertsToCreate.add(puiAlert);
				}
			}

			try {
				puiAlertService.getTableDao().bulkInsert(puiAlertsToCreate);
			} catch (PuiDaoInsertException e) {
				logger.error(e.getMessage(), e);
			}
		});
	}

	private void updateGenericAlerts(IPuiAlertConfiguration oldDto, IPuiAlertConfiguration dto) {
		if (oldDto.getLaunchingdatetime().equals(dto.getLaunchingdatetime())) {
			return;
		}

		FilterBuilder filter = FilterBuilder.newAndFilter().addEquals(IPuiAlert.ALERT_CONFIG_ID_COLUMN, dto.getId())
				.addEquals(IPuiAlert.PROCESSED_COLUMN, PuiConstants.FALSE_INT);

		IPuiAlert alert;
		try {
			alert = puiAlertService.getTableDao().findOne(filter);
		} catch (PuiDaoFindException e) {
			alert = null;
		}
		if (alert == null) {
			return;
		}

		try {
			puiAlertService.getTableDao().patch(alert.createPk(),
					Collections.singletonMap(IPuiAlert.LAUNCHING_DATETIME_COLUMN, dto.getLaunchingdatetime()));
		} catch (PuiDaoSaveException e) {
			// do nothing
		}
	}

	private void updateModelAlerts(IPuiAlertConfiguration oldDto, IPuiAlertConfiguration dto) {
		if (oldDto.getTimevalue().equals(dto.getTimevalue()) && oldDto.getTimeunit().equals(dto.getTimeunit())
				&& oldDto.getTimebeforeafter().equals(dto.getTimebeforeafter())) {
			return;
		}

		Class<? extends ITableDto> dtoClass = getDaoRegistry().getTableDtoFromModelId(dto.getModel(), false, false);
		if (dtoClass == null) {
			return;
		}

		Field alertableField = DtoRegistry.getJavaFieldFromColumnName(dtoClass, dto.getColumnname());
		if (alertableField == null) {
			return;
		}

		FilterBuilder filter = FilterBuilder.newAndFilter().addEquals(IPuiAlert.ALERT_CONFIG_ID_COLUMN, dto.getId())
				.addIsNotNull(IPuiAlert.PK_COLUMN);

		SearchRequest req = new SearchRequest();
		req.setFilter(filter.asFilterGroup());
		req.setRows(500);

		puiAlertService.getTableDao().executePaginagedOperation(req, alert -> {
			Pair<ITableDto, IViewDto> pair = getDtosFromPuiAlertConfiguration(dto, alert);
			if (pair == null) {
				return;
			}

			ITableDto tableDto = pair.getKey();
			if (tableDto == null) {
				return;
			}

			Instant launchingdatetime = getLaunchingDatetimeFromAlertableFieldAndDto(dto, alertableField, tableDto);
			if (launchingdatetime != null) {
				try {
					puiAlertService.getTableDao().patch(alert.createPk(),
							Collections.singletonMap(IPuiAlert.LAUNCHING_DATETIME_COLUMN, launchingdatetime));
				} catch (PuiDaoSaveException e) {
					// do nothing
				}
			} else {
				try {
					puiAlertService.getTableDao().delete(alert.createPk());
				} catch (PuiDaoDeleteException e) {
					// do nothing
				}
			}
		}, null);
	}

}