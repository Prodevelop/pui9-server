package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when an attribute value is null and the column in database is
 * set as not null
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoNullParametersException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 110;

	public PuiDaoNullParametersException(String parameter) {
		super(CODE, parameter);
		setStatusResponse(422); // HttpStatus.UNPROCESSABLE_ENTITY.value();
	}

}
