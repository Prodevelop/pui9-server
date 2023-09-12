package es.prodevelop.pui9.dashboards.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiDashboard extends IViewDto {
	@PuiGenerated
	String ID_COLUMN = "id";
	@PuiGenerated
	String ID_FIELD = "id";
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String DEFINITION_COLUMN = "definition";
	@PuiGenerated
	String DEFINITION_FIELD = "definition";

	@PuiGenerated
	Integer getId();

	@PuiGenerated
	void setId(Integer id);

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	String getDefinition();

	@PuiGenerated
	void setDefinition(String definition);
}