package es.prodevelop.pui9.login;

import javax.servlet.http.HttpServletRequest;

import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectLoginException;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectUserPasswordException;
import es.prodevelop.pui9.services.exceptions.PuiServiceLoginMaxAttemptsException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoSessionException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserCredentialsExpiredException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserDisabledException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserLockedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserSessionTimeoutException;

public interface IPuiOpenapiLogin {

	String OPENAPI_SOURCE = "openapi";

	/**
	 * Check if the current request was executed from Swagger (OpenApi)
	 * 
	 * @param request The current request
	 * @return true or false
	 */
	boolean isOpenapiRequest(HttpServletRequest request);

	/**
	 * Init the Openapi request
	 * 
	 * @param request The request
	 * @throws PuiServiceIncorrectLoginException         If an error when login
	 *                                                   occurs
	 * @throws PuiServiceIncorrectUserPasswordException  If the user or password are
	 *                                                   wrong
	 * @throws PuiServiceUserCredentialsExpiredException If the user credentials
	 *                                                   expired
	 * @throws PuiServiceNoSessionException              If no authorization string
	 *                                                   is provided
	 * @throws PuiServiceUserSessionTimeoutException     If the given authorization
	 *                                                   token is expired
	 * @throws PuiServiceUserLockedException             If the user is locked by
	 *                                                   any reason
	 */
	void initSession(HttpServletRequest request) throws PuiServiceIncorrectUserPasswordException,
			PuiServiceIncorrectLoginException, PuiServiceUserDisabledException, PuiServiceLoginMaxAttemptsException,
			PuiServiceUserCredentialsExpiredException, PuiServiceNoSessionException,
			PuiServiceUserSessionTimeoutException, PuiServiceUserLockedException;

	/**
	 * Finish the Openapi request
	 * 
	 * @param request The request
	 * @throws PuiServiceNoSessionException If no authorization string is provided
	 */
	void finishSession() throws PuiServiceNoSessionException;

}
