package es.prodevelop.pui9.dashboards.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiWidget extends IPuiWidgetPk {
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String TYPEID_COLUMN = "typeid";
	@PuiGenerated
	String TYPEID_FIELD = "typeid";
	@PuiGenerated
	String DEFINITION_COLUMN = "definition";
	@PuiGenerated
	String DEFINITION_FIELD = "definition";

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	Integer getTypeid();

	@PuiGenerated
	void setTypeid(Integer typeid);

	@PuiGenerated
	String getDefinition();

	@PuiGenerated
	void setDefinition(String definition);

	String getType();

	void setType(String type);

	String getComponent();

	void setComponent(String component);
}