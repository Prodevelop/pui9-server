package es.prodevelop.pui9.notifications.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;

@PuiGenerated
@PuiEntity(tablename = "pui_user_fcm")
public class PuiUserFcm extends PuiUserFcmPk implements IPuiUserFcm {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiUserFcm.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IPuiUserFcm.LAST_USE_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant lastuse;

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
	public java.time.Instant getLastuse() {
		return lastuse;
	}

	@PuiGenerated
	@Override
	public void setLastuse(java.time.Instant lastuse) {
		this.lastuse = lastuse;
	}
}
