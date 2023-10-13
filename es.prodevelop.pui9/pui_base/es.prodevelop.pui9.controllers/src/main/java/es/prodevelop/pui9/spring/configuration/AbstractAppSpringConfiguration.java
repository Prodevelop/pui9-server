package es.prodevelop.pui9.spring.configuration;

import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PreDestroy;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jndi.JndiTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import es.prodevelop.pui9.controller.AbstractPuiController;
import es.prodevelop.pui9.interceptors.PuiInterceptor;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * This is an abstract Application Configuration for Spring that all the PUI
 * applications should implement. Basically need to provide implementation for
 * the kind of interceptor to be used. You can define in the concrete class more
 * dataSources if needed.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiSpringConfiguration
public abstract class AbstractAppSpringConfiguration {

	private List<Class<? extends AbstractPuiController>> overridedPuiControllers;

	protected AbstractAppSpringConfiguration() {
		setServerTimeZone();
	}

	protected void setServerTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone(PuiDateUtil.utcZone));
	}

	/**
	 * Don't override thid method. Use {@link #getHandlerInterceptor()}
	 */
	@Bean
	public HandlerInterceptor actionInterceptor() {
		return getHandlerInterceptor();
	}

	/**
	 * Don't override thid method. Use {@link #createDataSource()}
	 * 
	 * @return The main DataSource of the application
	 * @throws NamingException If the JNDI is not found
	 */
	@Bean
	public DataSource dataSource() throws NamingException {
		return createDataSource();
	}

	/**
	 * Maybe you want to override
	 * {@link AbstractAppSpringConfiguration#getJndiName()} method
	 * 
	 * @return The main DataSource of the application
	 * @throws NamingException If the JNDI is not found
	 */
	protected DataSource createDataSource() throws NamingException {
		try {
			String jndiName = getJndiName();
			if (jndiName != null) {
				JndiTemplate jndi = new JndiTemplate();
				return (DataSource) jndi.lookup(jndiName);
			} else {
				return null;
			}
		} catch (NamingException e) {
			return null;
		}
	}

	/**
	 * Don't override thid method. Use {@link #getAesSecret()}
	 */
	@Bean
	public String aesSecret() {
		String aesSecret = getAesSecret();
		if (aesSecret.length() != 32) {
			throw new IllegalArgumentException("AES secret should be of 256 bits, 32 characters string length");
		}
		return aesSecret;
	}

	/**
	 * Get a list with the overrided PUI controllers. Don't override thid method.
	 * Use {@link #fillOverridedPuiControllers()}
	 */
	public List<Class<? extends AbstractPuiController>> getOverridedPuiControllers() {
		if (overridedPuiControllers == null) {
			overridedPuiControllers = fillOverridedPuiControllers();
		}
		return overridedPuiControllers;
	}

	/**
	 * Provide the implementation class for the {@link HandlerInterceptor} object to
	 * use
	 * 
	 * @return The interceptor to be used
	 */
	protected PuiInterceptor getHandlerInterceptor() {
		return new PuiInterceptor();
	}

	/**
	 * The name of the JNDI for the datasource
	 * 
	 * @return the JNDI name
	 */
	protected abstract String getJndiName();

	/**
	 * A secret for the application. It is used to encrypt and decrypt the passwords
	 * of the application. Should never change, and if you dit it, you should
	 * encrypt again all the literals that you have encrypted with the old
	 * secret.<br>
	 * <br>
	 * The secret should be of 256 bits, so an string of 32 characters of length is
	 * required.
	 * 
	 * @return
	 */
	protected abstract String getAesSecret();

	/**
	 * A list with the overrided PUI controllers. An overrided controller, should be
	 * declared with {@link org.springframework.context.annotation.Primary}
	 * annotation
	 * 
	 * @return
	 */
	protected List<Class<? extends AbstractPuiController>> fillOverridedPuiControllers() {
		return Collections.emptyList();
	}

	@PreDestroy
	private void preDestroy() {
		PuiBackgroundExecutors.getSingleton().destroy();
	}

}
