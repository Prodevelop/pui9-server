package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilter;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.GeometryType;

@PuiEntity(tablename = "pui_user_model_filter")
@PuiGenerated
public class PuiUserModelFilter extends PuiUserModelFilterPk implements IPuiUserModelFilter {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiUserModelFilter.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IPuiUserModelFilter.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IPuiUserModelFilter.LABEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String label;
	@PuiGenerated
	@PuiField(columnname = IPuiUserModelFilter.FILTER_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private es.prodevelop.pui9.filter.FilterGroup filter;
	@PuiGenerated
	@PuiField(columnname = IPuiUserModelFilter.ISDEFAULT_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private Integer isdefault = 0;

	@PuiGenerated
	@Override
	public String getUsr() {
		return usr;
	}

	@PuiGenerated
	@Override
	public void setUsr(String usr) {
		this.usr = usr;
	}

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
	public es.prodevelop.pui9.filter.FilterGroup getFilter() {
		return filter;
	}

	@PuiGenerated
	@Override
	public void setFilter(es.prodevelop.pui9.filter.FilterGroup filter) {
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
