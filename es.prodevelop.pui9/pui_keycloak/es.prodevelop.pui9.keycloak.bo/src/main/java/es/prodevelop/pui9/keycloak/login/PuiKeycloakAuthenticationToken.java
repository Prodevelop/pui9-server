package es.prodevelop.pui9.keycloak.login;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PuiKeycloakAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;
	private static final String NO_PASSWORD = "NO_PASSWORD";

	public PuiKeycloakAuthenticationToken(Object principal) {
		super(principal, NO_PASSWORD);
	}

}
