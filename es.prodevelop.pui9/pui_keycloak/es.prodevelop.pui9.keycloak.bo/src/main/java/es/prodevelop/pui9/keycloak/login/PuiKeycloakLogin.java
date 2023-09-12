package es.prodevelop.pui9.keycloak.login;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.keycloak.dto.KeycloakCredentials;
import es.prodevelop.pui9.keycloak.dto.KeycloakLoginData;
import es.prodevelop.pui9.keycloak.dto.KeycloakUserInfo;
import es.prodevelop.pui9.login.LoginData;
import es.prodevelop.pui9.login.PuiLogin;
import es.prodevelop.pui9.login.PuiUserInfo;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectLoginException;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectUserPasswordException;
import es.prodevelop.pui9.services.exceptions.PuiServiceLoginMaxAttemptsException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserCredentialsExpiredException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserDisabledException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserLockedException;
import es.prodevelop.pui9.utils.PuiConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class PuiKeycloakLogin extends PuiLogin {

	@Autowired
	private IPuiUserService userService;

	/**
	 * Perform a login into the application using the given Keycloak JWT token
	 * 
	 * @param keycloakLoginData The necessary data to perform a login from Keycloak
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
	public PuiUserInfo loginUser(KeycloakLoginData keycloakLoginData) throws PuiServiceIncorrectUserPasswordException,
			PuiServiceIncorrectLoginException, PuiServiceUserDisabledException, PuiServiceLoginMaxAttemptsException,
			PuiServiceUserCredentialsExpiredException, PuiServiceUserLockedException {
		String jwt = keycloakLoginData.getJwt().replace(PuiConstants.BEARER_PREFIX, "");
		String user = getUserFromKeycloakJwt(jwt);
		keycloakLoginData.withUsr(user);

		return super.loginUser(keycloakLoginData);
	}

	@Override
	protected Authentication createAuthentication(LoginData loginData) {
		return new PuiKeycloakAuthenticationToken(loginData.getUsr());
	}

	/**
	 * Check if the given credentials are valid or not, and returns some user info
	 * in case are valid
	 * 
	 * @param keycloakCredentials The credentials to check
	 * @return The user info
	 * @throws PuiServiceIncorrectUserPasswordException  If the user or password are
	 *                                                   wrong
	 * @throws PuiServiceUserDisabledException           If the user is disabled on
	 *                                                   the database
	 * @throws PuiServiceUserCredentialsExpiredException If the user credentials
	 *                                                   expired
	 * @throws PuiServiceIncorrectLoginException         If an error while loging
	 *                                                   occurs
	 */
	public KeycloakUserInfo checkCredentials(KeycloakCredentials keycloakCredentials)
			throws PuiServiceIncorrectUserPasswordException, PuiServiceUserDisabledException,
			PuiServiceUserCredentialsExpiredException, PuiServiceIncorrectLoginException {
		LoginData loginData = new LoginData().withUsr(keycloakCredentials.getUsr())
				.withPassword(keycloakCredentials.getPassword());
		Authentication auth;

		try {
			auth = authenticationManager.authenticate(super.createAuthentication(loginData));
		} catch (UsernameNotFoundException | BadCredentialsException e) {
			throw new PuiServiceIncorrectUserPasswordException();
		} catch (DisabledException e) {
			throw new PuiServiceUserDisabledException(keycloakCredentials.getUsr());
		} catch (CredentialsExpiredException e) {
			throw new PuiServiceUserCredentialsExpiredException();
		} catch (AuthenticationException e) {
			throw new PuiServiceIncorrectLoginException(e);
		}

		PuiUserSession userSession = (PuiUserSession) auth.getPrincipal();

		return KeycloakUserInfo.builder().withUser(userSession.getUsr()).withFirstname(userSession.getName())
				.withLastname(userSession.getName()).withEmail(userSession.getEmail());
	}

	/**
	 * Get the user info from the given user name
	 * 
	 * @param user The user name
	 * @return The user info
	 * @throws PuiCommonUserNotExistsException If the user doesn't exist
	 */
	public KeycloakUserInfo getUser(String user) throws PuiCommonUserNotExistsException {
		IPuiUser puiUser = userService.getUserLite(user);

		return KeycloakUserInfo.builder().withUser(puiUser.getUsr()).withFirstname(puiUser.getName())
				.withLastname(puiUser.getName()).withEmail(puiUser.getEmail());
	}

	/**
	 * Get the list of users info that belongs to the given page and rows pagination
	 * 
	 * @param page The page of the pagination
	 * @param rows The rows of the pagination
	 * @return The list of users info
	 */
	public List<KeycloakUserInfo> getUsers(Integer page, Integer rows) {
		SearchRequest request = new SearchRequest();
		request.setPage(page);
		request.setRows(rows);

		SearchResponse<IPuiUser> response;
		try {
			response = userService.getTableDao().findPaginated(request);
		} catch (PuiDaoListException e) {
			response = new SearchResponse<>();
		}

		return response
				.getData().stream().map(pu -> KeycloakUserInfo.builder().withUser(pu.getUsr())
						.withFirstname(pu.getName()).withLastname(pu.getName()).withEmail(pu.getEmail()))
				.collect(Collectors.toList());
	}

	private String getUserFromKeycloakJwt(String jwt) throws PuiServiceIncorrectLoginException {
		String keycloakPublicKey = variableService.getVariable(PuiVariableValues.SESSION_KEYCLOAK_PUBLIC.name());
		Jws<Claims> jwsClaims;
		try {
			byte[] publicKeyDecoded = Base64.getDecoder().decode(keycloakPublicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyDecoded);
			KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
			PublicKey publicKey = rsaKeyFac.generatePublic(keySpec);

			jwsClaims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(jwt);
		} catch (JwtException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new PuiServiceIncorrectLoginException();
		}

		return jwsClaims.getBody().get("preferred_username", String.class);
	}

}
