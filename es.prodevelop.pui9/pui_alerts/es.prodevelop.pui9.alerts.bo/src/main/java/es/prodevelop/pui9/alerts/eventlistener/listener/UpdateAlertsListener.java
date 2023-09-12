package es.prodevelop.pui9.alerts.eventlistener.listener;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfiguration;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertConfigurationService;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertService;
import es.prodevelop.pui9.eventlistener.event.UpdateDaoEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.utils.PuiConstants;

/**
 * Update alerts associated with the updated registry
 */
@Component
public class UpdateAlertsListener extends PuiListener<UpdateDaoEvent> {

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private IPuiAlertConfigurationService puiAlertConfigurationService;

	@Autowired
	private IPuiAlertService puiAlertService;

	@Override
	protected boolean passFilter(UpdateDaoEvent event) {
		Set<String> models = daoRegistry.getModelIdFromDto(event.getSource().getClass());
		Set<String> modelsWithDocuments = puiAlertConfigurationService.getModelsWithAlerts();
		return !ObjectUtils.isEmpty(models) && CollectionUtils.containsAny(modelsWithDocuments, models);
	}

	@Override
	protected void process(UpdateDaoEvent event) throws PuiException {
		new Thread(() -> {
			ITableDto tableDto = event.getSource();
			ITableDto oldTableDto = event.getOldDto();
			Map<String, Object> fieldValuesMap = event.getFieldValuesMap();

			Set<String> models = daoRegistry.getModelIdFromDto(tableDto.getClass());

			models.forEach(model -> {
				List<IPuiAlertConfiguration> pacs = puiAlertConfigurationService
						.getPuiAlertConfigurationsFromModel(model);

				pacs.forEach(pac -> {
					Field alertableField = DtoRegistry.getJavaFieldFromColumnName(tableDto.getClass(),
							pac.getColumnname());
					if (oldTableDto != null) {
						if (alertableField == null || !alertableField.getType().isAssignableFrom(Instant.class)) {
							return;
						}

						try {
							Instant oldDtoValue = (Instant) alertableField.get(oldTableDto);
							Instant dtoValue = (Instant) alertableField.get(tableDto);

							if (Objects.equals(oldDtoValue, dtoValue)) {
								// alertable field not modified
								return;
							}
						} catch (IllegalAccessException e) {
							logger.error(e.getMessage(), e);
							return;
						}
					}

					String pk = puiAlertConfigurationService.createAlertPk(tableDto);
					FilterBuilder alertFilter = FilterBuilder.newAndFilter()
							.addEquals(IPuiAlert.ALERT_CONFIG_ID_COLUMN, pac.getId())
							.addEqualsExact(IPuiAlert.PK_COLUMN, pk);
					IPuiAlert alert;
					try {
						alert = puiAlertService.getTableDao().findOne(alertFilter);
					} catch (PuiDaoFindException e) {
						alert = null;
					}

					if (alert != null) {
						Instant launchingdatetime = null;
						if (fieldValuesMap == null) {
							launchingdatetime = puiAlertConfigurationService
									.getLaunchingDatetimeFromAlertableFieldAndDto(pac, alertableField, tableDto);
						} else {
							Object dtoValue = fieldValuesMap.containsKey(pac.getColumnname())
									? fieldValuesMap.get(pac.getColumnname())
									: fieldValuesMap.get(pac.getColumnname().replace("_", ""));
							if (dtoValue instanceof Instant) {
								launchingdatetime = puiAlertConfigurationService.getLaunchingDatetimeFromDtoValue(pac,
										(Instant) dtoValue);
							}
						}

						if (launchingdatetime != null) {
							try {
								Map<String, Object> map = new HashMap<>();
								map.put(IPuiAlert.PROCESSED_COLUMN, PuiConstants.FALSE_INT);
								map.put(IPuiAlert.LAUNCHING_DATETIME_COLUMN, launchingdatetime);
								puiAlertService.getTableDao().patch(alert.createPk(), map);
							} catch (PuiDaoSaveException e) {
								// do nothing
							}
						}
					} else {
						if (fieldValuesMap == null) {
							return;
						}

						Object fieldValue = fieldValuesMap.containsKey(pac.getColumnname())
								? fieldValuesMap.get(pac.getColumnname())
								: fieldValuesMap.get(pac.getColumnname().replace("_", ""));
						if (fieldValue == null && alertableField != null) {
							fieldValue = fieldValuesMap.containsKey(alertableField.getName())
									? fieldValuesMap.get(alertableField.getName())
									: fieldValuesMap.get(alertableField.getName().replace("_", ""));
						}

						if (fieldValue == null) {
							return;
						}

						alert = puiAlertConfigurationService.createPuiAlertFromAlertableFieldInTableDto(pac, null,
								tableDto, (Instant) fieldValue);
						if (alert == null) {
							return;
						}

						try {
							puiAlertService.getTableDao().insert(alert);
						} catch (PuiDaoInsertException e) {
							// do nothing
						}
					}
				});
			});
		}, "ALERTS_UPDATE_ON_UPDATE").start();
	}

}
