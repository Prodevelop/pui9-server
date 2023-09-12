package es.prodevelop.pui9.elasticsearch.search;

import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.utils.IPuiObject;

/**
 * This class represents an item in the ElasticSearch search operation. It
 * contains the ElasticSearch Identifier and the associated {@link IViewDto}
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ESSearchResultItem<T extends IPuiObject> implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private String id;
	private T dto;

	/**
	 * Default constructor for search Items
	 * 
	 * @param id  The ElasticSearch identifier
	 * @param dto The element as representation
	 */
	public ESSearchResultItem(String id, T dto) {
		this.id = id;
		this.dto = dto;
	}

	/**
	 * Get the ElasticSearch identifier for this item
	 * 
	 * @return The ElasticSearch identifier for this item
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get the Item as {@link IViewDto} representation
	 * 
	 * @return The Item as {@link IViewDto} representation
	 */
	public T getDto() {
		return dto;
	}

}