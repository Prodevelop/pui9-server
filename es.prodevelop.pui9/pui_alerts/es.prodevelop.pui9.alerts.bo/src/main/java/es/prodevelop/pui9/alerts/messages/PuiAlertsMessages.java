package es.prodevelop.pui9.alerts.messages;

import es.prodevelop.pui9.messages.AbstractPuiMessages;

/**
 * Utility class to get internationalized messages for PUI Alerts
 * 
 */
public class PuiAlertsMessages extends AbstractPuiMessages {

	private static PuiAlertsMessages singleton;

	public static PuiAlertsMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiAlertsMessages();
		}
		return singleton;
	}

	private PuiAlertsMessages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return PuiAlertsResourceBundle.class;
	}

}
