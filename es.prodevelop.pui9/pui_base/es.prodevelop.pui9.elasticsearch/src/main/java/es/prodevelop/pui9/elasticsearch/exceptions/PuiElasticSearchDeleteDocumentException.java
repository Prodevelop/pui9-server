package es.prodevelop.pui9.elasticsearch.exceptions;

/**
 * ElasticSearch exception when inseting a new document
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchDeleteDocumentException extends AbstractPuiElasticSearchException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 307;

	public PuiElasticSearchDeleteDocumentException(String index) {
		super(CODE, index);
	}

}
