package es.prodevelop.pui9.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import es.prodevelop.pui9.spring.configuration.PuiRootSpringConfiguration;
import es.prodevelop.pui9.spring.configuration.PuiWebApplicationSpringConfiguration;

/**
 * Configure a Test class to be used on PUI9 applications. The tests are
 * executed according the {@link Order} annotation
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = {
		PuiWebApplicationSpringConfiguration.class, PuiRootSpringConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class AbstractPuiTest {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * This method is executed before all the tests of this class
	 */
	@BeforeAll
	private void beforeAll() {
		doBeforeAll();
	}

	protected void doBeforeAll() {
		// do nothing
	}

	/**
	 * This method is executed before each the tests of this class
	 */
	@BeforeEach
	private void beforeEach() {
		doBeforeEach();
	}

	protected void doBeforeEach() {
		// do nothing
	}

	/**
	 * This method is executed after each the tests of this class
	 */
	@AfterEach
	private void afterEach() {
		doAfterEach();
	}

	protected void doAfterEach() {
		// do nothing
	}

	/**
	 * This method is executed after all the tests of this class
	 */
	@AfterAll
	private void afterAll() {
		doAfterAll();
	}

	protected void doAfterAll() {
		// do nothing
	}

}
