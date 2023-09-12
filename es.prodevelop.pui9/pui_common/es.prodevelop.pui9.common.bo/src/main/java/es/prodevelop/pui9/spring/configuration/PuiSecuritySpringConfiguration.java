package es.prodevelop.pui9.spring.configuration;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;

@EnableWebSecurity
@PuiSpringConfiguration
public class PuiSecuritySpringConfiguration {

	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	@Autowired(required = false)
	private List<AuthenticationProvider> authenticationProviders;

	@PostConstruct
	private void postConstruct() {
		if (!ObjectUtils.isEmpty(authenticationProviders)) {
			authenticationProviders.forEach(authenticationManagerBuilder::authenticationProvider);
		}
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
