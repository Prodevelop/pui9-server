package es.prodevelop.pui9.docgen.exceptions;

import org.springframework.http.HttpStatus;

public class PuiDocgenNoElementsException extends AbstractPuiDocgenException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 501;

	public PuiDocgenNoElementsException() {
		super(CODE);
		setStatusResponse(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

}
