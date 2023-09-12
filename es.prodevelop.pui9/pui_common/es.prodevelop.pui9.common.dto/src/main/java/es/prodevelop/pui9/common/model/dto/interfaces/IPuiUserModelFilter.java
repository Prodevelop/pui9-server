package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiUserModelFilter extends IPuiUserModelFilterPk {
	@PuiGenerated
	String USR_COLUMN = "usr";
	@PuiGenerated
	String USR_FIELD = "usr";
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String LABEL_COLUMN = "label";
	@PuiGenerated
	String LABEL_FIELD = "label";
	@PuiGenerated
	String FILTER_COLUMN = "filter";
	@PuiGenerated
	String FILTER_FIELD = "filter";
	@PuiGenerated
	String ISDEFAULT_COLUMN = "isdefault";
	@PuiGenerated
	String ISDEFAULT_FIELD = "isdefault";

	@PuiGenerated
	String getUsr();

	@PuiGenerated
	void setUsr(String usr);

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getLabel();

	@PuiGenerated
	void setLabel(String label);

	@PuiGenerated
	es.prodevelop.pui9.filter.FilterGroup getFilter();

	@PuiGenerated
	void setFilter(es.prodevelop.pui9.filter.FilterGroup filter);

	@PuiGenerated
	Integer getIsdefault();

	@PuiGenerated
	void setIsdefault(Integer isdefault);
}
