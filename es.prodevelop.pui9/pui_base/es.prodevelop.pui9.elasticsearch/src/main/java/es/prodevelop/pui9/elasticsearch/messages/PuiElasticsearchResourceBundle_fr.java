package es.prodevelop.pui9.elasticsearch.messages;

/**
 * French Translation for PUI ElasticSearch component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticsearchResourceBundle_fr extends PuiElasticsearchResourceBundle {

	@Override
	protected String getCountMessage_301() {
		return "Erreur en comptant le nombre de registres dans l''index \"{0}\": il n''est pas bien indexé en raison du nombre différent de documents indexés pour chaque langue";
	}

	@Override
	protected String getCreateIndexMessage_302() {
		return "Erreur lors de la création de l''index \"{0}\"";
	}

	@Override
	protected String getDeleteIndexMessage_303() {
		return "Erreur lors de la suppression de l''index \"{0}\"";
	}

	@Override
	protected String getExistsIndexMessage_304() {
		return "Erreur lors de la vérification de l''existence de l''index \"{0}\": les vues traduites devraient avoir un index pour chaque langue";
	}

	@Override
	protected String getInsertDocumentMessage_305() {
		return "Erreur lors de l''insertion d''un document dans l''index \"{0}\"";
	}

	@Override
	protected String getUpdateDocumentMessage_306() {
		return "Erreur lors de la mise à jour d''un document dans l''index \"{0}\"";
	}

	@Override
	protected String getDeleteDocumentMessage_307() {
		return "Erreur lors de la suppression d''un document dans l''index \"{0}\"";
	}

	@Override
	protected String getSearchMessage_309() {
		return "Erreur lors de la recherche : {0}";
	}

	@Override
	protected String getViewBlockedMessage_310() {
		return "La vue \"{0}\" est actuellement bloquée pour ElasticSearch";
	}

	@Override
	protected String getViewNotIndexableMessage_311() {
		return "La vue \"{0}\" est configurée comme non indexable pour ElasticSearch";
	}

}
