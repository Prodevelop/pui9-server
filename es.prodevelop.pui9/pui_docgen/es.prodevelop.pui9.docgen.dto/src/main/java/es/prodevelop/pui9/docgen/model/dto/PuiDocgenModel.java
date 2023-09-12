package es.prodevelop.pui9.docgen.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenModel;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_docgen_model")
public class PuiDocgenModel extends PuiDocgenModelPk implements IPuiDocgenModel {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenModel.LABEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String label;
	@PuiGenerated
	@PuiField(columnname = IPuiDocgenModel.IDENTITY_FIELDS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String identityfields;

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
	public String getIdentityfields() {
		return identityfields;
	}

	@PuiGenerated
	@Override
	public void setIdentityfields(String identityfields) {
		this.identityfields = identityfields;
	}
}
