package es.prodevelop.pui9.login.openapi;

import java.util.Collections;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.enums.Pui9KnownClients;
import es.prodevelop.pui9.login.IPuiOpenapiLogin;
import es.prodevelop.pui9.login.LoginData;
import es.prodevelop.pui9.login.PuiLogin;
import es.prodevelop.pui9.login.PuiUserInfo;
import es.prodevelop.pui9.login.PuiUserSession;
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
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiRequestUtils;

@Component
public class PuiOpenapiLogin implements IPuiOpenapiLogin {

	private static final String OPENAPI_SOURCE = "openapi";

	@Autowired
	private PuiLogin puiLogin;

	@Override
	public boolean isOpenapiRequest(HttpServletRequest request) {
		String source = getRequestSource(request);
		String authorization = getRequestAuthorization(request);
		return Objects.equals(source, OPENAPI_SOURCE) && authorization != null
				&& authorization.startsWith(PuiConstants.BASIC_PREFIX);
	}

	@Override
	public void initSession(HttpServletRequest request) throws PuiServiceIncorrectUserPasswordException,
			PuiServiceIncorrectLoginException, PuiServiceUserDisabledException, PuiServiceLoginMaxAttemptsException,
			PuiServiceUserCredentialsExpiredException, PuiServiceNoSessionException,
			PuiServiceUserSessionTimeoutException, PuiServiceUserLockedException {
		String authorization = getRequestAuthorization(request);
		if (ObjectUtils.isEmpty(authorization)) {
			throw new PuiServiceIncorrectUserPasswordException();
		}
		authorization = authorization.replace(PuiConstants.BASIC_PREFIX, "");

		String user = getUser(authorization);
		String password = getPassword(authorization);
		String ip = PuiRequestUtils.extractIp(request);
		String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
		String timezone = getRequestZoneOffset(request);

		HttpHeaders headers = new HttpHeaders();
		Collections.list(request.getHeaderNames()).forEach(header -> headers.add(header, request.getHeader(header)));

		LoginData loginData = new LoginData().withUsr(user).withPassword(password).withPersistent(false).withIp(ip)
				.withUserAgent(userAgent).withTimezone(timezone).withClient(Pui9KnownClients.OPENAPI_CLIENT.name())
				.withHeaders(headers);
		PuiUserInfo pui = puiLogin.loginUser(loginData);
		try {
			puiLogin.authenticate2fa(pui.getJwt().replace(PuiConstants.BEARER_PREFIX, ""), null, true);
		} catch (PuiServiceAuthenticate2faWrongCodeException | PuiServiceAuthenticate2faMaxWrongCodeException
				| PuiServiceNoSessionException | PuiServiceUserSessionTimeoutException e) {
			// do nothing
		}
	}

	@Override
	public void finishSession() throws PuiServiceNoSessionException {
		if (PuiUserSession.getCurrentSession() != null) {
			puiLogin.logoutUser(PuiUserSession.getCurrentSession().getJwt(), true);
		}
	}

	private String getUser(String authorization) throws PuiServiceIncorrectUserPasswordException {
		String decoded = new String(Base64Utils.decodeFromString(authorization));
		String[] splits = decoded.split(":");
		if (splits.length != 2) {
			throw new PuiServiceIncorrectUserPasswordException();
		}

		return splits[0];
	}

	private String getPassword(String authorization) throws PuiServiceIncorrectUserPasswordException {
		String decoded = new String(Base64Utils.decodeFromString(authorization));
		String[] splits = decoded.split(":");
		if (splits.length != 2) {
			throw new PuiServiceIncorrectUserPasswordException();
		}

		return splits[1];
	}

	private String getRequestAuthorization(HttpServletRequest request) {
		return request.getHeader(HttpHeaders.AUTHORIZATION);
	}

	private String getRequestSource(HttpServletRequest request) {
		return request.getHeader(PuiConstants.HEADER_SOURCE);
	}

	private String getRequestZoneOffset(HttpServletRequest request) {
		return request.getHeader(PuiConstants.HEADER_TIMEZONE);
	}

}
