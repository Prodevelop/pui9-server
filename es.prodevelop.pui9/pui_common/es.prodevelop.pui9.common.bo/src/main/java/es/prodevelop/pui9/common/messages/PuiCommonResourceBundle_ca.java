package es.prodevelop.pui9.common.messages;

/**
 * Catalan Translation for PUI Common component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiCommonResourceBundle_ca extends PuiCommonResourceBundle {

	@Override
	protected String getRequestResetPasswordSubject() {
		return "Restablir contrasenya";
	}

	@Override
	protected String getPasswordExpirationSubject() {
		return "Data de caducitat de la contrasenya pròxima";
	}

	@Override
	protected String getIssueTicketEmailFields() {
		return "Descripció,Urgència,Nom,Email,Telèfon";
	}

	@Override
	protected String getIssueTicketUrgency() {
		return "{'MAXIMUM':'Màxima', 'HIGH': 'Alta', 'MEDIUM': 'Mitjana', 'LOW': 'Baixa'}";
	}

	@Override
	protected String getInvalidPasswordMessage_202() {
		return "La contrasenya no complix els requisits: {0}";
	}

	@Override
	protected String getModelMessage_205() {
		return "La vista ''{0}'' no està inclosa en PUI_MODEL";
	}

	@Override
	protected String getNoFileMessage_206() {
		return "No es troba el fitxer sol·licitat";
	}

	@Override
	protected String getSamePasswordMessage_208() {
		return "La nueva contrasenya deu ser distinta de l'anterior";
	}

	@Override
	protected String getUserNotExistsMessage_211() {
		return "L''usuari ''{0}'' no existeix";
	}

	@Override
	protected String getUserResetTokenMessage_212() {
		return "No existeix un token per a l'usuari proporcionat";
	}

	@Override
	protected String getImportExportNoModelMessage_213() {
		return "No s'ha especificat l'atribut 'model'";
	}

	@Override
	protected String getImportExportInvalidColumnMessage_214() {
		return "La columna '{0}' no es pot exportar perquè no existeix en la taula de base de dades";
	}

	@Override
	protected String getImportExportPkNotIncludedMessage_215() {
		return "No s''han inclos les columnes PK en l''exportació: ''{0}''";
	}

	@Override
	protected String getImportExportWithErrorsMessage_216() {
		return "La importació no es pot executar perquè hi ha registres amb errors. Revise-la";
	}

	@Override
	protected String getImportExportDtoErrorMessage_217() {
		return "S''ha produït un error a l''establir a la columna ''{0}'' el valor ''{1}''";
	}

	@Override
	protected String getImportExportInvalidModelErrorMessage_218() {
		return "El model ''{0}'' no suporta l''acció d''exportar/importar";
	}

	@Override
	protected String getCopyInvalidModelErrorMessage_219() {
		return "El model ''{0}'' no suporta l''acció de copiar registre";
	}

	@Override
	protected String getUserEmailDuplicatedMessage_222() {
		return "L''email ''{0}'' ja està registrat";
	}

}
