package es.prodevelop.pui9.services.messages;

/**
 * Spanish Translation for PUI Common component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiServiceResourceBundle_es extends PuiServiceResourceBundle {

	@Override
	protected String getIncorrectUserPasswordMessage_203() {
		return "El usuario o el password suministrados son incorrectos";
	}

	@Override
	protected String getIncorrectLoginMessage_204() {
		return "Error al hacer login: datos de acceso incorrectos";
	}

	@Override
	protected String getNoSessionMessage_207() {
		return "No se ha proporcionado el token de sesión";
	}

	@Override
	protected String getUserSessionTimeoutMessage_209() {
		return "La sesión de usuario ha caducado";
	}

	@Override
	protected String getUserDisabledMessage_210() {
		return "El usuario ''{0}'' está deshabilitado";
	}

	@Override
	protected String getLoginMaxAttemptsMessage_220() {
		return "El usuario ''{0}'' ha alcanzado el máximo número de intentos para acceder a la aplicación. Su usuario ha sido bloqueado. Por favor, contacte con su administrador";
	}

	@Override
	protected String getAuthenticate2faWrongCodeMessage_223() {
		return "El código 2FA proporcionado no es válido. Inténtelo de nuevo";
	}

	@Override
	protected String getAuthenticate2faMaxWrongCodeMessage_224() {
		return "El código 2FA proporcionado no es válido. Se ha superado el nº máximo de intentos. Se cerrará la sesión del usuario";
	}

	@Override
	protected String getUserNotAuthenticatedMessage_225() {
		return "El usuario no está correctamente autenticado";
	}

	@Override
	protected String getUserCredentialsExpiredMessage_226() {
		return "Las credenciales del usuario han caducado. Debe restablecer la contraseña";
	}

	@Override
	protected String getUserLockedMessage_227() {
		return "El usuario está bloqueado y no puede utilizarse. Contacte con el administrador";
	}

	@Override
	protected String getFromJsonExceptionMessage_801() {
		return "Error al leer el JSON en el servidor. Por favor, compruebe que se están enviando correctamente los datos: ''{0}''";
	}

	@Override
	protected String getToJsonExceptionMessage_802() {
		return "Error al convertir la respuesta a JSON en el servidor. Por favor, compruebe que se están convirtiendo correctamente los datos: ''{0}''";
	}

	@Override
	protected String getSendMailExceptionMessage_803() {
		return "Error al enviar el correo: {0}";
	}

	@Override
	protected String getWrongMailExceptionMessage_804() {
		return "El correo ''{0}'' no es válido";
	}

	@Override
	protected String getTimeoutExceptionMessage_805() {
		return "Se ha superado el tiempo de espera en el Servicio Web: {0} segundos";
	}

	@Override
	protected String getNotAllowedExceptionMessage_806() {
		return "No dispone de suficientes permisos";
	}

	@Override
	protected String getConcurrencyExceptionMessage_807() {
		return "Error de concurrencia de datos: el registro ha sido modificado por otro usuario";
	}

	@Override
	protected String getNewExceptionMessage_808() {
		return "Error al crear un objecto plantilla vacío";
	}

	@Override
	protected String getGetExceptionMessage_809() {
		return "Error al obtener el registro";
	}

	@Override
	protected String getExistsExceptionMessage_810() {
		return "Error al comprobar la existencia del registro";
	}

	@Override
	protected String getInsertExceptionMessage_811() {
		return "Error al insertar el nuevo registro";
	}

	@Override
	protected String getUpdateExceptionMessage_812() {
		return "Error al actualizar el registro";
	}

	@Override
	protected String getDeleteExceptionMessage_813() {
		return "Error al borrar el registro";
	}

	@Override
	protected String getCopyRegistryExceptionMessage_814() {
		return "Error al realizar la copia del registro";
	}

	@Override
	protected String getNoMailContentExceptionMessage_815() {
		return "El contenido del email no puede ser vacío";
	}

	@Override
	protected String getNoApiKeyExceptionMessage_816() {
		return "No se ha proporcionado API Key o es inválida";
	}

	@Override
	protected String getExportExceptionMessage_817() {
		return "No se ha podido exportar el listado: {0}";
	}

}
