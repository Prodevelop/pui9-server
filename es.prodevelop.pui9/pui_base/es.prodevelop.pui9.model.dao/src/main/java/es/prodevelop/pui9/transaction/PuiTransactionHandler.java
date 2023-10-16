package es.prodevelop.pui9.transaction;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.prodevelop.pui9.exceptions.PuiException;

/**
 * Add support to execute some code within a transaction from any place of your
 * code. Just create the needed function and use this component to run it within
 * a transaction. If a transaction exists, the function is callen within this
 * transaction. If no one exists, a new one is created
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiTransactionHandler {

	/**
	 * Execute a callable that allows to throw an exception and returns nothing
	 * 
	 * @param callable The callable to be executed
	 * @throws PuiException Possible exception thrown
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = PuiException.class)
	public void call(CheckedCallable callable) throws PuiException {
		try {
			callable.call();
		} catch (Exception e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Execute a callable that allows to throw an exception and returns a value
	 * 
	 * @param <T>      The type of the value returned
	 * @param callable The callable to be executed
	 * @return A value computed
	 * @throws PuiException Possible exception thrown
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = PuiException.class)
	public <T> T call(Callable<T> callable) throws PuiException {
		try {
			return callable.call();
		} catch (Exception e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Execute a supplier that returns a value
	 * 
	 * @param <T>      The type of the value returned
	 * @param supplier The supplier to be executed
	 * @return A value computed
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = PuiException.class)
	public <T> T get(Supplier<T> supplier) {
		return supplier.get();
	}

}
