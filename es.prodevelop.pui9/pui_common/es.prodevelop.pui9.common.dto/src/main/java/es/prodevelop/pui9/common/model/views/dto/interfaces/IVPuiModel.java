package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiModel extends IViewDto {
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String ENTITY_COLUMN = "entity";
	@PuiGenerated
	String ENTITY_FIELD = "entity";
	@PuiGenerated
	String FILTER_COLUMN = "filter";
	@PuiGenerated
	String FILTER_FIELD = "filter";
	@PuiGenerated
	String CONFIGURATION_COLUMN = "configuration";
	@PuiGenerated
	String CONFIGURATION_FIELD = "configuration";

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getEntity();

	@PuiGenerated
	void setEntity(String entity);

	@PuiGenerated
	String getFilter();

	@PuiGenerated
	void setFilter(String filter);

	@PuiGenerated
	String getConfiguration();

	@PuiGenerated
	void setConfiguration(String configuration);
}