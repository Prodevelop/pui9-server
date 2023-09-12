package es.prodevelop.pui9.elasticsearch.messages;

/**
 * Spanish Translation for PUI ElasticSearch component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticsearchResourceBundle_es extends PuiElasticsearchResourceBundle {

	@Override
	protected String getCountMessage_301() {
		return "Error al contar el número de registros en el índice \"{0}\": no está bien indexado debido a que tiene un número distinto de documentos indexados para cada idioma";
	}

	@Override
	protected String getCreateIndexMessage_302() {
		return "Error al crear el índice \"{0}\"";
	}

	@Override
	protected String getDeleteIndexMessage_303() {
		return "Error al borrar el índice \"{0}\"";
	}

	@Override
	protected String getExistsIndexMessage_304() {
		return "Error al comprobar la existencia del índice \"{0}\": las vistas traducidas deben tener un índice por cada idioma";
	}

	@Override
	protected String getInsertDocumentMessage_305() {
		return "Error al insertar un documento en el índice \"{0}\"";
	}

	@Override
	protected String getUpdateDocumentMessage_306() {
		return "Error al actualizar un documento en el índice \"{0}\"";
	}

	@Override
	protected String getDeleteDocumentMessage_307() {
		return "Error al borrar un documento en el índice \"{0}\"";
	}

	@Override
	protected String getSearchMessage_309() {
		return "Error al buscar: {0}";
	}

	@Override
	protected String getViewBlockedMessage_310() {
		return "La vista \"{0}\" está actualmente bloqueada para ElasticSearch";
	}

	@Override
	protected String getViewNotIndexableMessage_311() {
		return "La vista \"{0}\" está configurada como no indexable para ElasticSearch";
	}

}
