package es.prodevelop.pui9.keycloak.exceptions;

import org.springframework.http.HttpStatus;

import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.keycloak.messages.PuiKeycloakMessages;

/**
 * Exception when the provided token in the web services is not valid
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiKeycloakBadTokenException extends PuiServiceException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 291;

	public PuiKeycloakBadTokenException() {
		super(CODE, PuiKeycloakMessages.getSingleton().getString(CODE));
		setShouldLog(false);
		setStatusResponse(HttpStatus.UNAUTHORIZED.value());
	}

}
