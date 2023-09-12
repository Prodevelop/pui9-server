package es.prodevelop.pui9.dashboards.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiDashboard extends IPuiDashboardPk {
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String DEFINITION_COLUMN = "definition";
	@PuiGenerated
	String DEFINITION_FIELD = "definition";

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	String getDefinition();

	@PuiGenerated
	void setDefinition(String definition);
}