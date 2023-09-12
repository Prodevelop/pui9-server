package es.prodevelop.pui9.services.exceptions;

import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.services.messages.PuiServiceMessages;

public abstract class AbstractPuiServicesException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public AbstractPuiServicesException(Exception cause, Integer code) {
		super(cause, code, cause.getMessage());
	}

	public AbstractPuiServicesException(Integer code) {
		this(code, new Object[0]);
	}

	public AbstractPuiServicesException(Integer code, Object... parameters) {
		super(null, code, PuiServiceMessages.getSingleton().getString(code), parameters);
	}

}