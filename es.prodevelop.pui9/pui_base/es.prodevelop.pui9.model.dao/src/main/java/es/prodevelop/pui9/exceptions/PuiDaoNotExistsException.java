package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing a Find operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoNotExistsException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 112;

	public PuiDaoNotExistsException(String data) {
		super(CODE, data);
		setStatusResponse(404); // HttpStatus.NOT_FOUND.value();
	}

}
