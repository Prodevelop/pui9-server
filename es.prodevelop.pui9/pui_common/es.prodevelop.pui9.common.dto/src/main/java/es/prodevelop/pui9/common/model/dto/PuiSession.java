package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSession;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_session")
public class PuiSession extends PuiSessionPk implements IPuiSession {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiSession.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IPuiSession.CREATED_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant created;
	@PuiGenerated
	@PuiField(columnname = IPuiSession.EXPIRATION_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant expiration;
	@PuiGenerated
	@PuiField(columnname = IPuiSession.PERSISTENT_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer persistent = 0;
	@PuiGenerated
	@PuiField(columnname = IPuiSession.LASTUSE_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant lastuse;
	@PuiGenerated
	@PuiField(columnname = IPuiSession.JWT_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String jwt;

	@PuiGenerated
	@Override
	public String getUsr() {
		return usr;
	}

	@PuiGenerated
	@Override
	public void setUsr(String usr) {
		this.usr = usr;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getCreated() {
		return created;
	}

	@PuiGenerated
	@Override
	public void setCreated(java.time.Instant created) {
		this.created = created;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getExpiration() {
		return expiration;
	}

	@PuiGenerated
	@Override
	public void setExpiration(java.time.Instant expiration) {
		this.expiration = expiration;
	}

	@PuiGenerated
	@Override
	public Integer getPersistent() {
		return persistent;
	}

	@PuiGenerated
	@Override
	public void setPersistent(Integer persistent) {
		this.persistent = persistent;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getLastuse() {
		return lastuse;
	}

	@PuiGenerated
	@Override
	public void setLastuse(java.time.Instant lastuse) {
		this.lastuse = lastuse;
	}

	@PuiGenerated
	@Override
	public String getJwt() {
		return jwt;
	}

	@PuiGenerated
	@Override
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

}
