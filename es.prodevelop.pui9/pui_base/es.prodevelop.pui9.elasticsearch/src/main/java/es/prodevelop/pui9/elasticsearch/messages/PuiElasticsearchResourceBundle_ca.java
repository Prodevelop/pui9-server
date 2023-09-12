package es.prodevelop.pui9.elasticsearch.messages;

/**
 * Catalan Translation for PUI ElasticSearch component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticsearchResourceBundle_ca extends PuiElasticsearchResourceBundle {

	@Override
	protected String getCountMessage_301() {
		return "Error al contar el número de registres en l''índex \"{0}\": no està indexat correctament ja que té un número distint de documents indexats per a cada idioma";
	}

	@Override
	protected String getCreateIndexMessage_302() {
		return "Error al crear l''índex \"{0}\"";
	}

	@Override
	protected String getDeleteIndexMessage_303() {
		return "Error a l''esborrar l''índex \"{0}\"";
	}

	@Override
	protected String getExistsIndexMessage_304() {
		return "Error al comprovar l''existència de l''índex \"{0}\": les vistes traduïdes deuen tindre un índex per a cada idioma";
	}

	@Override
	protected String getInsertDocumentMessage_305() {
		return "Error al inserir un document en l''índex \"{0}\"";
	}

	@Override
	protected String getUpdateDocumentMessage_306() {
		return "Error a l''actualitzar un document en l''índex \"{0}\"";
	}

	@Override
	protected String getDeleteDocumentMessage_307() {
		return "Error a l''esborrar un document en l''índex \"{0}\"";
	}

	@Override
	protected String getSearchMessage_309() {
		return "Error al buscar: {0}";
	}

	@Override
	protected String getViewBlockedMessage_310() {
		return "La vista \"{0}\" està actualment bloquejada per a ElasticSearch";
	}

	@Override
	protected String getViewNotIndexableMessage_311() {
		return "La vista \"{0}\" està configurada com a no indexable per a ElasticSearch";
	}

}
