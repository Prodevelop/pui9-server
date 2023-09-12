package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.login.PasswordValidity;

/**
 * Event for Password Validity for sending Emails when the password is about to
 * be expired
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PasswordExpirationEmailEvent extends PuiEvent<IPuiUser> {

	private static final long serialVersionUID = 1L;

	private PasswordValidity passwordValidity;

	public PasswordExpirationEmailEvent(IPuiUser puiUser, PasswordValidity passwordValidity) {
		super(puiUser);
		this.passwordValidity = passwordValidity;
	}

	public PasswordValidity getPasswordValidity() {
		return passwordValidity;
	}

}
