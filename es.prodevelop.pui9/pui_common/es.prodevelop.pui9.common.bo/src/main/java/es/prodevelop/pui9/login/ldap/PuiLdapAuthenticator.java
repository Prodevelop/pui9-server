package es.prodevelop.pui9.login.ldap;

import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.stereotype.Component;

@Component
public class PuiLdapAuthenticator extends BindAuthenticator {

	public PuiLdapAuthenticator(PuiLdapSpringSecurityContextSource contextSource) {
		super(contextSource);
	}

	@Override
	public void afterPropertiesSet() {
		setUserSearch(createUserSearch());
		super.afterPropertiesSet();
	}

	private LdapUserSearch createUserSearch() {
		return new FilterBasedLdapUserSearch("", "userPrincipalName={0}", getContextSource());
	}

	@Override
	public PuiLdapSpringSecurityContextSource getContextSource() {
		return (PuiLdapSpringSecurityContextSource) super.getContextSource();
	}

}
