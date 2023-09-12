package es.prodevelop.pui9.login.ldap;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PuiLdapAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;

	public PuiLdapAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

}
