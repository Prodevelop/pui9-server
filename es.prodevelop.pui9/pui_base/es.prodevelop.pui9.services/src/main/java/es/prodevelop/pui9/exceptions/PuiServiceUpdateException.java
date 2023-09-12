package es.prodevelop.pui9.exceptions;

public class PuiServiceUpdateException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 812;

	public PuiServiceUpdateException(PuiServiceException cause) {
		super(cause, cause != null ? cause.getInternalCode() : null, cause != null ? cause.getMessage() : null);
	}

	public PuiServiceUpdateException(AbstractPuiDaoException cause) {
		super(cause);
	}

	public PuiServiceUpdateException(Exception cause) {
		super(cause);
	}

}
