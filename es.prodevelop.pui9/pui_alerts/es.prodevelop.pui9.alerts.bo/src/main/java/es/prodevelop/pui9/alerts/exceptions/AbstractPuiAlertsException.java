package es.prodevelop.pui9.alerts.exceptions;

import es.prodevelop.pui9.alerts.messages.PuiAlertsMessages;
import es.prodevelop.pui9.exceptions.PuiServiceException;

public abstract class AbstractPuiAlertsException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	protected AbstractPuiAlertsException(Integer code) {
		super(code, PuiAlertsMessages.getSingleton().getString(code));
	}

	protected AbstractPuiAlertsException(Integer code, Exception cause) {
		super(cause, code, PuiAlertsMessages.getSingleton().getString(code));
	}

	protected AbstractPuiAlertsException(Integer code, Object... parameters) {
		super(code, PuiAlertsMessages.getSingleton().getString(code), parameters);
	}

}
