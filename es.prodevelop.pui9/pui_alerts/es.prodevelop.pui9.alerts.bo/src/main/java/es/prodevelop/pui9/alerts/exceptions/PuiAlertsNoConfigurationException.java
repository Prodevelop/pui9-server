package es.prodevelop.pui9.alerts.exceptions;

public class PuiAlertsNoConfigurationException extends AbstractPuiAlertsException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 601;

	public PuiAlertsNoConfigurationException() {
		super(CODE);
	}

}
