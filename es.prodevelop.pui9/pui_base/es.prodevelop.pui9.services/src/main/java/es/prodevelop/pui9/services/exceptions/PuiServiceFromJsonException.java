package es.prodevelop.pui9.services.exceptions;

public class PuiServiceFromJsonException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 801;

	public PuiServiceFromJsonException(String error) {
		super(CODE, error);
	}

}
