package es.prodevelop.pui9.alerts.messages;

import java.util.LinkedHashMap;
import java.util.Map;

import es.prodevelop.pui9.alerts.exceptions.PuiAlertsNoConfigurationException;
import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * Alerts component
 * 
 */
public abstract class PuiAlertsResourceBundle extends AbstractPuiListResourceBundle {

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new LinkedHashMap<>();

		// Exceptions
		messages.put(PuiAlertsNoConfigurationException.CODE, getNoConfigurationMessage_601());

		return messages;
	}

	protected abstract String getNoConfigurationMessage_601();
}
