package es.prodevelop.pui9.documents.exceptions;

import org.springframework.http.HttpStatus;

public class PuiDocumentsFileSizeException extends AbstractPuiDocumentsException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 402;

	public PuiDocumentsFileSizeException() {
		super(CODE);
		setStatusResponse(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

}
