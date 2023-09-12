package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiUserFunctionality extends IViewDto {
	@PuiGenerated
	String USR_COLUMN = "usr";
	@PuiGenerated
	String USR_FIELD = "usr";
	@PuiGenerated
	String FUNCTIONALITY_COLUMN = "functionality";
	@PuiGenerated
	String FUNCTIONALITY_FIELD = "functionality";
	@PuiGenerated
	String FUNCTIONALITY_NAME_COLUMN = "functionality_name";
	@PuiGenerated
	String FUNCTIONALITY_NAME_FIELD = "functionalityname";
	@PuiGenerated
	String PROFILE_COLUMN = "profile";
	@PuiGenerated
	String PROFILE_FIELD = "profile";
	@PuiGenerated
	String PROFILE_NAME_COLUMN = "profile_name";
	@PuiGenerated
	String PROFILE_NAME_FIELD = "profilename";

	@PuiGenerated
	String getUsr();

	@PuiGenerated
	void setUsr(String usr);

	@PuiGenerated
	String getFunctionality();

	@PuiGenerated
	void setFunctionality(String functionality);

	@PuiGenerated
	String getFunctionalityname();

	@PuiGenerated
	void setFunctionalityname(String functionalityname);

	@PuiGenerated
	String getProfile();

	@PuiGenerated
	void setProfile(String profile);

	@PuiGenerated
	String getProfilename();

	@PuiGenerated
	void setProfilename(String profilename);
}
