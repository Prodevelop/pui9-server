package es.prodevelop.pui9.services.messages;

import es.prodevelop.pui9.messages.AbstractPuiMessages;

/**
 * Utility class to get internationalized messages for PUI Common
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiServiceMessages extends AbstractPuiMessages {

	private static PuiServiceMessages singleton;

	public static PuiServiceMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiServiceMessages();
		}
		return singleton;
	}

	private PuiServiceMessages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return PuiServiceResourceBundle.class;
	}

}
