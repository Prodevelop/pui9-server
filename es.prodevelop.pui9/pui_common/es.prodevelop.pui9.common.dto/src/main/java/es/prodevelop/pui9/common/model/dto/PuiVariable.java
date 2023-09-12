package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_variable")
public class PuiVariable extends PuiVariablePk implements IPuiVariable {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiVariable.VALUE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String value;
	@PuiGenerated
	@PuiField(columnname = IPuiVariable.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 500, islang = false, isgeometry = false, issequence = false)
	private String description;

	@PuiGenerated
	@Override
	public String getValue() {
		return value;
	}

	@PuiGenerated
	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@PuiGenerated
	@Override
	public String getDescription() {
		return description;
	}

	@PuiGenerated
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
