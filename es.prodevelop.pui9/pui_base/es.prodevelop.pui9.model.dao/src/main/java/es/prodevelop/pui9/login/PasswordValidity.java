package es.prodevelop.pui9.login;

import java.time.Instant;

import es.prodevelop.pui9.utils.IPuiObject;

public class PasswordValidity implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private Boolean valid = false;
	private Boolean notifyExpiration = false;
	private Boolean changePasswordOnLogin = false;
	private Instant expireOn;

	public Boolean isValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Boolean isNotifyExpiration() {
		return notifyExpiration;
	}

	public void setNotifyExpiration(Boolean notifyExpiration) {
		this.notifyExpiration = notifyExpiration;
	}

	public Boolean isChangePasswordOnLogin() {
		return changePasswordOnLogin;
	}

	public void setChangePasswordOnLogin(Boolean changePasswordOnLogin) {
		this.changePasswordOnLogin = changePasswordOnLogin;
	}

	public Instant getExpireOn() {
		return expireOn;
	}

	public void setExpireOn(Instant expireOn) {
		this.expireOn = expireOn;
	}

}
