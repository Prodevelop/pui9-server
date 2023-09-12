package es.prodevelop.pui9.alerts.service.interfaces;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import es.prodevelop.pui9.alerts.model.dao.interfaces.IPuiAlertConfigurationDao;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfiguration;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfigurationPk;
import es.prodevelop.pui9.alerts.model.views.dao.interfaces.IVPuiAlertConfigurationDao;
import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlertConfiguration;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.configuration.AlertableModel;
import es.prodevelop.pui9.common.configuration.ModelConfiguration;
import es.prodevelop.pui9.common.configuration.AlertableModel.AlertableColumnDefinition;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.service.interfaces.IService;

@PuiGenerated
public interface IPuiAlertConfigurationService extends
		IService<IPuiAlertConfigurationPk, IPuiAlertConfiguration, IVPuiAlertConfiguration, IPuiAlertConfigurationDao, IVPuiAlertConfigurationDao> {

	/**
	 * Return the models that are susceptible to create alerts
	 * 
	 * @return The list of models that are allowed to create alerts
	 */
	Set<String> getModelsWithAlerts();

	/**
	 * Return the list of alert configurations from the model
	 * 
	 * @return The list of alert configurations
	 */
	List<IPuiAlertConfiguration> getPuiAlertConfigurationsFromModel(String model);

	/**
	 * Create a PuiAlerts from the alertable field in the tableDto. The alertable
	 * field has to be an Instant value after now. In other case, the alert will not
	 * be created because has no sense
	 * 
	 * @param pac             The alert configuration
	 * @param alertableField  The alertable field in the DTO
	 * @param tableDto        The table DTO where the alert will be created
	 * @param dtoInstantValue The DTO instant value
	 * 
	 * @return The new alert
	 */
	IPuiAlert createPuiAlertFromAlertableFieldInTableDto(IPuiAlertConfiguration pac, Field alertableField,
			ITableDto tableDto, Instant dtoInstantValue);

	/**
	 * Get the real datetime when the alert will be fired, taking into account the
	 * alert configuration. The alertable field has to be an Instant value after
	 * now. In other case, no time is returned because has no sense
	 * 
	 * @param pac            The alert configuration
	 * @param alertableField The alertable field in the DTO
	 * @param tableDto       The table DTO where the alert will be fired
	 * 
	 * @return The real datetime when the alert will be fired. Or null if the
	 *         configuration indicates that the alert has not to be fired
	 */
	Instant getLaunchingDatetimeFromAlertableFieldAndDto(IPuiAlertConfiguration pac, Field alertableField,
			ITableDto tableDto);

	/**
	 * Get the real datetime when the alert will be fired, taking into account the
	 * alert configuration. The dto value has to be an Instant value after now. In
	 * other case, no time is returned because has no sense
	 * 
	 * @param pac      The alert configuration
	 * @param dtoValue The DTO Instant value
	 * @return The real datetime when the alert will be fired. Or null if the
	 *         configuration indicates that the alert has not to be fired
	 */
	Instant getLaunchingDatetimeFromDtoValue(IPuiAlertConfiguration pac, Instant dtoValue);

	/**
	 * Get the DTO associated to the given Alert configuration and Alert
	 * 
	 * @param puiAlertConfiguation The Alert configuration
	 * @param puiAlert             The alert
	 * @return The DTO associated to the given alert
	 */
	Pair<ITableDto, IViewDto> getDtosFromPuiAlertConfiguration(IPuiAlertConfiguration puiAlertConfiguation,
			IPuiAlert puiAlert);

	/**
	 * Create the PK as String associated to the given Table DTO
	 * 
	 * @param tableDto The table DTO to build its PK as String
	 * @return The PK as String
	 */
	String createAlertPk(IDto tableDto);

	/**
	 * Get AlertableModel from the given Alert configuration and the Model
	 * configuration
	 * 
	 * @param puiAlertConfiguration The Alert configuration
	 * @param modelConfiguration    The Model configuration
	 * @return The Alert Model
	 */
	AlertableModel getAlertableModel(IPuiAlertConfiguration puiAlertConfiguration,
			ModelConfiguration modelConfiguration);

	/**
	 * Get AlertableColumnDefinition from the given Alert configuration and the
	 * Alertable model
	 * 
	 * @param puiAlertConfiguration The Alert configuration
	 * @param alertableModel        The Alertable model
	 * @return The Alertable column configuration
	 */
	AlertableColumnDefinition getAlertableColumnDefinition(IPuiAlertConfiguration puiAlertConfiguration,
			AlertableModel alertableModel);
}