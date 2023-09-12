package es.prodevelop.pui9.common.exceptions;

public class PuiCommonUserEmailDuplicatedException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 222;

	public PuiCommonUserEmailDuplicatedException(String email) {
		super(CODE, email);
	}

}
