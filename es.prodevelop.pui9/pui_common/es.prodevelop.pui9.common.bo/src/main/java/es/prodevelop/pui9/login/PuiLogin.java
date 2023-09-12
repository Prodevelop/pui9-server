package es.prodevelop.pui9.login;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.util.Utils;
import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.eventlistener.PuiEventLauncher;
import es.prodevelop.pui9.eventlistener.event.LoginEvent;
import es.prodevelop.pui9.eventlistener.event.LogoutEvent;
import es.prodevelop.pui9.eventlistener.event.SessionCreatedEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.login.database.PuiDatabaseAuthenticationToken;
import es.prodevelop.pui9.login.ldap.PuiLdapAuthenticationToken;
import es.prodevelop.pui9.services.exceptions.PuiServiceAuthenticate2faMaxWrongCodeException;
import es.prodevelop.pui9.services.exceptions.PuiServiceAuthenticate2faWrongCodeException;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectLoginException;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectUserPasswordException;
import es.prodevelop.pui9.services.exceptions.PuiServiceLoginMaxAttemptsException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoSessionException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserCredentialsExpiredException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserDisabledException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserLockedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserNotAuthenticatedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserSessionTimeoutException;
import es.prodevelop.pui9.session.PuiSessionHandler;
import es.prodevelop.pui9.utils.PuiConstants;

/**
 * This component allows to manage the sessions in the application. It allows to
 * make the login/logout of a user and check the provided session. It uses the
 * standard JWT to generate the session tokens, and stores in it some necessary
 * information about the logged user
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiLogin {

	@Autowired
	protected AuthenticationManager authenticationManager;

	@Autowired
	private PuiEventLauncher eventLauncher;

	@Autowired
	private PuiSessionHandler sessionHandler;

	@Autowired
	protected IPuiVariableService variableService;

	/**
	 * Perform the login of the given user into the application. The login is
	 * created by Spring Security
	 * 
	 * @param loginData The necessary data to perform a login
	 * @return The session user info
	 * @throws PuiServiceIncorrectUserPasswordException  If the user or password are
	 *                                                   wrong
	 * @throws PuiServiceIncorrectLoginException         If an error while loging
	 *                                                   occurs
	 * @throws PuiServiceUserDisabledException           If the user is disabled on
	 *                                                   the database
	 * @throws PuiServiceLoginMaxAttemptsException       If the user reach the
	 *                                                   maximum login attempts
	 * @throws PuiServiceUserCredentialsExpiredException If the user credentials
	 *                                                   expired
	 * @throws PuiServiceUserLockedException             If the user is locked by
	 *                                                   any reason
	 */
	public PuiUserInfo loginUser(LoginData loginData) throws PuiServiceIncorrectUserPasswordException,
			PuiServiceIncorrectLoginException, PuiServiceUserDisabledException, PuiServiceLoginMaxAttemptsException,
			PuiServiceUserCredentialsExpiredException, PuiServiceUserLockedException {
		if (ObjectUtils.isEmpty(loginData.getUsr())) {
			throw new PuiServiceIncorrectUserPasswordException();
		}

		Authentication auth = authenticate(loginData);
		return createUserInfo(loginData, auth);
	}

	/**
	 * Logout the user identified by the given jwt
	 * 
	 * @param jwt                      The authorization string (the JWT token)
	 * @param removeSessionFromContext true/false if the jwt session should be
	 *                                 removed from current context
	 * @return The session of the user, or null if no session found
	 * @throws PuiServiceNoSessionException If no authorization string is provided
	 */
	public PuiUserSession logoutUser(String jwt, boolean removeSessionFromContext) throws PuiServiceNoSessionException {
		if (ObjectUtils.isEmpty(jwt)) {
			throw new PuiServiceNoSessionException();
		}

		jwt = jwt.replace(PuiConstants.BEARER_PREFIX, "");
		Authentication auth = sessionHandler.remove(jwt);

		if (auth != null) {
			PuiUserSession userSession = (PuiUserSession) auth.getPrincipal();
			eventLauncher.fireSync(new LogoutEvent(LoginEventData.success(userSession)));

			if (removeSessionFromContext) {
				sessionHandler.removeContextSession();
			}
			return (PuiUserSession) auth.getPrincipal();
		} else {
			return null;
		}
	}

	/**
	 * Generates the QR for 2FA authentication for the current authenticated user
	 * 
	 * @param jwt The jwt of the session to generate the QR for 2FA
	 * @throws PuiServiceNoSessionException          If no authorization string is
	 *                                               provided
	 * @throws PuiServiceUserSessionTimeoutException If the given authorization
	 *                                               token is expired
	 * 
	 * @return An object that represents the QR information
	 */
	public TwoFactorAuthenticationData generateQr2fa(String jwt)
			throws PuiServiceNoSessionException, PuiServiceUserSessionTimeoutException {
		jwt = jwt.replace(PuiConstants.BEARER_PREFIX, "");

		Authentication auth = sessionHandler.get(jwt);

		PuiUserSession pus = (PuiUserSession) auth.getPrincipal();
		if (!pus.isUse2fa()) {
			return null;
		}

		String appname = variableService.getVariable(PuiVariableValues.APPLICATION_NAME.name());

		String label = pus.getEmail();
		if (ObjectUtils.isEmpty(label)) {
			label = pus.getUsername();
		}
		QrData data = new QrData.Builder().label(label).secret(pus.getSecret2fa()).issuer(appname).build();

		TwoFactorAuthenticationData twoFA = new TwoFactorAuthenticationData();
		twoFA.setLabel(label);
		twoFA.setIssuer(appname);
		twoFA.setSecret(pus.getSecret2fa());
		twoFA.setOtpAuthUri(data.getUri());

		Integer qrSize = 350;

		{
			// set QR as png/base64 image
			ZxingPngQrGenerator generator = new ZxingPngQrGenerator();
			generator.setImageSize(qrSize);
			byte[] imageData;
			try {
				imageData = generator.generate(data);
			} catch (QrGenerationException e) {
				imageData = new byte[0];
			}
			String mimeType = generator.getImageMimeType();

			String qrImageUri = Utils.getDataUriForImage(imageData, mimeType);
			twoFA.setQrImageUri(qrImageUri);
		}

		{
			// set a URL to generate the QR
			String uri;
			try {
				uri = URLEncoder.encode(data.getUri(), StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				uri = "";
			}
			String genUri = "https://quickchart.io/qr?ecLevel=L&size=" + qrSize + "&text=" + uri;
			twoFA.setGeneratorUri(genUri);
		}

		return twoFA;
	}

	/**
	 * Complete the authentication of the current authenticated user with the code
	 * provided by the 2FA
	 * 
	 * @param jwt   The jwt of the session to be finally authenticate
	 * @param code  The code provided by the 2FA
	 * @param force To force the authentication even if the code is not valid or
	 *              null
	 * @throws PuiServiceAuthenticate2faWrongCodeException    If the given code is
	 *                                                        not valid
	 * @throws PuiServiceAuthenticate2faMaxWrongCodeException If maximum attempts
	 *                                                        for authentication was
	 *                                                        reached
	 * @throws PuiServiceNoSessionException                   If no authorization
	 *                                                        string is provided
	 * @throws PuiServiceUserSessionTimeoutException          If the given
	 *                                                        authorization token is
	 *                                                        expired
	 */
	public void authenticate2fa(String jwt, String code, boolean force)
			throws PuiServiceAuthenticate2faWrongCodeException, PuiServiceAuthenticate2faMaxWrongCodeException,
			PuiServiceNoSessionException, PuiServiceUserSessionTimeoutException {
		jwt = jwt.replace(PuiConstants.BEARER_PREFIX, "");

		Authentication auth = sessionHandler.get(jwt);

		PuiUserSession pus = (PuiUserSession) auth.getPrincipal();
		if (!pus.isUse2fa() || force) {
			pus.withAuthenticated(true);
			return;
		}

		pus.incrementAttempts2fa();

		CodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), new SystemTimeProvider());

		if (!verifier.isValidCode(pus.getSecret2fa(), code)) {
			Integer maxAttempts = variableService.getVariable(Integer.class,
					PuiVariableValues.LOGIN_MAX_ATTEMPTS.name());
			if (maxAttempts == null) {
				maxAttempts = Integer.MAX_VALUE;
			}
			if (pus.getAttemps2fa() >= maxAttempts) {
				try {
					logoutUser(pus.getJwt(), true);
				} catch (PuiServiceNoSessionException e) {
					// do nothing
				}
				PuiServiceAuthenticate2faMaxWrongCodeException ex = new PuiServiceAuthenticate2faMaxWrongCodeException();
				eventLauncher.fireSync(new LoginEvent(
						LoginEventData.error(pus.getUsr(), pus.getIp(), pus.getClient(), ex.getMessage())));
				throw ex;
			} else {
				throw new PuiServiceAuthenticate2faWrongCodeException();
			}
		} else {
			pus.withAuthenticated(true);
			eventLauncher.fireSync(new LoginEvent(LoginEventData.success(pus)));
		}
	}

	/**
	 * Check if the given jwt is correctly authenticated with 2FA
	 * 
	 * @param jwt The jwt of the session
	 * @throws PuiServiceNoSessionException          If no authorization string is
	 *                                               provided
	 * @throws PuiServiceUserSessionTimeoutException If the given authorization
	 *                                               token is expired
	 * 
	 * @return true is it's completelly authenticated; false if not
	 */
	public boolean is2faAuthenticated(String jwt)
			throws PuiServiceNoSessionException, PuiServiceUserSessionTimeoutException {
		jwt = jwt.replace(PuiConstants.BEARER_PREFIX, "");

		Authentication auth = sessionHandler.get(jwt);

		PuiUserSession pus = (PuiUserSession) auth.getPrincipal();
		return pus.isUse2fa() && pus.isAuthenticated();
	}

	protected Authentication authenticate(LoginData loginData)
			throws PuiServiceIncorrectLoginException, PuiServiceUserDisabledException,
			PuiServiceIncorrectUserPasswordException, PuiServiceLoginMaxAttemptsException,
			PuiServiceUserCredentialsExpiredException, PuiServiceUserLockedException {
		PuiException exception = null;

		try {
			return authenticationManager.authenticate(createAuthentication(loginData));
		} catch (UsernameNotFoundException e) {
			PuiServiceIncorrectUserPasswordException ex = new PuiServiceIncorrectUserPasswordException();
			exception = ex;
			throw ex;
		} catch (DisabledException e) {
			PuiServiceUserDisabledException ex = new PuiServiceUserDisabledException(loginData.getUsr());
			exception = ex;
			throw ex;
		} catch (LockedException e) {
			PuiServiceUserLockedException ex = new PuiServiceUserLockedException();
			exception = ex;
			throw ex;
		} catch (BadCredentialsException e) {
			if (e.getSuppressed().length > 0 && e.getSuppressed()[0] instanceof PuiServiceLoginMaxAttemptsException) {
				exception = (PuiException) e.getSuppressed()[0];
				throw (PuiServiceLoginMaxAttemptsException) exception;
			} else {
				exception = new PuiServiceIncorrectUserPasswordException();
				throw (PuiServiceIncorrectUserPasswordException) exception;
			}
		} catch (CredentialsExpiredException e) {
			PuiServiceUserCredentialsExpiredException ex = new PuiServiceUserCredentialsExpiredException();
			exception = ex;
			throw ex;
		} catch (AuthenticationException e) {
			PuiServiceIncorrectLoginException ex = new PuiServiceIncorrectLoginException(e);
			exception = ex;
			throw ex;
		} finally {
			if (exception != null) {
				eventLauncher.fireAsync(new LoginEvent(LoginEventData.error(loginData.getUsr(), loginData.getIp(),
						loginData.getClient(), exception.getMessage())));
			}
		}
	}

	/**
	 * Depending on the Authentication Token that this method returns, an
	 * Authentication Provider or other is used to authenticate the user
	 * 
	 * @param loginData
	 * @return
	 */
	protected Authentication createAuthentication(LoginData loginData) {
		if (variableService.getVariable(Boolean.class, PuiVariableValues.LDAP_ACTIVE.name()).booleanValue()) {
			return new PuiLdapAuthenticationToken(loginData.getUsr(), loginData.getPassword());
		} else {
			return new PuiDatabaseAuthenticationToken(loginData.getUsr(), loginData.getPassword());
		}
	}

	protected PuiUserInfo createUserInfo(LoginData loginData, Authentication auth) {
		PuiUserSession userSession = (PuiUserSession) auth.getPrincipal();
		fillUserSession(loginData, userSession);
		sessionHandler.buildJwt(userSession);
		sessionHandler.add(userSession.getJwt(), auth);
		try {
			// init the context session after creating it. Maybe only for audit purposes
			sessionHandler.setContextSession(userSession.getJwt());
		} catch (PuiServiceNoSessionException | PuiServiceUserSessionTimeoutException
				| PuiServiceUserNotAuthenticatedException e) {
			// should never occur, because the session is recently created
		}

		eventLauncher.fireSync(new SessionCreatedEvent(userSession, loginData));
		if (!userSession.isUse2fa()) {
			eventLauncher.fireSync(new LoginEvent(LoginEventData.success(userSession)));
		}

		return userSession.asPuiUserInfo();
	}

	private void fillUserSession(LoginData loginData, PuiUserSession userSession) {
		userSession.withIp(loginData.getIp());
		userSession.withUserAgent(loginData.getUserAgent());
		userSession.withZoneId(loginData.getJavaZoneId());
		userSession.withPersistent(loginData.isPersistent());
		userSession.withClient(loginData.getClient());
	}

}
