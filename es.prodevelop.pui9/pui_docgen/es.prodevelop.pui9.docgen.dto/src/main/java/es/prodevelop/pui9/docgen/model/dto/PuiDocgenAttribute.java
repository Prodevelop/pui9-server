package es.prodevelop.pui9.docgen.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttribute;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_docgen_attribute")
public class PuiDocgenAttribute extends PuiDocgenAttributePk implements IPuiDocgenAttribute {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenAttribute.LABEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String label;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenAttribute.VALUE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 500, islang = false, isgeometry = false, issequence = false)
	private String value;

	@PuiGenerated
	@Override
	public String getLabel() {
		return label;
	}

	@PuiGenerated
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

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
}
