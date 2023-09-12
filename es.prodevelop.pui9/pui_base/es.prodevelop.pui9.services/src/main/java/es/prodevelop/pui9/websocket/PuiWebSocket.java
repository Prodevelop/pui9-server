package es.prodevelop.pui9.websocket;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * This class allows to send messages over WebSockets to the clients. It
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiWebSocket {

	public static final String TOPIC_DESTINATION = "/topic/";

	@Autowired
	private SimpMessagingTemplate template;

	private PuiWebSocket() {
	}

	/**
	 * Send a message to given destination with the indicated payload
	 * 
	 * @param destination The destination of the message (/topic/sample)
	 * @param payload     The object to be sent
	 */
	public void sendMessage(String destination, Object payload) {
		this.sendMessage(destination, payload, null);
	}

	/**
	 * Send a message to given destination with the indicated payload. Additionally
	 * you can add some headers
	 * 
	 * @param destination The destination of the message (/topic/sample)
	 * @param payload     The object to be sent
	 * @param headers     Additional headers to sent
	 */
	public void sendMessage(String destination, Object payload, Map<String, Object> headers) {
		if (payload == null) {
			payload = "";
		}
		template.convertAndSend(destination, payload, headers);
	}

}
