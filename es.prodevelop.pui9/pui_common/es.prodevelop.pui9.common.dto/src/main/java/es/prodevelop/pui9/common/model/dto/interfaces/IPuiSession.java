package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiSession extends IPuiSessionPk {
	@PuiGenerated
	String EXPIRATION_COLUMN = "expiration";
	@PuiGenerated
	String EXPIRATION_FIELD = "expiration";
	@PuiGenerated
	String PERSISTENT_COLUMN = "persistent";
	@PuiGenerated
	String PERSISTENT_FIELD = "persistent";
	@PuiGenerated
	String LASTUSE_COLUMN = "lastuse";
	@PuiGenerated
	String LASTUSE_FIELD = "lastuse";
	@PuiGenerated
	String JWT_COLUMN = "jwt";
	@PuiGenerated
	String JWT_FIELD = "jwt";

	@PuiGenerated
	String getUsr();

	@PuiGenerated
	void setUsr(String usr);

	@PuiGenerated
	java.time.Instant getCreated();

	@PuiGenerated
	void setCreated(java.time.Instant created);

	@PuiGenerated
	java.time.Instant getExpiration();

	@PuiGenerated
	void setExpiration(java.time.Instant expiration);

	@PuiGenerated
	Integer getPersistent();

	@PuiGenerated
	void setPersistent(Integer persistent);

	@PuiGenerated
	java.time.Instant getLastuse();

	@PuiGenerated
	void setLastuse(java.time.Instant lastuse);

	@PuiGenerated
	String getJwt();

	@PuiGenerated
	void setJwt(String jwt);

	@PuiGenerated
	String CREATED_COLUMN = "created";
	@PuiGenerated
	String CREATED_FIELD = "created";
	@PuiGenerated
	String USR_COLUMN = "usr";
	@PuiGenerated
	String USR_FIELD = "usr";
}
