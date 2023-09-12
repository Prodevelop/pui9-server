package es.prodevelop.pui9.services.exceptions;

public class PuiServiceWrongMailException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 804;

	public PuiServiceWrongMailException(String email) {
		super(CODE, email);
	}

}
