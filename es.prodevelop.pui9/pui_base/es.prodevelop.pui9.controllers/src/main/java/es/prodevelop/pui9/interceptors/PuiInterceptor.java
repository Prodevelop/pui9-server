package es.prodevelop.pui9.interceptors;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.lang.LanguageThreadLocal;
import es.prodevelop.pui9.login.IPuiApiKeyLogin;
import es.prodevelop.pui9.login.IPuiOpenapiLogin;
import es.prodevelop.pui9.login.IPuiSessionContext;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.mvc.configuration.IPuiRequestMappingHandlerMapping;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoSessionException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNotAllowedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserNotAuthenticatedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserSessionTimeoutException;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

/**
 * This is a base {@link HandlerInterceptor} implementation for PUI. Basically
 * it allows to manage the responses to convert them into a JSON response
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiInterceptor implements HandlerInterceptor {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	protected IPuiSessionContext getPuiSessionContext() {
		return PuiApplicationContext.getInstance().getBean(IPuiSessionContext.class);
	}

	protected IPuiApiKeyLogin getPuiApiKeyLogin() {
		return PuiApplicationContext.getInstance().getBean(IPuiApiKeyLogin.class);
	}

	protected IPuiOpenapiLogin getPuiOpenapiLogin() {
		return PuiApplicationContext.getInstance().getBean(IPuiOpenapiLogin.class);
	}

	protected IPuiRequestMappingHandlerMapping getPuiRequestMapping() {
		return PuiApplicationContext.getInstance().getBean(IPuiRequestMappingHandlerMapping.class);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		cleanPreviousRequests(request);

		if (getPuiRequestMapping().isWebServiceSecured(handler)) {
			if (isApiKeyRequest(request, handler)) {
				getPuiApiKeyLogin().validateApiKey(request, handler);
				return true;
			} else if (isSessionRequiredRequest(handler)) {
				if (isOpenapiRequest(request)) {
					getPuiOpenapiLogin().initSession(request);
				} else {
					setContextSession(request);
				}
				checkUserPermission(handler);
				setLanguageToCurrentSession(request);
				return true;
			} else {
				throw new PuiServiceNotAllowedException();
			}
		} else {
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return;
		}

		if (getPuiRequestMapping().isWebServiceSecured(handler) && !isApiKeyRequest(request, handler)
				&& isSessionRequiredRequest(handler)) {
			if (isOpenapiRequest(request)) {
				getPuiOpenapiLogin().finishSession();
			} else {
				removeContextSession();
			}
		}

		LanguageThreadLocal.getSingleton().removeData();

	}

	protected void cleanPreviousRequests(HttpServletRequest request) {
		removeContextSession();
		setLanguageToThreadLocal(request);
	}

	protected boolean isApiKeyRequest(HttpServletRequest request, Object handler) {
		return getPuiApiKeyLogin() != null && getPuiApiKeyLogin().isApiKeyRequest(request, handler);
	}

	protected boolean isSessionRequiredRequest(Object handler) {
		return getPuiRequestMapping().isWebServiceSessionRequired(handler);
	}

	protected boolean isOpenapiRequest(HttpServletRequest request) {
		return getPuiOpenapiLogin() != null && getPuiOpenapiLogin().isOpenapiRequest(request);
	}

	/**
	 * Set the user's session for current context
	 * 
	 * @param request The request
	 * @throws PuiServiceNoSessionException            If no authorization string is
	 *                                                 provided
	 * @throws PuiServiceUserSessionTimeoutException   If the given authorization
	 *                                                 token is expired
	 * @throws PuiServiceUserNotAuthenticatedException If the user is not
	 *                                                 completelly authenticated
	 */
	protected void setContextSession(HttpServletRequest request) throws PuiServiceNoSessionException,
			PuiServiceUserSessionTimeoutException, PuiServiceUserNotAuthenticatedException {
		if (getPuiSessionContext() != null) {
			getPuiSessionContext().setContextSession(request.getHeader(HttpHeaders.AUTHORIZATION));
		}
	}

	/**
	 * Remove the session from current context
	 */
	protected void removeContextSession() {
		if (getPuiSessionContext() != null) {
			getPuiSessionContext().removeContextSession();
		}
	}

	/**
	 * Check if the user has permission to execute the given Web Service. Any Web
	 * Service that requires permission, should declare the {@link PuiFunctionality}
	 * annotation. This annotation is used to extract the name of the funcionality
	 * that the user should have to consume it
	 * 
	 * @param handler The handler that represents the Web Service
	 * @throws PuiCommonNotAllowedException If the user has no permission to execute
	 *                                      it
	 */
	protected void checkUserPermission(Object handler) throws PuiServiceNotAllowedException {
		String functionality = getPuiRequestMapping().getWebServiceFunctionality(handler);
		boolean hasFunctionality = false;

		if (ObjectUtils.isEmpty(functionality)) {
			hasFunctionality = true;
		} else {
			hasFunctionality = PuiUserSession.getCurrentSession().getFunctionalities().stream()
					.filter(func -> func.equals(functionality)).count() > 0;
		}

		if (!hasFunctionality) {
			throw new PuiServiceNotAllowedException();
		}
	}

	protected void setLanguageToThreadLocal(HttpServletRequest request) {
		LanguageThreadLocal.getSingleton().setData(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
	}

	protected void setLanguageToThreadLocal(PuiLanguage lang) {
		LanguageThreadLocal.getSingleton().setData(lang);
	}

	/**
	 * If using a session, set the language for it. If no language could be
	 * obtained, {@link PuiLanguage#DEFAULT_LANG} will be set
	 * 
	 * @param request The request servlet
	 */
	protected void setLanguageToCurrentSession(HttpServletRequest request) {
		PuiLanguage lang = getLanguageForRequest(request);
		if (lang == null) {
			lang = PuiLanguage.DEFAULT_LANG;
		}
		PuiUserSession.getCurrentSession().withLanguage(lang);
		setLanguageToThreadLocal(lang);
	}

	/**
	 * The order to get the language is the following:
	 * <p>
	 * <ol>
	 * <li><b>URL Parameter: </b>
	 * http://localhost:8080/appname/controller/action?<b>lang=en</b></li>
	 * <li><b>Request Header language ('Accept-Language' header): </b>
	 * Accept-Language:en-US,en;q=0.8,en-GB;q=0.6,es;q=0.4,ca;q=0.2</li>
	 * <li><b>User language: </b>the language registered by the user</li>
	 * <li><b>Default language in DB: </b>The language set as default in the DB</li>
	 * </ol>
	 * 
	 * @param request The request
	 * @return The language to use in the request
	 */
	protected PuiLanguage getLanguageForRequest(HttpServletRequest request) {
		// URL parameter
		PuiLanguage lang = getLanguageFromUrlParameter(request);
		if (lang == null) {
			// Request language
			lang = getLanguageFromHeaders(request);
		}
		if (lang == null) {
			// From User language
			lang = getLanguageFromLoggedUser();
		}
		if (lang == null) {
			// From default in DB
			lang = getLanguageFromDB();
		}

		return lang;
	}

	/**
	 * Returns the language from the Headers of the request: first check if any
	 * called 'lang' exists; if not, check if 'Accept-Language' exists
	 * 
	 * @param request The current request
	 * @return The language if found or null
	 */
	protected PuiLanguage getLanguageFromHeaders(HttpServletRequest request) {
		String lang = request.getHeader(IDto.LANG_COLUMN_NAME);
		if (PuiLanguageUtils.existLanguage(lang)) {
			return new PuiLanguage(lang);
		}

		Locale locale = request.getLocale();
		if (PuiLanguageUtils.existLanguage(locale)) {
			return new PuiLanguage(locale);
		}

		return null;
	}

	/**
	 * Returns the language from the URL Parameters of the request. The parameter
	 * should be called 'lang'
	 * 
	 * @param request The current request
	 * @return The language if found or null
	 */
	protected PuiLanguage getLanguageFromUrlParameter(HttpServletRequest request) {
		String lang = request.getParameter(IDto.LANG_COLUMN_NAME);
		return PuiLanguageUtils.existLanguage(lang) ? new PuiLanguage(lang) : null;
	}

	/**
	 * Returns the language that the logged user
	 * 
	 * @return The language if found or null
	 */
	protected PuiLanguage getLanguageFromLoggedUser() {
		PuiUserSession userSession = PuiUserSession.getCurrentSession();
		if (userSession != null) {
			return PuiLanguageUtils.existLanguage(userSession.getLanguage()) ? userSession.getLanguage() : null;
		} else {
			return null;
		}
	}

	/**
	 * Returns the default language in the DB
	 * 
	 * @return The language if found or null
	 */
	protected PuiLanguage getLanguageFromDB() {
		return PuiLanguageUtils.getDefaultLanguage();
	}

}
