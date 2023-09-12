package es.prodevelop.pui9.exceptions;

/**
 * DAO exception when executing a Save operation (insert or delete) over the
 * database
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiDaoSaveException extends AbstractPuiDaoException {

	private static final long serialVersionUID = 1L;

	protected PuiDaoSaveException(AbstractPuiDaoException cause) {
		super(cause);
	}

	protected PuiDaoSaveException(Exception cause, int code) {
		super(cause, code);
	}

}
