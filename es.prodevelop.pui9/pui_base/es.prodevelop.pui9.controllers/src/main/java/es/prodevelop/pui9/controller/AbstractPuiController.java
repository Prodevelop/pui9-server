package es.prodevelop.pui9.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import es.prodevelop.pui9.eventlistener.PuiEventLauncher;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;

/**
 * This abstract controller is the top of the controllers of PUI. All the
 * controllers should inherit from this one in order to have access to some
 * useful methods, but it's not mandatory to inherit it
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiController {

	protected static final String ID_FUNCTIONALITY_INSERT = "insert";
	protected static final String ID_FUNCTIONALITY_UPDATE = "update";
	protected static final String ID_FUNCTIONALITY_DELETE = "delete";
	protected static final String ID_FUNCTIONALITY_GET = "get";
	protected static final String ID_FUNCTIONALITY_LIST = "list";
	protected static final String ID_FUNCTIONALITY_EXPORT = "export";
	protected static final String METHOD_FUNCTIONALITY_INSERT = "getInsertFunctionality";
	protected static final String METHOD_FUNCTIONALITY_UPDATE = "getUpdateFunctionality";
	protected static final String METHOD_FUNCTIONALITY_DELETE = "getDeleteFunctionality";
	protected static final String METHOD_FUNCTIONALITY_GET = "getGetFunctionality";
	protected static final String METHOD_FUNCTIONALITY_LIST = "getListFunctionality";
	protected static final String METHOD_FUNCTIONALITY_EXPORT = "getExportFunctionality";

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private PuiEventLauncher eventLauncher;

	/**
	 * Get the PUI Event Launcher, that allows to fire PUI Events over the
	 * application. See {@link PuiEvent} class
	 * 
	 * @return
	 */
	protected PuiEventLauncher getEventLauncher() {
		return eventLauncher;
	}

	/**
	 * Get the permission name to Insert a registry
	 */
	protected String getInsertFunctionality() {
		return getWriteFunctionality();
	}

	/**
	 * Get the permission name to Update a registry
	 */
	protected String getUpdateFunctionality() {
		return getWriteFunctionality();
	}

	/**
	 * Get the permission name to Delete a registry
	 */
	protected String getDeleteFunctionality() {
		return getWriteFunctionality();
	}

	/**
	 * Get the permission name to Get a registry
	 */
	protected String getGetFunctionality() {
		return getReadFunctionality();
	}

	/**
	 * Get the permission name to List a model
	 */
	protected String getListFunctionality() {
		return getReadFunctionality();
	}

	/**
	 * Get the permission name to Export a model
	 */
	protected String getExportFunctionality() {
		return getListFunctionality();
	}

	/**
	 * Get the permission name to Read any registry
	 */
	protected String getReadFunctionality() {
		return null;
	}

	/**
	 * Get the permission name to Write any registry
	 */
	protected String getWriteFunctionality() {
		return null;
	}

	protected HttpServletRequest getRequest() {
		Optional<HttpServletRequest> opt = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
				.filter(ServletRequestAttributes.class::isInstance).map(ServletRequestAttributes.class::cast)
				.map(ServletRequestAttributes::getRequest);
		return opt.isPresent() ? opt.get() : null;
	}

	protected HttpServletResponse getResponse() {
		Optional<HttpServletResponse> opt = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
				.filter(ServletRequestAttributes.class::isInstance).map(ServletRequestAttributes.class::cast)
				.map(ServletRequestAttributes::getResponse);
		return opt.isPresent() ? opt.get() : null;
	}

}
