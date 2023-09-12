package es.prodevelop.pui9.elasticsearch.messages;

import java.util.LinkedHashMap;
import java.util.Map;

import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCountException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCreateIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchDeleteDocumentException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchDeleteIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchExistsIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchInsertDocumentException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchSearchException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchUpdateDocumentException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchViewBlockedException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchViewNotIndexableException;
import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * ElasticSearch component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiElasticsearchResourceBundle extends AbstractPuiListResourceBundle {

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new LinkedHashMap<>();
		messages.put(PuiElasticSearchCountException.CODE, getCountMessage_301());
		messages.put(PuiElasticSearchCreateIndexException.CODE, getCreateIndexMessage_302());
		messages.put(PuiElasticSearchDeleteIndexException.CODE, getDeleteIndexMessage_303());
		messages.put(PuiElasticSearchExistsIndexException.CODE, getExistsIndexMessage_304());
		messages.put(PuiElasticSearchInsertDocumentException.CODE, getInsertDocumentMessage_305());
		messages.put(PuiElasticSearchUpdateDocumentException.CODE, getUpdateDocumentMessage_306());
		messages.put(PuiElasticSearchDeleteDocumentException.CODE, getDeleteDocumentMessage_307());
		messages.put(PuiElasticSearchSearchException.CODE, getSearchMessage_309());
		messages.put(PuiElasticSearchViewBlockedException.CODE, getViewBlockedMessage_310());
		messages.put(PuiElasticSearchViewNotIndexableException.CODE, getViewNotIndexableMessage_311());

		return messages;
	}

	protected abstract String getCountMessage_301();

	protected abstract String getCreateIndexMessage_302();

	protected abstract String getDeleteIndexMessage_303();

	protected abstract String getExistsIndexMessage_304();

	protected abstract String getInsertDocumentMessage_305();

	protected abstract String getUpdateDocumentMessage_306();

	protected abstract String getDeleteDocumentMessage_307();

	protected abstract String getSearchMessage_309();

	protected abstract String getViewBlockedMessage_310();

	protected abstract String getViewNotIndexableMessage_311();

}
