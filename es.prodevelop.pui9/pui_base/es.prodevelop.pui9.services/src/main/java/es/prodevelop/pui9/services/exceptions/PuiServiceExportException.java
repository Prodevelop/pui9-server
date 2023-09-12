package es.prodevelop.pui9.services.exceptions;

public class PuiServiceExportException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 817;

	public PuiServiceExportException(String message) {
		super(CODE, message);
	}

}
