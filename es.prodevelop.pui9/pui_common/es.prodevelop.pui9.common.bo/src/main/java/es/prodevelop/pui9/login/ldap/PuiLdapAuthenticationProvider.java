package es.prodevelop.pui9.login.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.model.dto.PuiUserPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.login.PuiUserDetailsService;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.login.database.PuiDatabaseAuthenticationProvider;

@Component
public class PuiLdapAuthenticationProvider extends LdapAuthenticationProvider {

	@Autowired
	protected IPuiVariableService variableService;

	@Autowired
	protected IPuiUserService puiUserService;

	@Autowired
	private PuiUserDetailsService userDetailService;

	@Autowired
	private PuiDatabaseAuthenticationProvider databaseAuthProvider;

	public PuiLdapAuthenticationProvider(PuiLdapAuthenticator authenticator, PuiLdapAuthoritiesPopulator populator) {
		super(authenticator, populator);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(PuiLdapAuthenticationToken.class);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (userShouldExistInDatabase()) {
			checkUserInDatabase(authentication.getName());
		}

		Authentication auth = null;
		String domain = variableService.getVariable(PuiVariableValues.LDAP_DOMAIN.name());
		if (!ObjectUtils.isEmpty(domain) && !authentication.getName().endsWith(domain)) {
			auth = new UsernamePasswordAuthenticationToken(addDomain(authentication.getName()),
					authentication.getCredentials());
		} else {
			auth = authentication;
		}

		try {
			return super.authenticate(auth);
		} catch (Exception e) {
			if (useDatabaseAuthenticationOnFail()) {
				return databaseAuthProvider.authenticate(authentication);
			} else {
				throw e;
			}
		}
	}

	protected boolean userShouldExistInDatabase() {
		return true;
	}

	protected boolean useDatabaseAuthenticationOnFail() {
		return false;
	}

	protected void checkUserInDatabase(String user) {
		user = removeDomain(user);

		// the user should exist previously in the database. If not, throw an exception
		try {
			if (!puiUserService.getTableDao().exists(new PuiUserPk(user))) {
				throw new BadCredentialsException("User not found: " + user);
			}
		} catch (PuiDaoFindException e) {
			// something went wrong
		}
	}

	@Override
	protected Authentication createSuccessfulAuthentication(UsernamePasswordAuthenticationToken authentication,
			UserDetails user) {
		beforeCreateSuccessfulAuthentication(authentication, user);
		PuiUserSession detail = (PuiUserSession) userDetailService
				.loadUserByUsername(removeDomain(authentication.getName()));
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(detail,
				authentication.getCredentials(), user.getAuthorities());
		userDetailService.fillAuthorities(detail);
		return result;
	}

	protected void beforeCreateSuccessfulAuthentication(UsernamePasswordAuthenticationToken authentication,
			UserDetails user) {
		// do nothing
	}

	protected String addDomain(String usr) {
		String domain = variableService.getVariable(PuiVariableValues.LDAP_DOMAIN.name());

		if (!ObjectUtils.isEmpty(domain) && !usr.endsWith(domain)) {
			usr = usr + "@" + domain;
		}

		return usr;
	}

	protected String removeDomain(String usr) {
		String domain = variableService.getVariable(PuiVariableValues.LDAP_DOMAIN.name());

		if (!ObjectUtils.isEmpty(domain) && usr.endsWith(domain)) {
			usr = usr.substring(0, usr.indexOf('@'));
		}

		return usr;
	}

}
