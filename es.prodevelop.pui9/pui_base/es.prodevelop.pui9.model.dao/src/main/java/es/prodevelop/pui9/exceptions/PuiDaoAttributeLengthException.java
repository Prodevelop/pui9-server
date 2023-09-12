package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when an attribute length provided is greater than the allowed
 * by the column in database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoAttributeLengthException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 101;

	public PuiDaoAttributeLengthException(String fieldName, Integer maxLength, Integer currentLength) {
		super(CODE, fieldName, maxLength, currentLength);
		setStatusResponse(422); // HttpStatus.UNPROCESSABLE_ENTITY.value();
	}

}
