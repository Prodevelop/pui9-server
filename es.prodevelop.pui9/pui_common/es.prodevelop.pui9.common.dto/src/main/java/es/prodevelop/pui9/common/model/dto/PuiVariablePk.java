package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariablePk;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.utils.PuiObjectUtils;

@PuiGenerated
public class PuiVariablePk extends AbstractTableDto implements IPuiVariablePk {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiVariablePk.VARIABLE_COLUMN, ispk = true, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	private String variable;

	@PuiGenerated
	public PuiVariablePk() {
	}

	@PuiGenerated
	public PuiVariablePk(String variable) {
		this.variable = variable;
	}

	@PuiGenerated
	@Override
	public String getVariable() {
		return variable;
	}

	@PuiGenerated
	@Override
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@PuiGenerated
	@Override
	@SuppressWarnings("unchecked")
	public PuiVariablePk createPk() {
		PuiVariablePk pk = new PuiVariablePk();
		PuiObjectUtils.copyProperties(pk, this);
		return pk;
	}
}
