package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiVariablePk extends ITableDto {
	@PuiGenerated
	String VARIABLE_COLUMN = "variable";
	@PuiGenerated
	String VARIABLE_FIELD = "variable";

	@PuiGenerated
	String getVariable();

	@PuiGenerated
	void setVariable(String variable);
}
