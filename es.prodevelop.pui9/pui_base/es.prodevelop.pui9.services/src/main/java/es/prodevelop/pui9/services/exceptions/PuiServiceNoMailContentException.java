package es.prodevelop.pui9.services.exceptions;

public class PuiServiceNoMailContentException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 815;

	public PuiServiceNoMailContentException() {
		super(CODE);
	}

}
