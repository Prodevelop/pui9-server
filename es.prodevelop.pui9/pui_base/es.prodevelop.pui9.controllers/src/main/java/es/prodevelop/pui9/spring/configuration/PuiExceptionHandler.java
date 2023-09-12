package es.prodevelop.pui9.spring.configuration;

import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiExceptionDto;

/**
 * This class acts as Exception Handler for all the PUI applications. Each
 * exception occurred in the code, and called from a Controller (a Web Service),
 * will be cached by this handler and return the desired object to the client.
 * <p>
 * This is special for all the {@link PuiException} thrown
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@ControllerAdvice
public class PuiExceptionHandler {

	private Logger logger = LogManager.getLogger(this.getClass());

	@ExceptionHandler(Exception.class)
	private PuiExceptionDto handlePuiExceptionDto(Exception ex, WebRequest request) {
		PuiExceptionDto errorDto;
		boolean shouldLog = true;
		Exception realException = ex;
		if (!(realException instanceof PuiException) && (realException.getCause() instanceof PuiException)) {
			realException = (Exception) ex.getCause();
		}

		if (realException instanceof PuiException) {
			errorDto = ((PuiException) realException).asTransferObject();
			shouldLog = ((PuiException) realException).shouldLog();
			errorDto.setClassName(((PuiException) realException).getClassName());
		} else {
			errorDto = new PuiException(realException).asTransferObject();
		}

		String url = "";
		if (request instanceof ServletWebRequest) {
			url = ((ServletWebRequest) request).getRequest().getRequestURI();
		}
		errorDto.setUrl(url);

		HttpStatus status = HttpStatus.resolve(errorDto.getStatusCode());
		if (status != null) {
			errorDto.setStatusCode(status.value());
		}
		errorDto.setErrorClassName(ex.getClass().getName());

		if (shouldLog) {
			if (realException instanceof ServletException) {
				String message = realException.getMessage() + " (url: {})";
				logger.error(message, url, realException);
			} else {
				logger.error(realException.toString(), realException);
			}
		}

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, realException, RequestAttributes.SCOPE_REQUEST);
		}

		return errorDto;
	}

}
