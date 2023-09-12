package es.prodevelop.pui9.exceptions;

public class PuiServiceNewException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 808;

	public PuiServiceNewException(Exception cause) {
		super(cause);
	}

}
