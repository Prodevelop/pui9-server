package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiModel;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "v_pui_model")
public class VPuiModel extends AbstractViewDto implements IVPuiModel {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiModel.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IVPuiModel.ENTITY_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String entity;
	@PuiGenerated
	@PuiField(columnname = IVPuiModel.FILTER_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String filter;
	@PuiGenerated
	@PuiField(columnname = IVPuiModel.CONFIGURATION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.hidden)
	private String configuration;

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
	public String getFilter() {
		return filter;
	}

	@PuiGenerated
	@Override
	public void setFilter(String filter) {
		this.filter = filter;
	}

	@PuiGenerated
	@Override
	public String getConfiguration() {
		return configuration;
	}

	@PuiGenerated
	@Override
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}
}