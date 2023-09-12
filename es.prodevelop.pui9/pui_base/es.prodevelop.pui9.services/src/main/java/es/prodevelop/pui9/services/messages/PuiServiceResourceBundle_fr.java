package es.prodevelop.pui9.services.messages;

/**
 * French Translation for PUI Common component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiServiceResourceBundle_fr extends PuiServiceResourceBundle {

	@Override
	protected String getIncorrectUserPasswordMessage_203() {
		return "L'utilisateur ou le mot de passe fournis sont incorrectes";
	}

	@Override
	protected String getIncorrectLoginMessage_204() {
		return "Erreur lors de la connexion à l'application : mauvaises informations d'identification";
	}

	@Override
	protected String getNoSessionMessage_207() {
		return "Aucun token de session n'est fourni";
	}

	@Override
	protected String getUserSessionTimeoutMessage_209() {
		return "La session de l'utilisateur a expiré";
	}

	@Override
	protected String getUserDisabledMessage_210() {
		return "Utilisateur ''{0}'' est désactivé";
	}

	@Override
	protected String getLoginMaxAttemptsMessage_220() {
		return "L''utilisateur ''{0}'' a atteint le nombre maximum de tentatives d''accès à l''application. Votre utilisateur a été bloqué. Veuillez contacter votre administrateur";
	}

	@Override
	protected String getAuthenticate2faWrongCodeMessage_223() {
		return "Le code 2FA n'est pas valide. Essayez à nouveau";
	}

	@Override
	protected String getAuthenticate2faMaxWrongCodeMessage_224() {
		return "Le code 2FA n'est pas valide. Le nombre maximum d'essais a été atteint. L'utilisateur sera déconnecté";
	}

	@Override
	protected String getUserNotAuthenticatedMessage_225() {
		return "L'utilisateur n'est pas correctement authentifié";
	}

	@Override
	protected String getUserCredentialsExpiredMessage_226() {
		return "Les informations d'identification de l'utilisateur ont expiré. Vous devez définir un nouveau mot de passe";
	}

	@Override
	protected String getUserLockedMessage_227() {
		return "L'utilisateur est bloqué et ne peut pas être utilisé. Contactez l'administrateur";
	}

	@Override
	protected String getFromJsonExceptionMessage_801() {
		return "Erreur lors de la lecture du JSON dans le serveur. S''il vous plaît, vérifiez que vous envoyez les données correctement: ''{0}''";
	}

	@Override
	protected String getToJsonExceptionMessage_802() {
		return "Erreur lors de la conversion de la réponse en JSON dans le serveur. S''il vous plaît, vérifiez que les données sont converties correctement: ''{0}''";
	}

	@Override
	protected String getSendMailExceptionMessage_803() {
		return "Erreur lors de l''envoi du courrier: {0}";
	}

	@Override
	protected String getWrongMailExceptionMessage_804() {
		return "L''adresse électronique ''{0}'' n'est pas valide";
	}

	@Override
	protected String getTimeoutExceptionMessage_805() {
		return "Timeout sur le Service Web: {0} secondes";
	}

	@Override
	protected String getNotAllowedExceptionMessage_806() {
		return "Vous ne disposez pas de permis suffisants";
	}

	@Override
	protected String getConcurrencyExceptionMessage_807() {
		return "Erreur de simultanéité des données: le registre a été modifié par un autre utilisateur";
	}

	@Override
	protected String getNewExceptionMessage_808() {
		return "Erreur lors de la création du modèle d'objet vide";
	}

	@Override
	protected String getGetExceptionMessage_809() {
		return "Erreur lors de l'obtention du registre";
	}

	@Override
	protected String getExistsExceptionMessage_810() {
		return "Erreur lors de la vérification de l'existence du registre";
	}

	@Override
	protected String getInsertExceptionMessage_811() {
		return "Erreur lors de l'insertion du nouveau registre";
	}

	@Override
	protected String getUpdateExceptionMessage_812() {
		return "Erreur lors de la mise à jour du registre";
	}

	@Override
	protected String getDeleteExceptionMessage_813() {
		return "Erreur lors de la suppression du registre";
	}

	@Override
	protected String getCopyRegistryExceptionMessage_814() {
		return "Erreur lors de la copie du registre";
	}

	@Override
	protected String getNoMailContentExceptionMessage_815() {
		return "Le contenu du courrier ne peut pas être vide";
	}

	@Override
	protected String getNoApiKeyExceptionMessage_816() {
		return "Aucune clé API fournie ou elle n'est pas valide";
	}

	@Override
	protected String getExportExceptionMessage_817() {
		return "Impossible d''exporter la liste: {0}";
	}

}
