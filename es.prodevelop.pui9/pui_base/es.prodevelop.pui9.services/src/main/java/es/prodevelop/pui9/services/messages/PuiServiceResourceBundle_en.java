package es.prodevelop.pui9.services.messages;

/**
 * English Translation for PUI Common component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiServiceResourceBundle_en extends PuiServiceResourceBundle {

	@Override
	protected String getIncorrectUserPasswordMessage_203() {
		return "The given user or password are not valid";
	}

	@Override
	protected String getIncorrectLoginMessage_204() {
		return "Error when login the application: bad credentials";
	}

	@Override
	protected String getNoSessionMessage_207() {
		return "No session token provided";
	}

	@Override
	protected String getUserSessionTimeoutMessage_209() {
		return "The user session has ended";
	}

	@Override
	protected String getUserDisabledMessage_210() {
		return "User ''{0}'' is disabled";
	}

	@Override
	protected String getLoginMaxAttemptsMessage_220() {
		return "The user ''{0}'' has reached the maximum number of attempts to access the application. Your user has been blocked. Please contact your administrator";
	}

	@Override
	protected String getAuthenticate2faWrongCodeMessage_223() {
		return "The 2FA code is not valid. Try again";
	}

	@Override
	protected String getAuthenticate2faMaxWrongCodeMessage_224() {
		return "The 2FA code is not valid. Maximum attemps reached. The user will be logged out";
	}

	@Override
	protected String getUserNotAuthenticatedMessage_225() {
		return "The user is not properly authenticated";
	}

	@Override
	protected String getUserCredentialsExpiredMessage_226() {
		return "User credentials have expired. You should set a new password";
	}

	@Override
	protected String getUserLockedMessage_227() {
		return "User is bloqued and cannot be used. Contact the admin";
	}

	@Override
	protected String getFromJsonExceptionMessage_801() {
		return "Error while reading the JSON in the server. Please, check you''re sending the data correctly: ''{0}''";
	}

	@Override
	protected String getToJsonExceptionMessage_802() {
		return "Error while converting the response to JSON in the server. Please, check the data is being converted correctly: ''{0}''";
	}

	@Override
	protected String getSendMailExceptionMessage_803() {
		return "Error while sending the mail: {0}";
	}

	@Override
	protected String getWrongMailExceptionMessage_804() {
		return "The email ''{0}'' is not valid";
	}

	@Override
	protected String getTimeoutExceptionMessage_805() {
		return "Timeout on the Web Service: {0} seconds";
	}

	@Override
	protected String getNotAllowedExceptionMessage_806() {
		return "Don't have permission";
	}

	@Override
	protected String getConcurrencyExceptionMessage_807() {
		return "Data concurrency error: the registry was modified by other user";
	}

	@Override
	protected String getNewExceptionMessage_808() {
		return "Error while creating the empty object template";
	}

	@Override
	protected String getGetExceptionMessage_809() {
		return "Error while getting the registry";
	}

	@Override
	protected String getExistsExceptionMessage_810() {
		return "Error while checking the existence of the registry";
	}

	@Override
	protected String getInsertExceptionMessage_811() {
		return "Error while inserting the new registry";
	}

	@Override
	protected String getUpdateExceptionMessage_812() {
		return "Error while updating the registry";
	}

	@Override
	protected String getDeleteExceptionMessage_813() {
		return "Error while deleting the registry";
	}

	@Override
	protected String getCopyRegistryExceptionMessage_814() {
		return "Error while copying the registry";
	}

	@Override
	protected String getNoMailContentExceptionMessage_815() {
		return "Mail content coult not be empty";
	}

	@Override
	protected String getNoApiKeyExceptionMessage_816() {
		return "No API Key provided or it's invalid";
	}

	@Override
	protected String getExportExceptionMessage_817() {
		return "Couldn''t export the list: {0}";
	}

}
