package es.prodevelop.pui9.spring.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import es.prodevelop.pui9.interceptors.PuiInterceptor;
import es.prodevelop.pui9.spring.configuration.AbstractAppSpringConfiguration;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;
import es.prodevelop.pui9.utils.PuiPropertiesManager;

/**
 * This class is used to configure the application. It is configured the
 * database connection and the handler interceptor (not really used because here
 * we are testing the service layer, not the API). If you want to create a more
 * specific configuration class, extends this one and include a {@link Primary}
 * annotation. Remember to copy the "credentials.properties.template" file
 * (rename it to "credentials.properties") and the "log4j.xml" file to your
 * "/src/test/resources" folder
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiSpringConfiguration
@ComponentScan(basePackages = { "es.prodevelop" })
public class Pui9TestSpringConfiguration extends AbstractAppSpringConfiguration {

	private static final String CREDENTIALS_PROPERTIES_FILENAME = "credentials.properties";
	private static final String DRIVER_CLASS_NAME_PROP = "driverClassName";
	private static final String URL_PROP = "url";
	private static final String USERNAME_PROP = "username";
	private static final String PASSWORD_PROP = "password";
	private static final String AES_SECRET_PROP = "aesSecret";

	private static final Integer maxTotal = 100;
	private static final Integer maxIdle = 2;
	private static final Integer maxWaitMillis = 10000;

	protected Properties properties;

	@PostConstruct
	private void postConstruct() throws IOException {
		properties = PuiPropertiesManager.loadPropertiesFile(CREDENTIALS_PROPERTIES_FILENAME);
	}

	@Override
	protected final DataSource createDataSource() throws NamingException {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(getDriverClassName());
		ds.setUrl(getUrl());
		ds.setUsername(getUsername());
		ds.setPassword(getPassword());
		Properties props = new Properties();
		props.put("maxTotal", getMaxTotal());
		props.put("maxIdle", getMaxIdle());
		props.put("maxWaitMillis", getMaxWaitMillis());
		ds.setConnectionProperties(props);
		return ds;
	}

	protected String getDriverClassName() {
		return (String) properties.get(DRIVER_CLASS_NAME_PROP);
	}

	protected String getUrl() {
		return (String) properties.get(URL_PROP);
	}

	protected String getUsername() {
		return (String) properties.get(USERNAME_PROP);
	}

	protected String getPassword() {
		return (String) properties.get(PASSWORD_PROP);
	}

	@Override
	protected String getAesSecret() {
		return (String) properties.get(AES_SECRET_PROP);
	}

	protected Integer getMaxTotal() {
		return maxTotal;
	}

	protected Integer getMaxIdle() {
		return maxIdle;
	}

	protected Integer getMaxWaitMillis() {
		return maxWaitMillis;
	}

	@Override
	protected String getJndiName() {
		return "";
	}

	@Override
	protected PuiInterceptor getHandlerInterceptor() {
		return new PuiInterceptor();
	}

}
