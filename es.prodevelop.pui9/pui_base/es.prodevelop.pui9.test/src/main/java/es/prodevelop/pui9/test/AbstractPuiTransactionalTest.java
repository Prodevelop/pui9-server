package es.prodevelop.pui9.test;

import org.junit.jupiter.api.Order;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 * Configure a Test class to be used on PUI9 applications. The tests are
 * executed according the {@link Order} annotation.<br>
 * <br>
 * 
 * This abstract test class is algo {@link Transactional}, what means that each
 * test (each method) is envolved by a transaction that rollbacks the changes on
 * database after execution
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Transactional
public class AbstractPuiTransactionalTest extends AbstractPuiTest {

	/**
	 * This method is executed before a transaction is created
	 */
	@BeforeTransaction
	private void beforeTransaction() {
		doBeforeTransaction();
	}

	protected void doBeforeTransaction() {
		// do nothing
	}

	/**
	 * This method is executed after a transaction is ended
	 */
	@AfterTransaction
	private void afterTransaction() {
		doAfterTransaction();
	}

	protected void doAfterTransaction() {
		// do nothing
	}

}
