package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;

/**
 * Event for the List action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ListEvent<T extends IDto> extends PuiEvent<SearchRequest> {

	private static final long serialVersionUID = 1L;

	private SearchResponse<T> response;

	public ListEvent(SearchRequest request, SearchResponse<T> response) {
		super(request);
		this.response = response;
	}

	public SearchResponse<T> getResponse() {
		return response;
	}

}
