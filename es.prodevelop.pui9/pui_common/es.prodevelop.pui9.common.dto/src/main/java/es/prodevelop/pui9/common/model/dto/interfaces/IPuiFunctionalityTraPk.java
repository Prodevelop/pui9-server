package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiFunctionalityTraPk extends ITableDto {
	@PuiGenerated
	String FUNCTIONALITY_COLUMN = "functionality";
	@PuiGenerated
	String FUNCTIONALITY_FIELD = "functionality";
	@PuiGenerated
	String LANG_COLUMN = "lang";
	@PuiGenerated
	String LANG_FIELD = "lang";

	@PuiGenerated
	String getFunctionality();

	@PuiGenerated
	void setFunctionality(String functionality);

	@PuiGenerated
	String getLang();

	@PuiGenerated
	void setLang(String lang);
}
