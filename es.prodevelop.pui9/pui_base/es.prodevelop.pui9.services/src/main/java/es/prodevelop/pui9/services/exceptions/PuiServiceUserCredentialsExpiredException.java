package es.prodevelop.pui9.services.exceptions;

import org.springframework.http.HttpStatus;

public class PuiServiceUserCredentialsExpiredException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 226;

	public PuiServiceUserCredentialsExpiredException() {
		super(CODE);
		setShouldLog(false);
		setStatusResponse(HttpStatus.UNAUTHORIZED.value());
	}

}
