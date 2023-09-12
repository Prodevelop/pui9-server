package es.prodevelop.pui9.exceptions;

public class PuiServiceInsertException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 811;

	public PuiServiceInsertException(PuiServiceException cause) {
		super(cause, cause != null ? cause.getMessage() : null);
	}

	public PuiServiceInsertException(AbstractPuiDaoException cause) {
		super(cause);
	}

}
