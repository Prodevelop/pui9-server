package es.prodevelop.pui9.keycloak.provider;

import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.keycloak.login.PuiKeycloakAuthenticationToken;
import es.prodevelop.pui9.login.database.PuiDatabaseAuthenticationProvider;

/**
 * Provider for Keycloak Authentication
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Primary
@Component
public class PuiKeycloakAuthenticationProvider extends PuiDatabaseAuthenticationProvider {

	@Override
	protected void doAfterPropertiesSet() {
		super.doAfterPropertiesSet();
		setPasswordEncoder(new PasswordEncoder() {
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return true;
			}

			@Override
			public String encode(CharSequence rawPassword) {
				return String.valueOf(rawPassword);
			}
		});
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(PuiKeycloakAuthenticationToken.class);
	}

}
