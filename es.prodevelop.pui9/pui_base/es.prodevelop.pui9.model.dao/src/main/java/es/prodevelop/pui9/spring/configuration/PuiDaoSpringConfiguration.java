package es.prodevelop.pui9.spring.configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.framework.ProxyProcessorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.pui9.PuiTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import es.prodevelop.pui9.classpath.PuiClassLoaderUtils;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;

/**
 * Spring configuration for the DAO layer. JDBC approach should implement needed
 * methods
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiSpringConfiguration
@EnableTransactionManagement
public class PuiDaoSpringConfiguration {

	@Autowired(required = false)
	@Qualifier("dataSource")
	protected DataSource dataSource;

	@Autowired
	@Qualifier(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME)
	private ProxyProcessorSupport proxySupport;

	@PostConstruct
	private void postConstruct() {
		proxySupport.setBeanClassLoader(PuiClassLoaderUtils.getClassLoader());
	}

	/**
	 * Create a bean for the "transactionManager" object
	 * 
	 * @return The transaction manager object
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		if (dataSource != null) {
			return new PuiTransactionManager(dataSource);
		} else {
			return null;
		}
	}

}
