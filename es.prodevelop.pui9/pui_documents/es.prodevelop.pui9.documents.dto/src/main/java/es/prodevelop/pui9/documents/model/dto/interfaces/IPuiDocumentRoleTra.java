package es.prodevelop.pui9.documents.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiDocumentRoleTra extends IPuiDocumentRoleTraPk {
	@PuiGenerated
	String LANG_STATUS_COLUMN = "lang_status";
	@PuiGenerated
	String LANG_STATUS_FIELD = "langstatus";
	@PuiGenerated
	String DESCRIPTION_COLUMN = "description";
	@PuiGenerated
	String DESCRIPTION_FIELD = "description";

	@PuiGenerated
	Integer getLangstatus();

	@PuiGenerated
	void setLangstatus(Integer langstatus);

	@PuiGenerated
	String getDescription();

	@PuiGenerated
	void setDescription(String description);
}
