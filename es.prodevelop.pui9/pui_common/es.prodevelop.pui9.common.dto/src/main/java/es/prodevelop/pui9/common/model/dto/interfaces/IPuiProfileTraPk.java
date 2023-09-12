package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiProfileTraPk extends ITableDto {
	@PuiGenerated
	String PROFILE_COLUMN = "profile";
	@PuiGenerated
	String PROFILE_FIELD = "profile";
	@PuiGenerated
	String LANG_COLUMN = "lang";
	@PuiGenerated
	String LANG_FIELD = "lang";

	@PuiGenerated
	String getProfile();

	@PuiGenerated
	void setProfile(String profile);

	@PuiGenerated
	String getLang();

	@PuiGenerated
	void setLang(String lang);
}
