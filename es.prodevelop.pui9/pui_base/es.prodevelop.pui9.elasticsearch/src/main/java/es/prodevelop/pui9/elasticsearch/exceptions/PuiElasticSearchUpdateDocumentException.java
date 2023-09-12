package es.prodevelop.pui9.elasticsearch.exceptions;

/**
 * ElasticSearch exception when inseting a new document
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchUpdateDocumentException extends AbstractPuiElasticSearchException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 306;

	public PuiElasticSearchUpdateDocumentException(String index) {
		super(CODE, index);
	}

}
