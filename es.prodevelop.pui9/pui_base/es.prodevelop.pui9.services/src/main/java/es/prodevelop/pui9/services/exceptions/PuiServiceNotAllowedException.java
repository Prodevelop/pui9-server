package es.prodevelop.pui9.services.exceptions;

import org.springframework.http.HttpStatus;

public class PuiServiceNotAllowedException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 806;

	public PuiServiceNotAllowedException() {
		super(CODE);
		setShouldLog(false);
		setStatusResponse(HttpStatus.FORBIDDEN.value());
	}

}
