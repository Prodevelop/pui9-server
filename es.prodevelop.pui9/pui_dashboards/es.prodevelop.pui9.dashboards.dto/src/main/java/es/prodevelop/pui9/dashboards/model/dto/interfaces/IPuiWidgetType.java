package es.prodevelop.pui9.dashboards.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiWidgetType extends IPuiWidgetTypePk {
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String TYPE_COLUMN = "type";
	@PuiGenerated
	String TYPE_FIELD = "type";
	@PuiGenerated
	String COMPONENT_COLUMN = "component";
	@PuiGenerated
	String COMPONENT_FIELD = "component";
	@PuiGenerated
	String DEFINITION_COLUMN = "definition";
	@PuiGenerated
	String DEFINITION_FIELD = "definition";

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	String getType();

	@PuiGenerated
	void setType(String type);

	@PuiGenerated
	String getComponent();

	@PuiGenerated
	void setComponent(String component);

	@PuiGenerated
	String getDefinition();

	@PuiGenerated
	void setDefinition(String definition);
}