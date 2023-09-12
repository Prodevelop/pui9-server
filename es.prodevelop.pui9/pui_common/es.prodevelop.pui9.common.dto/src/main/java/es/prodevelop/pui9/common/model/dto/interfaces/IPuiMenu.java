package es.prodevelop.pui9.common.model.dto.interfaces;

import java.util.List;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiMenu extends IPuiMenuPk {
	@PuiGenerated
	String PARENT_COLUMN = "parent";
	@PuiGenerated
	String PARENT_FIELD = "parent";
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String COMPONENT_COLUMN = "component";
	@PuiGenerated
	String COMPONENT_FIELD = "component";
	@PuiGenerated
	String FUNCTIONALITY_COLUMN = "functionality";
	@PuiGenerated
	String FUNCTIONALITY_FIELD = "functionality";
	@PuiGenerated
	String LABEL_COLUMN = "label";
	@PuiGenerated
	String LABEL_FIELD = "label";
	@PuiGenerated
	String ICON_LABEL_COLUMN = "icon_label";
	@PuiGenerated
	String ICON_LABEL_FIELD = "iconlabel";

	@PuiGenerated
	Integer getParent();

	@PuiGenerated
	void setParent(Integer parent);

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getComponent();

	@PuiGenerated
	void setComponent(String component);

	@PuiGenerated
	String getFunctionality();

	@PuiGenerated
	void setFunctionality(String functionality);

	@PuiGenerated
	String getLabel();

	@PuiGenerated
	void setLabel(String label);

	@PuiGenerated
	String getIconlabel();

	@PuiGenerated
	void setIconlabel(String iconlabel);

	List<IPuiMenu> getChildren();

	void setChildren(List<IPuiMenu> children);
}
