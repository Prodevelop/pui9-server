package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiProfileFunctionality extends IViewDto {
	@PuiGenerated
	String PROFILE_COLUMN = "profile";
	@PuiGenerated
	String PROFILE_FIELD = "profile";
	@PuiGenerated
	String PROFILE_NAME_COLUMN = "profile_name";
	@PuiGenerated
	String PROFILE_NAME_FIELD = "profilename";
	@PuiGenerated
	String FUNCTIONALITY_COLUMN = "functionality";
	@PuiGenerated
	String FUNCTIONALITY_FIELD = "functionality";
	@PuiGenerated
	String LANG_COLUMN = "lang";
	@PuiGenerated
	String LANG_FIELD = "lang";

	@PuiGenerated
	String getProfile();

	@PuiGenerated
	void setProfile(String profile);

	@PuiGenerated
	String getProfilename();

	@PuiGenerated
	void setProfilename(String profilename);

	@PuiGenerated
	String getFunctionality();

	@PuiGenerated
	void setFunctionality(String functionality);

	@PuiGenerated
	String getLang();

	@PuiGenerated
	void setLang(String lang);
}
