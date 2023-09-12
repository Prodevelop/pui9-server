package es.prodevelop.pui9.common.messages;

/**
 * Spanish Translation for PUI Common component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiCommonResourceBundle_es extends PuiCommonResourceBundle {

	@Override
	protected String getRequestResetPasswordSubject() {
		return "Restablecer contraseña";
	}

	@Override
	protected String getPasswordExpirationSubject() {
		return "Fecha de caducidad de la contraseña cercana";
	}

	@Override
	protected String getInvalidPasswordMessage_202() {
		return "El password no cumple con los requisitos: {0}";
	}

	@Override
	protected String getModelMessage_205() {
		return "El modelo ''{0}'' no está incluido en PUI_MODEL";
	}

	@Override
	protected String getNoFileMessage_206() {
		return "No se encuentra el fichero solicitado";
	}

	@Override
	protected String getSamePasswordMessage_208() {
		return "La nueva contraseña debe ser distinta de la anterior";
	}

	@Override
	protected String getUserNotExistsMessage_211() {
		return "El usuario ''{0}'' no existe";
	}

	@Override
	protected String getUserResetTokenMessage_212() {
		return "No existe un usuario para el token proporcionado";
	}

	@Override
	protected String getImportExportNoModelMessage_213() {
		return "No se ha especificado el atributo 'model'";
	}

	@Override
	protected String getImportExportInvalidColumnMessage_214() {
		return "La columna ''{0}'' no se puede exportar porque no existe en la tabla de base de datos";
	}

	@Override
	protected String getImportExportPkNotIncludedMessage_215() {
		return "No se ha incluido las columnas PK en la exportación: ''{0}''";
	}

	@Override
	protected String getImportExportWithErrorsMessage_216() {
		return "La importación no se puede ejecutar porque hay registros con errores. Revísela";
	}

	@Override
	protected String getImportExportDtoErrorMessage_217() {
		return "Se ha producido un error al establecer a la columna ''{0}'' el valor ''{1}''";
	}

	@Override
	protected String getImportExportInvalidModelErrorMessage_218() {
		return "El modelo ''{0}'' no soporta la acción de exportar/importar";
	}

	@Override
	protected String getCopyInvalidModelErrorMessage_219() {
		return "El modelo ''{0}'' no soporta la acción de copiar registro";
	}

	@Override
	protected String getUserEmailDuplicatedMessage_222() {
		return "El email ''{0}'' ya está registrado";
	}

}
