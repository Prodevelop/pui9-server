package es.prodevelop.pui9.services.exceptions;

public class PuiServiceConcurrencyException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 807;

	public PuiServiceConcurrencyException() {
		super(CODE);
	}

}
