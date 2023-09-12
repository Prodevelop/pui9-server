package es.prodevelop.pui9.documents.exceptions;

import org.springframework.http.HttpStatus;

public class PuiDocumentsUploadException extends AbstractPuiDocumentsException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 404;

	public PuiDocumentsUploadException() {
		super(CODE);
		setStatusResponse(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

	public PuiDocumentsUploadException(Exception cause) {
		super(cause, CODE);
		setStatusResponse(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

}
