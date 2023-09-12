package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiVariable extends IPuiVariablePk {
	String VARIABLE_WITHOUT_VALUE = "-";
	@PuiGenerated
	String VALUE_COLUMN = "value";
	@PuiGenerated
	String VALUE_FIELD = "value";
	@PuiGenerated
	String DESCRIPTION_COLUMN = "description";
	@PuiGenerated
	String DESCRIPTION_FIELD = "description";

	@PuiGenerated
	String getValue();

	@PuiGenerated
	void setValue(String value);

	@PuiGenerated
	String getDescription();

	@PuiGenerated
	void setDescription(String description);
}
