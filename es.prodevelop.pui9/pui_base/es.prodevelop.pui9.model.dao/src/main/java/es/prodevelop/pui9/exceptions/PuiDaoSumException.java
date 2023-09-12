package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing the Sum operation over the database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoSumException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 113;

	public PuiDaoSumException(Exception cause) {
		super(cause, CODE);
	}

}
