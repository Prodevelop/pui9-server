package es.prodevelop.pui9.keycloak.controllers;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.controller.AbstractPuiController;
import es.prodevelop.pui9.enums.Pui9KnownClients;
import es.prodevelop.pui9.keycloak.dto.KeycloakCredentials;
import es.prodevelop.pui9.keycloak.dto.KeycloakLoginData;
import es.prodevelop.pui9.keycloak.dto.KeycloakUserInfo;
import es.prodevelop.pui9.keycloak.exceptions.PuiKeycloakBadTokenException;
import es.prodevelop.pui9.keycloak.login.PuiKeycloakLogin;
import es.prodevelop.pui9.login.PuiUserInfo;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectLoginException;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectUserPasswordException;
import es.prodevelop.pui9.services.exceptions.PuiServiceLoginMaxAttemptsException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserCredentialsExpiredException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserDisabledException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserLockedException;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiRequestUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

@PuiGenerated
@Controller
@Tag(name = "Keycloak authentication")
@RequestMapping("/keycloak")
public class KeycloakController extends AbstractPuiController {

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private PuiKeycloakLogin keycloackLogin;

	@PuiNoSessionRequired
	@Operation(summary = "Login into the application with a Keycloak JWT", description = "Login into the application using the given Keycloak JWT")
	@GetMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
	public PuiUserInfo keycloakSignin(HttpServletRequest request,
			@Parameter(in = ParameterIn.HEADER) @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
			@Parameter(in = ParameterIn.HEADER, hidden = true) @RequestHeader(value = HttpHeaders.USER_AGENT) String userAgent,
			@Parameter(in = ParameterIn.HEADER, hidden = true) @RequestHeader(value = PuiConstants.HEADER_TIMEZONE) String timezone,
			@Parameter(hidden = true) @RequestHeader HttpHeaders headers)
			throws PuiServiceIncorrectLoginException, PuiServiceIncorrectUserPasswordException,
			PuiServiceUserDisabledException, PuiServiceLoginMaxAttemptsException,
			PuiServiceUserCredentialsExpiredException, PuiServiceUserLockedException {
		return keycloackLogin.loginUser((KeycloakLoginData) KeycloakLoginData.builder().withJwt(authorization)
				.withClient(Pui9KnownClients.KEYCLOCK_CLIENT.name()).withIp(PuiRequestUtils.extractIp(request))
				.withTimezone(timezone).withUserAgent(userAgent).withHeaders(headers));
	}

	@PuiNoSessionRequired
	@Operation(summary = "Check user credentials", description = "Check given user credentials")
	@PostMapping(value = "/checkCredentials", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public KeycloakUserInfo checkCredentials(
			@Parameter(description = "The credentials to check", required = true) @RequestBody KeycloakCredentials keycloakCredentials,
			@Parameter(description = "The token to authorize the request", required = true) @RequestParam String token)
			throws PuiServiceIncorrectUserPasswordException, PuiServiceIncorrectLoginException,
			PuiServiceUserDisabledException, PuiServiceUserCredentialsExpiredException, PuiKeycloakBadTokenException {
		checkToken(token);
		return keycloackLogin.checkCredentials(keycloakCredentials);
	}

	@PuiNoSessionRequired
	@Operation(summary = "Get user info", description = "Get the info of the given user")
	@GetMapping(value = "/getUser", produces = { MediaType.APPLICATION_JSON_VALUE })
	public KeycloakUserInfo getUser(
			@Parameter(description = "The user name", required = true) @RequestParam String user,
			@Parameter(description = "The token to authorize the request", required = true) @RequestParam String token)
			throws PuiCommonUserNotExistsException, PuiKeycloakBadTokenException {
		checkToken(token);
		return keycloackLogin.getUser(user);
	}

	@PuiNoSessionRequired
	@Operation(summary = "Get users info", description = "Get the info the users in the given pagination position")
	@GetMapping(value = "/getUsers", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<KeycloakUserInfo> getUsers(
			@Parameter(description = "The page of the pagination", required = true) @RequestParam Integer page,
			@Parameter(description = "The rows of the pagination", required = true) @RequestParam Integer rows,
			@Parameter(description = "The token to authorize the request", required = true) @RequestParam String token)
			throws PuiKeycloakBadTokenException {
		checkToken(token);
		return keycloackLogin.getUsers(page, rows);
	}

	private void checkToken(String token) throws PuiKeycloakBadTokenException {
		String keycloakToken = variableService.getVariable(PuiVariableValues.KEYCLOAK_TOKEN.name());
		if (!Objects.equals(token, keycloakToken)) {
			throw new PuiKeycloakBadTokenException();
		}
	}

}
