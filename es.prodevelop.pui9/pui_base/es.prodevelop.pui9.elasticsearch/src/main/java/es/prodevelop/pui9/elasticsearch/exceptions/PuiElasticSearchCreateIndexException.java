package es.prodevelop.pui9.elasticsearch.exceptions;

import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * ElasticSearch exception when creating a new index in the server
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchCreateIndexException extends AbstractPuiElasticSearchException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 302;

	public PuiElasticSearchCreateIndexException(String index) {
		super(CODE, index);
	}

	public PuiElasticSearchCreateIndexException(Class<? extends IDto> dtoClass) {
		super(CODE, dtoClass.getSimpleName());
	}

}
