package es.prodevelop.pui9.mvc.configuration;

import java.util.Map;

public interface IPuiRequestMappingHandlerMapping {

	/**
	 * Returns a map with detailed information about all the controllers
	 * 
	 * @return
	 */
	Map<String, PuiControllersInfo> getUrlsAndFunctionalitiesByController();

	/**
	 * Check if the consumed Web Service is secured or not
	 * 
	 * @param handler The Web Service to be consumed
	 * @return true or false depending on the Web Service is secured or not
	 */
	boolean isWebServiceSecured(Object handler);

	/**
	 * Check if a session is required to exist for cunsuming the given Web Service.
	 * Basically it checks if the {@link PuiNoSessionRequired} annotation exists for
	 * the consumed method or its declaring class (or any class in the hierarchy)
	 * 
	 * @param handler The handler that represents the Web Service to be consumed
	 * @return true if the Web Service requires a session; false if not
	 */
	boolean isWebServiceSessionRequired(Object handler);

	/**
	 * Check if the use of an API KEY is supported for cunsuming the given Web
	 * Service. Basically it checks if the {@link PuiApiKey} annotation exists for
	 * the consumed method or its declaring class (or any class in the hierarchy)
	 * 
	 * @param handler The handler that represents the Web Service to be consumed
	 * @return true if the Web Service accepts api key consumption; false if not
	 */
	boolean isWebServiceApiKeySupported(Object handler);

	/**
	 * Obtains the needed functionality to consume the given handler with a logged
	 * user
	 * 
	 * @param handler The handler to be consumed
	 * @return The needed functionality
	 */
	String getWebServiceFunctionality(Object handler);

	/**
	 * Obtains the needed functionality to consume the given handler with api-key
	 * 
	 * @param handler The handler to be consumed
	 * @return The needed functionality
	 */
	String getWebServiceApiKeyFunctionality(Object handler);

}
