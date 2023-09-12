package es.prodevelop.pui9.services.messages;

/**
 * Catalan Translation for PUI Common component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiServiceResourceBundle_ca extends PuiServiceResourceBundle {

	@Override
	protected String getIncorrectUserPasswordMessage_203() {
		return "L'usuari o la contrasenya proporcionada son incorrectes";
	}

	@Override
	protected String getIncorrectLoginMessage_204() {
		return "Error accedint a l'aplicació: dades d'accés incorrectes";
	}

	@Override
	protected String getNoSessionMessage_207() {
		return "No s'ha proporcionat el token de sessió";
	}

	@Override
	protected String getUserSessionTimeoutMessage_209() {
		return "La sessió d'usuari ha caducat";
	}

	@Override
	protected String getUserDisabledMessage_210() {
		return "L''usuari ''{0}'' està deshabilitat";
	}

	@Override
	protected String getLoginMaxAttemptsMessage_220() {
		return "L''usuari ''{0}'' ha alcançat el número màxim d''intents per accedir a l''aplicació. L''usuari ha sigut bloquejat. Per favor, contacte amb el seu administrador";
	}

	@Override
	protected String getAuthenticate2faWrongCodeMessage_223() {
		return "El codi 2FA proporcionat no és vàlid. Intente-ho de nou";
	}

	@Override
	protected String getAuthenticate2faMaxWrongCodeMessage_224() {
		return "El codi 2FA proporcionat no és vàlid. S'ha superat el nº màxim d'intents. Es tancarà la sessió de l'usuari";
	}

	@Override
	protected String getUserNotAuthenticatedMessage_225() {
		return "L'usuari no està correctament autenticat";
	}

	@Override
	protected String getUserCredentialsExpiredMessage_226() {
		return "Les credencials de l'usuari han caducat. Deu restablir la contrasenya";
	}

	@Override
	protected String getUserLockedMessage_227() {
		return "L'usuari està bloquejat i no es pot utilitzar. Contacte amb l'administrador";
	}

	@Override
	protected String getFromJsonExceptionMessage_801() {
		return "Error al llegir el JSON en el servidor. Per favor, comprove que s''estàn enviant correctament les dades: ''{0}''";
	}

	@Override
	protected String getToJsonExceptionMessage_802() {
		return "Error al convertir la resposta a JSON en el servidor. Per favor, comprove que s''estàn convertint correctament les dades: ''{0}''";
	}

	@Override
	protected String getSendMailExceptionMessage_803() {
		return "Error a l''enviar el correu: {0}";
	}

	@Override
	protected String getWrongMailExceptionMessage_804() {
		return "El correu ''{0}'' no es vàlid";
	}

	@Override
	protected String getTimeoutExceptionMessage_805() {
		return "S''ha superat el temps d''espera en el Servei Web: {0} segons";
	}

	@Override
	protected String getNotAllowedExceptionMessage_806() {
		return "No es disposa de suficients permisos";
	}

	@Override
	protected String getConcurrencyExceptionMessage_807() {
		return "Error de concurrència de dades: el registre ha sigut modificat per un altre usuari";
	}

	@Override
	protected String getNewExceptionMessage_808() {
		return "Error al crear un objecte plantilla buit";
	}

	@Override
	protected String getGetExceptionMessage_809() {
		return "Error a l'obtindre el registre";
	}

	@Override
	protected String getExistsExceptionMessage_810() {
		return "Error al comprovar l'existència del registre";
	}

	@Override
	protected String getInsertExceptionMessage_811() {
		return "Error al insertar el nou registre";
	}

	@Override
	protected String getUpdateExceptionMessage_812() {
		return "Error a l'actualitzar el registre";
	}

	@Override
	protected String getDeleteExceptionMessage_813() {
		return "Error a l'esborrar el registre";
	}

	@Override
	protected String getCopyRegistryExceptionMessage_814() {
		return "Error al realitzar la copia del registre";
	}

	@Override
	protected String getNoMailContentExceptionMessage_815() {
		return "El contingut de l'email no pot ser buit";
	}

	@Override
	protected String getNoApiKeyExceptionMessage_816() {
		return "No s'ha proporcional l'API Key o es invàlida";
	}

	@Override
	protected String getExportExceptionMessage_817() {
		return "No s''ha pogut exportar el llistat: {0}";
	}

}
