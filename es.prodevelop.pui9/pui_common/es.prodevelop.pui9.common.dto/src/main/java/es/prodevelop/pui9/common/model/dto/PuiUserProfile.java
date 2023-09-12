package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfile;

@PuiGenerated
@PuiEntity(tablename = "pui_user_profile")
public class PuiUserProfile extends PuiUserProfilePk implements IPuiUserProfile {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
}
