package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.configuration.ModelConfiguration;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_model")
public class PuiModel extends PuiModelPk implements IPuiModel {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiModel.ENTITY_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String entity;
	@PuiGenerated
	@PuiField(columnname = IPuiModel.CONFIGURATION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private ModelConfiguration configuration;
	@PuiGenerated
	@PuiField(columnname = IPuiModel.FILTER_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String filter;

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
	public ModelConfiguration getConfiguration() {
		return configuration;
	}

	@PuiGenerated
	@Override
	public void setConfiguration(ModelConfiguration configuration) {
		this.configuration = configuration;
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
}
