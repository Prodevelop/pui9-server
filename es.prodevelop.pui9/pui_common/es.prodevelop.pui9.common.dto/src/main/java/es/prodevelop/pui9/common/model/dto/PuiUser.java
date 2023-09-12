package es.prodevelop.pui9.common.model.dto;

import java.util.ArrayList;
import java.util.List;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfile;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_user")
public class PuiUser extends PuiUserPk implements IPuiUser {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.NAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	private String name;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.PASSWORD_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String password;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.LANGUAGE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	private String language;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.EMAIL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String email;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.DISABLED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer disabled = 0;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.DISABLED_DATE_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant disableddate;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.DATEFORMAT_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	private String dateformat = "dd/MM/yyyy";
	@PuiGenerated
	@PuiField(columnname = IPuiUser.RESET_PASSWORD_TOKEN_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String resetpasswordtoken;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.LAST_ACCESS_TIME_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant lastaccesstime;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.LAST_ACCESS_IP_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	private String lastaccessip;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.LAST_PASSWORD_CHANGE_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant lastpasswordchange;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.LOGIN_WRONG_ATTEMPTS_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer loginwrongattempts = 0;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.CHANGE_PASSWORD_NEXT_LOGIN_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer changepasswordnextlogin = 0;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.SECRET_2FA_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	private String secret2fa;
	@PuiGenerated
	@PuiField(columnname = IPuiUser.RESET_PASSWORD_TOKEN_DATE_COLUMN, ispk = false, nullable = true, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant resetpasswordtokendate;

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
	public String getPassword() {
		return password;
	}

	@PuiGenerated
	@Override
	public void setPassword(String password) {
		this.password = password;
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
	public String getResetpasswordtoken() {
		return resetpasswordtoken;
	}

	@PuiGenerated
	@Override
	public void setResetpasswordtoken(String resetpasswordtoken) {
		this.resetpasswordtoken = resetpasswordtoken;
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

	@PuiGenerated
	@Override
	public String getSecret2fa() {
		return secret2fa;
	}

	@PuiGenerated
	@Override
	public void setSecret2fa(String secret2fa) {
		this.secret2fa = secret2fa;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getResetpasswordtokendate() {
		return resetpasswordtokendate;
	}

	@PuiGenerated
	@Override
	public void setResetpasswordtokendate(java.time.Instant resetpasswordtokendate) {
		this.resetpasswordtokendate = resetpasswordtokendate;
	}

	private List<IPuiProfile> profiles = new ArrayList<>();

	@Override
	public List<IPuiProfile> getProfiles() {
		return profiles;
	}

	@Override
	public void setProfiles(List<IPuiProfile> profiles) {
		this.profiles = profiles;
	}
}
