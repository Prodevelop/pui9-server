package es.prodevelop.pui9.login.ldap;

import org.springframework.context.annotation.Primary;
import org.springframework.ldap.core.ContextSource;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

/**
 * If you want to extend it, simple do it and mark the class as
 * {@link Component} and {@link Primary} annotations
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiLdapAuthoritiesPopulator extends DefaultLdapAuthoritiesPopulator {

	public PuiLdapAuthoritiesPopulator(ContextSource contextSource) {
		super(contextSource, null);
	}

}
