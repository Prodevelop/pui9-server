package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiProfile extends IViewDto {
	@PuiGenerated
	String PROFILE_COLUMN = "profile";
	@PuiGenerated
	String PROFILE_FIELD = "profile";
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String LANG_COLUMN = "lang";
	@PuiGenerated
	String LANG_FIELD = "lang";

	@PuiGenerated
	String getProfile();

	@PuiGenerated
	void setProfile(String profile);

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	String getLang();

	@PuiGenerated
	void setLang(String lang);
}
