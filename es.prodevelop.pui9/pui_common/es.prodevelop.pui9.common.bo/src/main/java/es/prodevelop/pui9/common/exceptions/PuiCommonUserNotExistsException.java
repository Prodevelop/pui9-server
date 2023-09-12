package es.prodevelop.pui9.common.exceptions;

import org.springframework.http.HttpStatus;

public class PuiCommonUserNotExistsException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 211;

	public PuiCommonUserNotExistsException(String user) {
		super(CODE, user);
		setShouldLog(false);
		setStatusResponse(HttpStatus.UNAUTHORIZED.value());
	}

}
