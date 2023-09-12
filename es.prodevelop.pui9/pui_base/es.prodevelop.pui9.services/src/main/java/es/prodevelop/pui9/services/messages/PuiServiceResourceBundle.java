package es.prodevelop.pui9.services.messages;

import java.util.LinkedHashMap;
import java.util.Map;

import es.prodevelop.pui9.exceptions.PuiServiceCopyRegistryException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceExistsException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceNewException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;
import es.prodevelop.pui9.services.exceptions.PuiServiceAuthenticate2faMaxWrongCodeException;
import es.prodevelop.pui9.services.exceptions.PuiServiceAuthenticate2faWrongCodeException;
import es.prodevelop.pui9.services.exceptions.PuiServiceConcurrencyException;
import es.prodevelop.pui9.services.exceptions.PuiServiceExportException;
import es.prodevelop.pui9.services.exceptions.PuiServiceFromJsonException;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectLoginException;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectUserPasswordException;
import es.prodevelop.pui9.services.exceptions.PuiServiceLoginMaxAttemptsException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoApiKeyException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoMailContentException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoSessionException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNotAllowedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceSendMailException;
import es.prodevelop.pui9.services.exceptions.PuiServiceTimeoutException;
import es.prodevelop.pui9.services.exceptions.PuiServiceToJsonException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserCredentialsExpiredException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserDisabledException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserLockedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserNotAuthenticatedException;
import es.prodevelop.pui9.services.exceptions.PuiServiceUserSessionTimeoutException;
import es.prodevelop.pui9.services.exceptions.PuiServiceWrongMailException;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * Common component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiServiceResourceBundle extends AbstractPuiListResourceBundle {

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new LinkedHashMap<>();

		// Exceptions
		messages.put(PuiServiceIncorrectUserPasswordException.CODE, getIncorrectUserPasswordMessage_203());
		messages.put(PuiServiceIncorrectLoginException.CODE, getIncorrectLoginMessage_204());
		messages.put(PuiServiceNoSessionException.CODE, getNoSessionMessage_207());
		messages.put(PuiServiceUserSessionTimeoutException.CODE, getUserSessionTimeoutMessage_209());
		messages.put(PuiServiceUserDisabledException.CODE, getUserDisabledMessage_210());
		messages.put(PuiServiceLoginMaxAttemptsException.CODE, getLoginMaxAttemptsMessage_220());
		messages.put(PuiServiceAuthenticate2faWrongCodeException.CODE, getAuthenticate2faWrongCodeMessage_223());
		messages.put(PuiServiceAuthenticate2faMaxWrongCodeException.CODE, getAuthenticate2faMaxWrongCodeMessage_224());
		messages.put(PuiServiceUserNotAuthenticatedException.CODE, getUserNotAuthenticatedMessage_225());
		messages.put(PuiServiceUserCredentialsExpiredException.CODE, getUserCredentialsExpiredMessage_226());
		messages.put(PuiServiceUserLockedException.CODE, getUserLockedMessage_227());

		messages.put(PuiServiceFromJsonException.CODE, getFromJsonExceptionMessage_801());
		messages.put(PuiServiceToJsonException.CODE, getToJsonExceptionMessage_802());
		messages.put(PuiServiceSendMailException.CODE, getSendMailExceptionMessage_803());
		messages.put(PuiServiceWrongMailException.CODE, getWrongMailExceptionMessage_804());
		messages.put(PuiServiceTimeoutException.CODE, getTimeoutExceptionMessage_805());
		messages.put(PuiServiceNotAllowedException.CODE, getNotAllowedExceptionMessage_806());
		messages.put(PuiServiceConcurrencyException.CODE, getConcurrencyExceptionMessage_807());
		messages.put(PuiServiceNewException.CODE, getNewExceptionMessage_808());
		messages.put(PuiServiceGetException.CODE, getGetExceptionMessage_809());
		messages.put(PuiServiceExistsException.CODE, getExistsExceptionMessage_810());
		messages.put(PuiServiceInsertException.CODE, getInsertExceptionMessage_811());
		messages.put(PuiServiceUpdateException.CODE, getUpdateExceptionMessage_812());
		messages.put(PuiServiceDeleteException.CODE, getDeleteExceptionMessage_813());
		messages.put(PuiServiceCopyRegistryException.CODE, getCopyRegistryExceptionMessage_814());
		messages.put(PuiServiceNoMailContentException.CODE, getNoMailContentExceptionMessage_815());
		messages.put(PuiServiceNoApiKeyException.CODE, getNoApiKeyExceptionMessage_816());
		messages.put(PuiServiceExportException.CODE, getExportExceptionMessage_817());

		return messages;
	}

	protected abstract String getIncorrectUserPasswordMessage_203();

	protected abstract String getIncorrectLoginMessage_204();

	protected abstract String getNoSessionMessage_207();

	protected abstract String getUserSessionTimeoutMessage_209();

	protected abstract String getUserDisabledMessage_210();

	protected abstract String getLoginMaxAttemptsMessage_220();

	protected abstract String getAuthenticate2faWrongCodeMessage_223();

	protected abstract String getAuthenticate2faMaxWrongCodeMessage_224();

	protected abstract String getUserNotAuthenticatedMessage_225();

	protected abstract String getUserCredentialsExpiredMessage_226();

	protected abstract String getUserLockedMessage_227();

	protected abstract String getFromJsonExceptionMessage_801();

	protected abstract String getToJsonExceptionMessage_802();

	protected abstract String getSendMailExceptionMessage_803();

	protected abstract String getWrongMailExceptionMessage_804();

	protected abstract String getTimeoutExceptionMessage_805();

	protected abstract String getNotAllowedExceptionMessage_806();

	protected abstract String getConcurrencyExceptionMessage_807();

	protected abstract String getNewExceptionMessage_808();

	protected abstract String getGetExceptionMessage_809();

	protected abstract String getExistsExceptionMessage_810();

	protected abstract String getInsertExceptionMessage_811();

	protected abstract String getUpdateExceptionMessage_812();

	protected abstract String getDeleteExceptionMessage_813();

	protected abstract String getCopyRegistryExceptionMessage_814();

	protected abstract String getNoMailContentExceptionMessage_815();

	protected abstract String getNoApiKeyExceptionMessage_816();

	protected abstract String getExportExceptionMessage_817();
}
