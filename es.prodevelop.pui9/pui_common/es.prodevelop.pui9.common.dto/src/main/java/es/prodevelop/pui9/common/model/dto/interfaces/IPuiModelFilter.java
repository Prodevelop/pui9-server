package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiModelFilter extends IPuiModelFilterPk {
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String LABEL_COLUMN = "label";
	@PuiGenerated
	String LABEL_FIELD = "label";
	@PuiGenerated
	String DESCRIPTION_COLUMN = "description";
	@PuiGenerated
	String DESCRIPTION_FIELD = "description";
	@PuiGenerated
	String FILTER_COLUMN = "filter";
	@PuiGenerated
	String FILTER_FIELD = "filter";
	@PuiGenerated
	String ISDEFAULT_COLUMN = "isdefault";
	@PuiGenerated
	String ISDEFAULT_FIELD = "isdefault";

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getLabel();

	@PuiGenerated
	void setLabel(String label);

	@PuiGenerated
	String getDescription();

	@PuiGenerated
	void setDescription(String description);

	@PuiGenerated
	String getFilter();

	@PuiGenerated
	void setFilter(String filter);

	@PuiGenerated
	Integer getIsdefault();

	@PuiGenerated
	void setIsdefault(Integer isdefault);
}
