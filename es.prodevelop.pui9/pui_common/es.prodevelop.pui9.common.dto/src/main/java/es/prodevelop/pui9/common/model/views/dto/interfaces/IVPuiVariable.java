package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiVariable extends IViewDto {
	@PuiGenerated
	String VARIABLE_COLUMN = "variable";
	@PuiGenerated
	String VARIABLE_FIELD = "variable";
	@PuiGenerated
	String VALUE_COLUMN = "value";
	@PuiGenerated
	String VALUE_FIELD = "value";
	@PuiGenerated
	String DESCRIPTION_COLUMN = "description";
	@PuiGenerated
	String DESCRIPTION_FIELD = "description";

	@PuiGenerated
	String getVariable();

	@PuiGenerated
	void setVariable(String variable);

	@PuiGenerated
	String getValue();

	@PuiGenerated
	void setValue(String value);

	@PuiGenerated
	String getDescription();

	@PuiGenerated
	void setDescription(String description);
}
