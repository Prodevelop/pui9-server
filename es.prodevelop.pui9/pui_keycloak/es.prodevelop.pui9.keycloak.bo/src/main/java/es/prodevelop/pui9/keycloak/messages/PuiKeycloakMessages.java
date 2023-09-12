package es.prodevelop.pui9.keycloak.messages;

import es.prodevelop.pui9.messages.AbstractPuiMessages;

/**
 * Utility class to get internationalized messages for PUI Keycloak
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiKeycloakMessages extends AbstractPuiMessages {

	private static PuiKeycloakMessages singleton;

	public static PuiKeycloakMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiKeycloakMessages();
		}
		return singleton;
	}

	private PuiKeycloakMessages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return PuiKeycloakResourceBundle.class;
	}

}
