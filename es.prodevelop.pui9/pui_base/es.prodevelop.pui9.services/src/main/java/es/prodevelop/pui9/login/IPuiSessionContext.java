package es.prodevelop.pui9.login;

import es.prodevelop.pui9.services.exceptions.PuiServiceNoSessionException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserNotAuthenticatedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserSessionTimeoutException;

public interface IPuiSessionContext {

	/**
	 * Set the session for the current request
	 * 
	 * @param jwt The authorization string (the JWT token)
	 * @return The authorization string updated (the expiration time is updated
	 * @throws PuiServiceNoSessionException            If no authorization string is
	 *                                                provided
	 * @throws PuiServiceUserSessionTimeoutException   If the given authorization
	 *                                                token is expired
	 * @throws PuiServiceUserNotAuthenticatedException If the user is not completelly
	 *                                                authenticated
	 */
	void setContextSession(String jwt) throws PuiServiceNoSessionException, PuiServiceUserSessionTimeoutException,
			PuiServiceUserNotAuthenticatedException;

	/**
	 * Remove the session of the current logged user
	 */
	void removeContextSession();

}
