package es.prodevelop.pui9.services.exceptions;

public class PuiServiceToJsonException extends AbstractPuiServicesException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 802;

	public PuiServiceToJsonException(String error) {
		super(CODE, error);
	}

}
