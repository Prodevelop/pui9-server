package es.prodevelop.pui9.common.messages;

import java.util.LinkedHashMap;
import java.util.Map;

import es.prodevelop.pui9.common.exceptions.PuiCommonCopyInvalidModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportDtoColumnErrorException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportInvalidColumnException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportInvalidModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportNoModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportPkNotIncludedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportWithErrorsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonInvalidPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonNoFileException;
import es.prodevelop.pui9.common.exceptions.PuiCommonSamePasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserEmailDuplicatedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserResetTokenException;
import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * Common component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiCommonResourceBundle extends AbstractPuiListResourceBundle {

	public static final String requestResetPasswordSubject = "requestResetPasswordSubject";
	public static final String passwordExpirationSubject = "passwordExpirationSubject";
	public static final String issueTicketEmailFields = "issueTicketEmailFields";
	public static final String issueTicketUrgency = "issueTicketUrgency";

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new LinkedHashMap<>();

		// messages
		messages.put(requestResetPasswordSubject, getRequestResetPasswordSubject());
		messages.put(passwordExpirationSubject, getPasswordExpirationSubject());
		messages.put(issueTicketEmailFields, getIssueTicketEmailFields());
		messages.put(issueTicketUrgency, getIssueTicketUrgency());

		// Exceptions
		messages.put(PuiCommonInvalidPasswordException.CODE, getInvalidPasswordMessage_202());
		messages.put(PuiCommonModelException.CODE, getModelMessage_205());
		messages.put(PuiCommonNoFileException.CODE, getNoFileMessage_206());
		messages.put(PuiCommonSamePasswordException.CODE, getSamePasswordMessage_208());
		messages.put(PuiCommonUserNotExistsException.CODE, getUserNotExistsMessage_211());
		messages.put(PuiCommonUserResetTokenException.CODE, getUserResetTokenMessage_212());
		messages.put(PuiCommonImportExportNoModelException.CODE, getImportExportNoModelMessage_213());
		messages.put(PuiCommonImportExportInvalidColumnException.CODE, getImportExportInvalidColumnMessage_214());
		messages.put(PuiCommonImportExportPkNotIncludedException.CODE, getImportExportPkNotIncludedMessage_215());
		messages.put(PuiCommonImportExportWithErrorsException.CODE, getImportExportWithErrorsMessage_216());
		messages.put(PuiCommonImportExportDtoColumnErrorException.CODE, getImportExportDtoErrorMessage_217());
		messages.put(PuiCommonImportExportInvalidModelException.CODE, getImportExportInvalidModelErrorMessage_218());
		messages.put(PuiCommonCopyInvalidModelException.CODE, getCopyInvalidModelErrorMessage_219());
		messages.put(PuiCommonUserEmailDuplicatedException.CODE, getUserEmailDuplicatedMessage_222());

		return messages;
	}

	protected abstract String getRequestResetPasswordSubject();

	protected abstract String getPasswordExpirationSubject();

	protected abstract String getIssueTicketEmailFields();

	protected abstract String getIssueTicketUrgency();

	protected abstract String getInvalidPasswordMessage_202();

	protected abstract String getModelMessage_205();

	protected abstract String getNoFileMessage_206();

	protected abstract String getSamePasswordMessage_208();

	protected abstract String getUserNotExistsMessage_211();

	protected abstract String getUserResetTokenMessage_212();

	protected abstract String getImportExportNoModelMessage_213();

	protected abstract String getImportExportInvalidColumnMessage_214();

	protected abstract String getImportExportPkNotIncludedMessage_215();

	protected abstract String getImportExportWithErrorsMessage_216();

	protected abstract String getImportExportDtoErrorMessage_217();

	protected abstract String getImportExportInvalidModelErrorMessage_218();

	protected abstract String getCopyInvalidModelErrorMessage_219();

	protected abstract String getUserEmailDuplicatedMessage_222();

}
