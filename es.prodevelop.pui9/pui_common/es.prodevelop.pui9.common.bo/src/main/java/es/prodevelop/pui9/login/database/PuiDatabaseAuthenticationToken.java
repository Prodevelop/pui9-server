package es.prodevelop.pui9.login.database;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PuiDatabaseAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;

	public PuiDatabaseAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

}
