package es.prodevelop.pui9.alerts.eventlistener.listener;

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
import es.prodevelop.pui9.eventlistener.event.DeleteDaoEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Delete all the alerts associated with the deleted registry
 */
@Component
public class DeleteAlertsListener extends PuiListener<DeleteDaoEvent> {

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private IPuiAlertConfigurationService puiAlertConfigurationService;

	@Autowired
	private IPuiAlertService puiAlertService;

	@Override
	protected boolean passFilter(DeleteDaoEvent event) {
		Set<String> models = daoRegistry.getModelIdFromDto(event.getSource().getClass());
		Set<String> modelsWithDocuments = puiAlertConfigurationService.getModelsWithAlerts();
		return !ObjectUtils.isEmpty(models) && CollectionUtils.containsAny(modelsWithDocuments, models);
	}

	@Override
	protected void process(DeleteDaoEvent event) throws PuiException {
		new Thread(() -> {
			ITableDto tableDto = event.getSource();
			Set<String> models = daoRegistry.getModelIdFromDto(tableDto.getClass());

			models.forEach(model -> {
				List<IPuiAlertConfiguration> pacs = puiAlertConfigurationService
						.getPuiAlertConfigurationsFromModel(model);
				if (pacs == null) {
					return;
				}

				pacs.forEach(pac -> {
					String pk = puiAlertConfigurationService.createAlertPk(tableDto);
					try {
						puiAlertService.getTableDao()
								.deleteWhere(FilterBuilder.newAndFilter()
										.addEquals(IPuiAlert.ALERT_CONFIG_ID_COLUMN, pac.getId())
										.addEqualsExact(IPuiAlert.PK_COLUMN, pk));
					} catch (PuiDaoDeleteException e) {
						// do nothing
					}
				});
			});
		}, "ALERTS_UPDATE_ON_DELETE").start();
	}

}
