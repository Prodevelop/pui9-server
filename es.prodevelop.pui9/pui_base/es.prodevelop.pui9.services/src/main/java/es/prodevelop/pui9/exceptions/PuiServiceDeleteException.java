package es.prodevelop.pui9.exceptions;

public class PuiServiceDeleteException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 813;

	public PuiServiceDeleteException(PuiServiceException cause) {
		super(cause, cause != null ? cause.getMessage() : null);
	}

	public PuiServiceDeleteException(AbstractPuiDaoException cause) {
		super(cause);
	}

}
