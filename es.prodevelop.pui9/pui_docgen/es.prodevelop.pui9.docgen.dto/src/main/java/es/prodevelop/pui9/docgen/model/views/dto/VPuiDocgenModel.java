package es.prodevelop.pui9.docgen.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenModel;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "v_pui_docgen_model")
public class VPuiDocgenModel extends AbstractViewDto implements IVPuiDocgenModel {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenModel.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenModel.ENTITY_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String entity;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenModel.LABEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 203, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String label;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocgenModel.IDENTITY_FIELDS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String identityfields;

	@PuiGenerated
	@Override
	public String getModel() {
		return model;
	}

	@PuiGenerated
	@Override
	public void setModel(String model) {
		this.model = model;
	}

	@PuiGenerated
	@Override
	public String getEntity() {
		return entity;
	}

	@PuiGenerated
	@Override
	public void setEntity(String entity) {
		this.entity = entity;
	}

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
