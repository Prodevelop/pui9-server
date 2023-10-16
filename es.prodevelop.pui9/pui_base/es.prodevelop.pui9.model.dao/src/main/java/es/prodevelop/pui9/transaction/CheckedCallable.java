package es.prodevelop.pui9.transaction;

import java.util.concurrent.Callable;

/**
 * Add support to a {@link Callable} without a return value
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@FunctionalInterface
public interface CheckedCallable {

	/**
	 * Computes an operation, or throws an exception if unable to do so.
	 *
	 * @throws Exception if unable to compute the operation
	 */
	void call() throws Exception;

}