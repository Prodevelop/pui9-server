package es.prodevelop.pui9.services.exceptions;

import org.springframework.http.HttpStatus;

public class PuiServiceAuthenticate2faWrongCodeException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 223;

	public PuiServiceAuthenticate2faWrongCodeException() {
		super(CODE);
		setShouldLog(false);
		setStatusResponse(HttpStatus.UNAUTHORIZED.value());
	}

}
