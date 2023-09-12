package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSession;
import es.prodevelop.pui9.login.PuiUserSession;

/**
 * Event fired when destroying a session. JWT and UUID will always have value;
 * PuiUserSession will only have value if the user is currently logged into the
 * application; IPuiSession will only have value if the session is stored in the
 * database
 */
public class SessionDestroyedEvent extends PuiEvent<String> {

	private static final long serialVersionUID = 1L;

	private String uuid;
	private PuiUserSession userSession;
	private IPuiSession puiSession;

	public SessionDestroyedEvent(String jwt, String uuid, PuiUserSession userSession, IPuiSession puiSession) {
		super(jwt);
		this.uuid = uuid;
		this.userSession = userSession;
		this.puiSession = puiSession;
	}

	public String getUuid() {
		return uuid;
	}

	public PuiUserSession getUserSession() {
		return userSession;
	}

	public IPuiSession getPuiSession() {
		return puiSession;
	}

}
