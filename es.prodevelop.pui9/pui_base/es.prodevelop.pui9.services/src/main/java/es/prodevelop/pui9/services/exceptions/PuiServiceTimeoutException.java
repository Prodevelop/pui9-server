package es.prodevelop.pui9.services.exceptions;

public class PuiServiceTimeoutException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 805;

	public PuiServiceTimeoutException(Integer time) {
		super(CODE, time.toString());
	}

}
