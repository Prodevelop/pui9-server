package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.login.LoginData;
import es.prodevelop.pui9.login.PuiUserSession;

/**
 * Event fired when creating a session
 */
public class SessionCreatedEvent extends PuiEvent<PuiUserSession> {

	private static final long serialVersionUID = 1L;

	private LoginData loginData;

	public SessionCreatedEvent(PuiUserSession session, LoginData loginData) {
		super(session);
		this.loginData = loginData;
	}

	public LoginData getLoginData() {
		return loginData;
	}

}
