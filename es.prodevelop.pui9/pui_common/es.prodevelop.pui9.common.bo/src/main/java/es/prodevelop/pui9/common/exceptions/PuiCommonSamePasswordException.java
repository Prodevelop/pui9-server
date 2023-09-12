package es.prodevelop.pui9.common.exceptions;

import org.springframework.http.HttpStatus;

public class PuiCommonSamePasswordException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 208;

	public PuiCommonSamePasswordException() {
		super(CODE);
		setShouldLog(false);
		setStatusResponse(HttpStatus.UNAUTHORIZED.value());
	}

}
