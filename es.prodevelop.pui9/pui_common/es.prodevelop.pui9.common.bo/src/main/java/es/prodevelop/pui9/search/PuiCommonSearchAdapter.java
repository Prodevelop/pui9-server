package es.prodevelop.pui9.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;

/**
 * This search implementation uses the tables of PUI9 like PUI_MODEL to
 * configure the search by adding some filters and configuration before
 * performing the search method to retrieve the data list
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Primary
@Component
public class PuiCommonSearchAdapter implements IPuiSearchAdapter {

	@Autowired
	private IPuiModelService modelService;

	@Override
	public <TYPE> SearchResponse<TYPE> search(SearchRequest req) throws PuiServiceGetException {
		return modelService.search(req);
	}

}
