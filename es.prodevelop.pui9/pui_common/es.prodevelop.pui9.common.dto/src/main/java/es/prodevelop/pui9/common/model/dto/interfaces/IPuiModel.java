package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.configuration.ModelConfiguration;

@PuiGenerated
public interface IPuiModel extends IPuiModelPk {
	@PuiGenerated
	String ENTITY_COLUMN = "entity";
	@PuiGenerated
	String ENTITY_FIELD = "entity";
	@PuiGenerated
	String CONFIGURATION_COLUMN = "configuration";
	@PuiGenerated
	String CONFIGURATION_FIELD = "configuration";
	@PuiGenerated
	String FILTER_COLUMN = "filter";
	@PuiGenerated
	String FILTER_FIELD = "filter";

	@PuiGenerated
	String getEntity();

	@PuiGenerated
	void setEntity(String entity);

	@PuiGenerated
	ModelConfiguration getConfiguration();

	@PuiGenerated
	void setConfiguration(ModelConfiguration configuration);

	@PuiGenerated
	String getFilter();

	@PuiGenerated
	void setFilter(String filter);
}
