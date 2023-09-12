package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "v_pui_user")
public class VPuiUser extends AbstractViewDto implements IVPuiUser {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String name;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.EMAIL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String email;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.LANGUAGE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String language;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.DATEFORMAT_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String dateformat;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.DISABLED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 6, visibility = ColumnVisibility.visible)
	private Integer disabled;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.DISABLED_DATE_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 7, visibility = ColumnVisibility.hidden)
	private java.time.Instant disableddate;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.LAST_ACCESS_TIME_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 8, visibility = ColumnVisibility.visible)
	private java.time.Instant lastaccesstime;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.LAST_ACCESS_IP_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 9, visibility = ColumnVisibility.visible)
	private String lastaccessip;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.LAST_PASSWORD_CHANGE_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 10, visibility = ColumnVisibility.visible)
	private java.time.Instant lastpasswordchange;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.LOGIN_WRONG_ATTEMPTS_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 11, visibility = ColumnVisibility.visible)
	private Integer loginwrongattempts;
	@PuiGenerated
	@PuiField(columnname = IVPuiUser.CHANGE_PASSWORD_NEXT_LOGIN_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 12, visibility = ColumnVisibility.visible)
	private Integer changepasswordnextlogin;

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
	public String getName() {
		return name;
	}

	@PuiGenerated
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@PuiGenerated
	@Override
	public String getEmail() {
		return email;
	}

	@PuiGenerated
	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@PuiGenerated
	@Override
	public String getLanguage() {
		return language;
	}

	@PuiGenerated
	@Override
	public void setLanguage(String language) {
		this.language = language;
	}

	@PuiGenerated
	@Override
	public String getDateformat() {
		return dateformat;
	}

	@PuiGenerated
	@Override
	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	@PuiGenerated
	@Override
	public Integer getDisabled() {
		return disabled;
	}

	@PuiGenerated
	@Override
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getDisableddate() {
		return disableddate;
	}

	@PuiGenerated
	@Override
	public void setDisableddate(java.time.Instant disableddate) {
		this.disableddate = disableddate;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getLastaccesstime() {
		return lastaccesstime;
	}

	@PuiGenerated
	@Override
	public void setLastaccesstime(java.time.Instant lastaccesstime) {
		this.lastaccesstime = lastaccesstime;
	}

	@PuiGenerated
	@Override
	public String getLastaccessip() {
		return lastaccessip;
	}

	@PuiGenerated
	@Override
	public void setLastaccessip(String lastaccessip) {
		this.lastaccessip = lastaccessip;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getLastpasswordchange() {
		return lastpasswordchange;
	}

	@PuiGenerated
	@Override
	public void setLastpasswordchange(java.time.Instant lastpasswordchange) {
		this.lastpasswordchange = lastpasswordchange;
	}

	@PuiGenerated
	@Override
	public Integer getLoginwrongattempts() {
		return loginwrongattempts;
	}

	@PuiGenerated
	@Override
	public void setLoginwrongattempts(Integer loginwrongattempts) {
		this.loginwrongattempts = loginwrongattempts;
	}

	@PuiGenerated
	@Override
	public Integer getChangepasswordnextlogin() {
		return changepasswordnextlogin;
	}

	@PuiGenerated
	@Override
	public void setChangepasswordnextlogin(Integer changepasswordnextlogin) {
		this.changepasswordnextlogin = changepasswordnextlogin;
	}
}
