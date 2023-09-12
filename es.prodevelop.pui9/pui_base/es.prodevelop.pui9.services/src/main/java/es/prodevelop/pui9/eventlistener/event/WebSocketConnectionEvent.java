package es.prodevelop.pui9.eventlistener.event;

/**
 * Event for a WebSocket connection event
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class WebSocketConnectionEvent extends PuiEvent<String> {

	private static final long serialVersionUID = 1L;

	private String user;

	public WebSocketConnectionEvent(String sessionId, String user) {
		super(sessionId);
		this.user = user;
	}

	public String getSessionId() {
		return getSource();
	}

	public String getUser() {
		return user;
	}

}
