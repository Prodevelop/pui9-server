package es.prodevelop.pui9.keycloak.messages;

import java.util.LinkedHashMap;
import java.util.Map;

import es.prodevelop.pui9.keycloak.exceptions.PuiKeycloakBadTokenException;
import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * Keycloak component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiKeycloakResourceBundle extends AbstractPuiListResourceBundle {

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new LinkedHashMap<>();

		// Exceptions
		messages.put(PuiKeycloakBadTokenException.CODE, getBadTokenMessage_291());

		return messages;
	}

	protected abstract String getBadTokenMessage_291();

}
