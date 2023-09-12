package es.prodevelop.pui9.exceptions;

public class PuiServiceGetException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 809;

	public PuiServiceGetException() {
		super(null);
	}

	public PuiServiceGetException(PuiServiceException cause) {
		super(cause, cause != null ? cause.getMessage() : null);
	}

	public PuiServiceGetException(AbstractPuiDaoException cause) {
		super(cause);
	}

	public PuiServiceGetException(Exception cause) {
		super(cause);
	}

}
