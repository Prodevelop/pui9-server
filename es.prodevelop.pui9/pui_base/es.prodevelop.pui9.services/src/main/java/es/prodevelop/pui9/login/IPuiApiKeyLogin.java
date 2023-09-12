package es.prodevelop.pui9.login;

import javax.servlet.http.HttpServletRequest;

import es.prodevelop.pui9.services.exceptions.PuiServiceNoApiKeyException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNotAllowedException;

/**
 * Interface for Api Key support
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiApiKeyLogin {

	/**
	 * Check if given request can be executed via Api Key
	 * 
	 * @param request The current request
	 * @param handler The endpoint
	 * @return true or false
	 */
	boolean isApiKeyRequest(HttpServletRequest request, Object handler);

	/**
	 * Validate the Api Key provided in the request
	 * 
	 * @param request The current request
	 * @param handler The endpoint
	 * @throws PuiServiceNoApiKeyException
	 * @throws PuiServiceNotAllowedException
	 */
	void validateApiKey(HttpServletRequest request, Object handler)
			throws PuiServiceNoApiKeyException, PuiServiceNotAllowedException;

}
