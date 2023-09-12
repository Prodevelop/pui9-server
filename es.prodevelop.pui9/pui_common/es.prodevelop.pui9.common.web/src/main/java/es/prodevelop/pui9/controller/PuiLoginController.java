package es.prodevelop.pui9.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.pui9.annotations.PuiApiKey;
import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.login.LoginData;
import es.prodevelop.pui9.login.PuiLogin;
import es.prodevelop.pui9.login.PuiUserInfo;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.login.TwoFactorAuthenticationData;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.services.exceptions.PuiServiceAuthenticate2faMaxWrongCodeException;
import es.prodevelop.pui9.services.exceptions.PuiServiceAuthenticate2faWrongCodeException;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectLoginException;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectUserPasswordException;
import es.prodevelop.pui9.services.exceptions.PuiServiceLoginMaxAttemptsException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoSessionException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserCredentialsExpiredException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserDisabledException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserLockedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserSessionTimeoutException;
import es.prodevelop.pui9.session.PuiSessionHandler;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiRequestUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

@PuiGenerated
@Controller
@Tag(name = "PUI Login and Session")
@RequestMapping("/login")
public class PuiLoginController extends AbstractPuiController {

	private static final String LIST_FUNCTIONALITY = "LIST_PUI_SESSIONS";
	private static final String KILL_FUNCTIONALITY = "KILL_PUI_SESSIONS";

	@Autowired
	private PuiLogin puiLogin;

	@Autowired
	private PuiSessionHandler sessionHandler;

	@PuiNoSessionRequired
	@Operation(summary = "Login into the application", description = "Login into the application using the given credentials")
	@PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
	public PuiUserInfo signin(HttpServletRequest request, @Parameter(required = true) @RequestBody LoginData loginData,
			@Parameter(in = ParameterIn.HEADER, hidden = true) @RequestHeader(value = HttpHeaders.USER_AGENT) String userAgent,
			@Parameter(in = ParameterIn.HEADER, hidden = true) @RequestHeader(value = PuiConstants.HEADER_TIMEZONE) String timezone,
			@Parameter(hidden = true) @RequestHeader HttpHeaders headers)
			throws PuiServiceIncorrectLoginException, PuiServiceIncorrectUserPasswordException,
			PuiServiceUserDisabledException, PuiServiceLoginMaxAttemptsException,
			PuiServiceUserCredentialsExpiredException, PuiServiceUserLockedException {
		return puiLogin.loginUser(loginData.withIp(PuiRequestUtils.extractIp(request)).withUserAgent(userAgent)
				.withTimezone(timezone).withHeaders(headers));
	}

	@PuiNoSessionRequired
	@Operation(summary = "Generate the QR for 2FA authentication", description = "Generate the QR for 2FA authentication")
	@GetMapping(value = "/generateQr2fa")
	public TwoFactorAuthenticationData generateQr2fa(
			@Parameter(required = true) @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization)
			throws PuiServiceNoSessionException, PuiServiceUserSessionTimeoutException {
		return puiLogin.generateQr2fa(authorization);
	}

	@PuiNoSessionRequired
	@Operation(summary = "Authenticate with 2FA code", description = "Authenticate with 2FA code")
	@GetMapping(value = "/authenticate2fa")
	public void authenticate2fa(
			@Parameter(required = true) @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization,
			@Parameter(required = true) @RequestParam String code)
			throws PuiServiceAuthenticate2faWrongCodeException, PuiServiceAuthenticate2faMaxWrongCodeException,
			PuiServiceNoSessionException, PuiServiceUserSessionTimeoutException {
		puiLogin.authenticate2fa(authorization, code, false);
	}

	@PuiNoSessionRequired
	@Operation(summary = "Check if user is correctly authenticated", description = "Check if user is correctly authenticated")
	@GetMapping(value = "/is2faAuthenticated")
	public boolean is2faAuthenticated(
			@Parameter(hidden = true) @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization)
			throws PuiServiceNoSessionException, PuiServiceUserSessionTimeoutException {
		return puiLogin.is2faAuthenticated(authorization);
	}

	@PuiApiKey
	@Operation(summary = "Get the info of the user session", description = "Get the info of the user session")
	@GetMapping(value = "/getUserInfo")
	public PuiUserInfo getUserInfo(
			@Parameter(required = true) @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization)
			throws PuiServiceNoSessionException, PuiServiceUserSessionTimeoutException {
		return sessionHandler.getUserInfo(authorization);
	}

	@Operation(summary = "Logout the application", description = "Logout the application.")
	@GetMapping(value = "/signout")
	public void signout(
			@Parameter(required = true) @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization)
			throws PuiServiceNoSessionException {
		puiLogin.logoutUser(authorization, true);
	}

	@PuiApiKey(value = LIST_FUNCTIONALITY)
	@Operation(summary = "List all sessions", description = "List all opened user sessions")
	@PuiFunctionality(id = LIST_FUNCTIONALITY, value = LIST_FUNCTIONALITY)
	@GetMapping(value = "/listSessions", produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse<PuiUserSession> listSessions() {
		List<PuiUserSession> list = sessionHandler.getAllSessions();

		SearchResponse<PuiUserSession> resp = new SearchResponse<>();
		resp.setData(list);
		resp.setCurrentPage(1);
		resp.setCurrentRecords(list.size());
		resp.setTotalPages((long) 1);
		resp.setTotalRecords((long) list.size());

		return resp;
	}

	@PuiApiKey(value = KILL_FUNCTIONALITY)
	@Operation(summary = "Kill given session", description = "Kill the given session")
	@PuiFunctionality(id = KILL_FUNCTIONALITY, value = KILL_FUNCTIONALITY)
	@PostMapping(value = "killSessions")
	public void killSessions(@RequestBody List<String> jwts) throws PuiServiceNoSessionException {
		if (ObjectUtils.isEmpty(jwts)) {
			return;
		}

		for (String jwt : jwts) {
			puiLogin.logoutUser(jwt, false);
		}
	}

	@Operation(summary = "Keep the session active", description = "Keep the session active")
	@GetMapping(value = "/keepSessionActive")
	public void keepSessionActive() {
		// do nothing. Simply keeps the session active by calling this method because
		// the CommonInterceptor updates the session
	}

}
