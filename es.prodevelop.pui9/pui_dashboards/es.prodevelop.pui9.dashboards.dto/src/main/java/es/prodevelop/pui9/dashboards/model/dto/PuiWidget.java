package es.prodevelop.pui9.dashboards.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidget;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_widget")
public class PuiWidget extends PuiWidgetPk implements IPuiWidget {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiWidget.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String name;
	@PuiGenerated
	@PuiField(columnname = IPuiWidget.TYPEID_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer typeid;
	@PuiGenerated
	@PuiField(columnname = IPuiWidget.DEFINITION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String definition;

	@PuiGenerated
	@Override
	public String getName() {
		return name;
	}

	@PuiGenerated
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@PuiGenerated
	@Override
	public Integer getTypeid() {
		return typeid;
	}

	@PuiGenerated
	@Override
	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}

	@PuiGenerated
	@Override
	public String getDefinition() {
		return definition;
	}

	@PuiGenerated
	@Override
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	private String type;
	private String component;

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getComponent() {
		return component;
	}

	@Override
	public void setComponent(String component) {
		this.component = component;
	}
}