package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiFunctionalityTra extends IPuiFunctionalityTraPk {
	@PuiGenerated
	String LANG_STATUS_COLUMN = "lang_status";
	@PuiGenerated
	String LANG_STATUS_FIELD = "langstatus";
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";

	@PuiGenerated
	Integer getLangstatus();

	@PuiGenerated
	void setLangstatus(Integer langstatus);

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);
}
