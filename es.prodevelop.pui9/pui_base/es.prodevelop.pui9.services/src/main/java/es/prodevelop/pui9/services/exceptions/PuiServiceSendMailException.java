package es.prodevelop.pui9.services.exceptions;

public class PuiServiceSendMailException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 803;

	public PuiServiceSendMailException(String message) {
		super(CODE, message);
	}

}
