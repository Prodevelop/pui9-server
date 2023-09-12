package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.login.LoginEventData;

/**
 * Event for the Logout of the user. Session still exists when this event is
 * fired
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class LogoutEvent extends PuiEvent<LoginEventData> {

	private static final long serialVersionUID = 1L;

	public LogoutEvent(LoginEventData loginEventData) {
		super(loginEventData);
	}

}
