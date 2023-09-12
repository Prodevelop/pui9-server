package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilter;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_model_filter")
public class PuiModelFilter extends PuiModelFilterPk implements IPuiModelFilter {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiModelFilter.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IPuiModelFilter.LABEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private String label;
	@PuiGenerated
	@PuiField(columnname = IPuiModelFilter.DESCRIPTION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 300, islang = false, isgeometry = false, issequence = false)
	private String description;
	@PuiGenerated
	@PuiField(columnname = IPuiModelFilter.FILTER_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String filter;
	@PuiGenerated
	@PuiField(columnname = IPuiModelFilter.ISDEFAULT_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer isdefault = 0;

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
	public String getDescription() {
		return description;
	}

	@PuiGenerated
	@Override
	public void setDescription(String description) {
		this.description = description;
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
	public Integer getIsdefault() {
		return isdefault;
	}

	@PuiGenerated
	@Override
	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}
}
