package es.prodevelop.pui9.eventlistener.event;

import java.util.List;
import java.util.Map;

/**
 * Event for a WebSocket unsubscription event
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class WebSocketUnsubscriptionEvent extends PuiEvent<String> {

	private static final long serialVersionUID = 1L;

	private String user;
	private String topic;
	private Map<String, List<String>> headers;

	public WebSocketUnsubscriptionEvent(String sessionId, String user, String topic,
			Map<String, List<String>> headers) {
		super(sessionId);
		this.user = user;
		this.topic = topic;
		this.headers = headers;
	}

	public String getSessionId() {
		return getSource();
	}

	public String getUser() {
		return user;
	}

	public String getTopic() {
		return topic;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

}