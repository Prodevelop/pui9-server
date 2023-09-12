package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@PuiGenerated
public interface IPuiUserProfilePk extends ITableDto {
	@PuiGenerated
	String USR_COLUMN = "usr";
	@PuiGenerated
	String USR_FIELD = "usr";
	@PuiGenerated
	String PROFILE_COLUMN = "profile";
	@PuiGenerated
	String PROFILE_FIELD = "profile";

	@PuiGenerated
	String getUsr();

	@PuiGenerated
	void setUsr(String usr);

	@PuiGenerated
	String getProfile();

	@PuiGenerated
	void setProfile(String profile);
}
