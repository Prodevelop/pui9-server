package es.prodevelop.pui9.services.exceptions;

import org.springframework.http.HttpStatus;

public class PuiServiceUserNotAuthenticatedException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 225;

	public PuiServiceUserNotAuthenticatedException() {
		super(CODE);
		setShouldLog(false);
		setStatusResponse(HttpStatus.UNAUTHORIZED.value());
	}

}
