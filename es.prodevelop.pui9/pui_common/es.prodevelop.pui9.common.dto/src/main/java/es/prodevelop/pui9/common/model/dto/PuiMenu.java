package es.prodevelop.pui9.common.model.dto;

import java.util.ArrayList;
import java.util.List;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenu;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_menu")
public class PuiMenu extends PuiMenuPk implements IPuiMenu {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiMenu.PARENT_COLUMN, ispk = false, nullable = true, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer parent;
	@PuiGenerated
	@PuiField(columnname = IPuiMenu.MODEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IPuiMenu.COMPONENT_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String component;
	@PuiGenerated
	@PuiField(columnname = IPuiMenu.FUNCTIONALITY_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String functionality;
	@PuiGenerated
	@PuiField(columnname = IPuiMenu.LABEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String label;
	@PuiGenerated
	@PuiField(columnname = IPuiMenu.ICON_LABEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String iconlabel;

	@PuiGenerated
	@Override
	public Integer getParent() {
		return parent;
	}

	@PuiGenerated
	@Override
	public void setParent(Integer parent) {
		this.parent = parent;
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
	public String getComponent() {
		return component;
	}

	@PuiGenerated
	@Override
	public void setComponent(String component) {
		this.component = component;
	}

	@PuiGenerated
	@Override
	public String getFunctionality() {
		return functionality;
	}

	@PuiGenerated
	@Override
	public void setFunctionality(String functionality) {
		this.functionality = functionality;
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
	public String getIconlabel() {
		return iconlabel;
	}

	@PuiGenerated
	@Override
	public void setIconlabel(String iconlabel) {
		this.iconlabel = iconlabel;
	}

	private List<IPuiMenu> children = new ArrayList<>();

	@Override
	public List<IPuiMenu> getChildren() {
		return children;
	}

	@Override
	public void setChildren(List<IPuiMenu> children) {
		this.children = children;
	}
}
