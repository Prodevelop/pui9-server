package es.prodevelop.pui9.alerts.eventlistener.listener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfiguration;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertConfigurationService;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertService;
import es.prodevelop.pui9.eventlistener.event.InsertDaoEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Create alerts associated with the inserted registry
 */
@Component
public class InsertAlertsListener extends PuiListener<InsertDaoEvent> {

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private IPuiAlertConfigurationService puiAlertConfigurationService;

	@Autowired
	private IPuiAlertService puiAlertService;

	@Override
	protected boolean passFilter(InsertDaoEvent event) {
		Set<String> models = daoRegistry.getModelIdFromDto(event.getSource().getClass());
		Set<String> modelsWithDocuments = puiAlertConfigurationService.getModelsWithAlerts();
		return !ObjectUtils.isEmpty(models) && CollectionUtils.containsAny(modelsWithDocuments, models);
	}

	@Override
	protected void process(InsertDaoEvent event) throws PuiException {
		new Thread(() -> {
			ITableDto tableDto = event.getSource();
			Set<String> models = daoRegistry.getModelIdFromDto(tableDto.getClass());

			models.forEach(model -> {
				List<IPuiAlertConfiguration> pacs = puiAlertConfigurationService
						.getPuiAlertConfigurationsFromModel(model);
				List<IPuiAlert> puiAlertsToCreate = new ArrayList<>();

				pacs.forEach(pac -> {
					Field alertableField = DtoRegistry.getJavaFieldFromColumnName(tableDto.getClass(),
							pac.getColumnname());
					if (alertableField == null) {
						return;
					}

					IPuiAlert alert = puiAlertConfigurationService.createPuiAlertFromAlertableFieldInTableDto(pac,
							alertableField, tableDto, null);
					if (alert != null) {
						puiAlertsToCreate.add(alert);
					}
				});

				try {
					puiAlertService.bulkInsert(puiAlertsToCreate);
				} catch (PuiServiceInsertException e) {
					// do nothing
				}
			});
		}, "ALERTS_UPDATE_ON_INSERT").start();
	}

}
