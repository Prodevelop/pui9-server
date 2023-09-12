package es.prodevelop.pui9.documents.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiDocumentRoleTraPk extends ITableDto {
	@PuiGenerated
	String ROLE_COLUMN = "role";
	@PuiGenerated
	String ROLE_FIELD = "role";
	@PuiGenerated
	String LANG_COLUMN = "lang";
	@PuiGenerated
	String LANG_FIELD = "lang";

	@PuiGenerated
	String getRole();

	@PuiGenerated
	void setRole(String role);

	@PuiGenerated
	String getLang();

	@PuiGenerated
	void setLang(String lang);
}
