package es.prodevelop.pui9.login.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dto.PuiUserPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.login.PuiPasswordEncoders;
import es.prodevelop.pui9.login.PuiUserDetailsService;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.services.exceptions.PuiServiceLoginMaxAttemptsException;

@Component
public class PuiDatabaseAuthenticationProvider extends DaoAuthenticationProvider {

	@Autowired
	private PuiUserDetailsService userDetailsService;

	@Autowired
	private IPuiUserService puiUserService;

	@Override
	protected void doAfterPropertiesSet() {
		setUserDetailsService(userDetailsService);
		setPasswordEncoder(PuiPasswordEncoders.bCryptPasswordEncoder);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(PuiDatabaseAuthenticationToken.class);
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		try {
			super.additionalAuthenticationChecks(userDetails, authentication);
		} catch (AuthenticationException e) {
			boolean raiseMaxAttempts = puiUserService.setWrongLogin(new PuiUserPk(userDetails.getUsername()));
			if (raiseMaxAttempts) {
				e.addSuppressed(new PuiServiceLoginMaxAttemptsException(userDetails.getUsername()));
			}
			throw e;
		}
	}

	@Override
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
			UserDetails user) {
		userDetailsService.fillAuthorities((PuiUserSession) user);
		return super.createSuccessAuthentication(principal, authentication, user);
	}

}
