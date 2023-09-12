package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.login.LoginEventData;

/**
 * Event for the Login of the user
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class LoginEvent extends PuiEvent<LoginEventData> {

	private static final long serialVersionUID = 1L;

	public LoginEvent(LoginEventData loginEventData) {
		super(loginEventData);
	}

}
