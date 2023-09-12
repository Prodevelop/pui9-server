package es.prodevelop.pui9.documents.exceptions;

import org.springframework.http.HttpStatus;

public class PuiDocumentsThumbnailException extends AbstractPuiDocumentsException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 403;

	public PuiDocumentsThumbnailException() {
		super(CODE);
		setStatusResponse(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

}
