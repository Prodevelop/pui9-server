package es.prodevelop.pui9.common.model.dto.interfaces;

import java.util.List;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiUser extends IPuiUserPk {
	@PuiGenerated
	String NAME_COLUMN = "name";
	@PuiGenerated
	String NAME_FIELD = "name";
	@PuiGenerated
	String PASSWORD_COLUMN = "password";
	@PuiGenerated
	String PASSWORD_FIELD = "password";
	@PuiGenerated
	String LANGUAGE_COLUMN = "language";
	@PuiGenerated
	String LANGUAGE_FIELD = "language";
	@PuiGenerated
	String EMAIL_COLUMN = "email";
	@PuiGenerated
	String EMAIL_FIELD = "email";
	@PuiGenerated
	String DISABLED_COLUMN = "disabled";
	@PuiGenerated
	String DISABLED_FIELD = "disabled";
	@PuiGenerated
	String DISABLED_DATE_COLUMN = "disabled_date";
	@PuiGenerated
	String DISABLED_DATE_FIELD = "disableddate";
	@PuiGenerated
	String DATEFORMAT_COLUMN = "dateformat";
	@PuiGenerated
	String DATEFORMAT_FIELD = "dateformat";
	@PuiGenerated
	String RESET_PASSWORD_TOKEN_COLUMN = "reset_password_token";
	@PuiGenerated
	String RESET_PASSWORD_TOKEN_FIELD = "resetpasswordtoken";
	@PuiGenerated
	String LAST_ACCESS_TIME_COLUMN = "last_access_time";
	@PuiGenerated
	String LAST_ACCESS_TIME_FIELD = "lastaccesstime";
	@PuiGenerated
	String LAST_ACCESS_IP_COLUMN = "last_access_ip";
	@PuiGenerated
	String LAST_ACCESS_IP_FIELD = "lastaccessip";
	@PuiGenerated
	String LAST_PASSWORD_CHANGE_COLUMN = "last_password_change";
	@PuiGenerated
	String LAST_PASSWORD_CHANGE_FIELD = "lastpasswordchange";
	@PuiGenerated
	String LOGIN_WRONG_ATTEMPTS_COLUMN = "login_wrong_attempts";
	@PuiGenerated
	String LOGIN_WRONG_ATTEMPTS_FIELD = "loginwrongattempts";
	@PuiGenerated
	String CHANGE_PASSWORD_NEXT_LOGIN_COLUMN = "change_password_next_login";
	@PuiGenerated
	String CHANGE_PASSWORD_NEXT_LOGIN_FIELD = "changepasswordnextlogin";
	@PuiGenerated
	String SECRET_2FA_COLUMN = "secret_2fa";
	@PuiGenerated
	String SECRET_2FA_FIELD = "secret2fa";
	@PuiGenerated
	String RESET_PASSWORD_TOKEN_DATE_COLUMN = "reset_password_token_date";
	@PuiGenerated
	String RESET_PASSWORD_TOKEN_DATE_FIELD = "resetpasswordtokendate";

	@PuiGenerated
	String getName();

	@PuiGenerated
	void setName(String name);

	@PuiGenerated
	String getPassword();

	@PuiGenerated
	void setPassword(String password);

	@PuiGenerated
	String getLanguage();

	@PuiGenerated
	void setLanguage(String language);

	@PuiGenerated
	String getEmail();

	@PuiGenerated
	void setEmail(String email);

	@PuiGenerated
	Integer getDisabled();

	@PuiGenerated
	void setDisabled(Integer disabled);

	@PuiGenerated
	java.time.Instant getDisableddate();

	@PuiGenerated
	void setDisableddate(java.time.Instant disableddate);

	@PuiGenerated
	String getDateformat();

	@PuiGenerated
	void setDateformat(String dateformat);

	@PuiGenerated
	String getResetpasswordtoken();

	@PuiGenerated
	void setResetpasswordtoken(String resetpasswordtoken);

	@PuiGenerated
	java.time.Instant getLastaccesstime();

	@PuiGenerated
	void setLastaccesstime(java.time.Instant lastaccesstime);

	@PuiGenerated
	String getLastaccessip();

	@PuiGenerated
	void setLastaccessip(String lastaccessip);

	@PuiGenerated
	java.time.Instant getLastpasswordchange();

	@PuiGenerated
	void setLastpasswordchange(java.time.Instant lastpasswordchange);

	@PuiGenerated
	Integer getLoginwrongattempts();

	@PuiGenerated
	void setLoginwrongattempts(Integer loginwrongattempts);

	@PuiGenerated
	Integer getChangepasswordnextlogin();

	@PuiGenerated
	void setChangepasswordnextlogin(Integer changepasswordnextlogin);

	@PuiGenerated
	String getSecret2fa();

	@PuiGenerated
	void setSecret2fa(String secret2fa);

	@PuiGenerated
	java.time.Instant getResetpasswordtokendate();

	@PuiGenerated
	void setResetpasswordtokendate(java.time.Instant resetpasswordtokendate);

	String PROFILES_FIELD = "profiles";

	List<IPuiProfile> getProfiles();

	void setProfiles(List<IPuiProfile> profiles);
}
