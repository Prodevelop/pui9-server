package es.prodevelop.pui9.notifications.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiUserFcm extends IPuiUserFcmPk {
	@PuiGenerated
	String USR_COLUMN = "usr";
	@PuiGenerated
	String USR_FIELD = "usr";
	@PuiGenerated
	String LAST_USE_COLUMN = "last_use";
	@PuiGenerated
	String LAST_USE_FIELD = "lastuse";

	@PuiGenerated
	String getUsr();

	@PuiGenerated
	void setUsr(String usr);

	@PuiGenerated
	java.time.Instant getLastuse();

	@PuiGenerated
	void setLastuse(java.time.Instant lastuse);
}
