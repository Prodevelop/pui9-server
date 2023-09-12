package es.prodevelop.pui9.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiApiKey;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.search.IPuiSearchAdapter;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller allows to search registries over all the available models,
 * using directly the Service of the DAO
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Tag(name = "PUI Search")
@RequestMapping("/puisearch")
public class PuiSearchController extends AbstractPuiController {

	@Autowired
	private IPuiSearchAdapter puiSearchAdapter;

	/**
	 * Executes a complex loup search
	 * 
	 * @param req The configuration of the search
	 * @return The list of registries that accomplish the conditions of the request
	 * @throws PuiServiceGetException If an error is throws in the search
	 */
	@PuiApiKey
	@Operation(summary = "Generic view search (loupe)", description = "List all the elements that accomplish the given condition")
	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public <TYPE> SearchResponse<TYPE> search(
			@Parameter(description = "The information for the search", required = true) @RequestBody SearchRequest req)
			throws PuiServiceGetException {
		req.setFromClient(true);
		return puiSearchAdapter.search(req);
	}

}
