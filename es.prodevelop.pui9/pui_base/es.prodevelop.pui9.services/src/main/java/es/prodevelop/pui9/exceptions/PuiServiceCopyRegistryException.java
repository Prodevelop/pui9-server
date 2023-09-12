package es.prodevelop.pui9.exceptions;

public class PuiServiceCopyRegistryException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 814;

	public PuiServiceCopyRegistryException(PuiServiceException cause) {
		super(cause, cause != null ? cause.getMessage() : null);
	}

}
