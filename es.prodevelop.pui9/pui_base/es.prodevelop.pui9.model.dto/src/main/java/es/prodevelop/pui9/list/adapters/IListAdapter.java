package es.prodevelop.pui9.list.adapters;

import javax.annotation.PostConstruct;

import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.filter.rules.IsNullRule;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.search.SearchRequest;

/**
 * This adapter is used to configure the search with parameters in the filters.
 * Each parameter in the filter is represented by the {@link #SEARCH_PARAMETER}
 * character.<br>
 * Subclasses must implement {@link #getFixedFilterParameters(SearchRequest)}
 * and return an String with all the parameters to be substituted, each one
 * separated with the {@link #SEPARATOR} character
 * 
 * @param <T> The {@link IDto} type of the adapter
 *
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IListAdapter<T extends IDto> {

	String SEARCH_PARAMETER = "?";
	char SEPARATOR = '|';

	FilterGroup EMPTY_FILTER = FilterGroup.andGroup().addRule(IsNullRule.of(SEARCH_PARAMETER));

	@PostConstruct
	default void postConstruct() {
		ListAdapterRegistry.getSingleton().registerListAdapter(this);
	}

	/**
	 * Return the FixedFilterParameter attribute from searchDto parameter. This is
	 * an String with all the values that should be substituted from the filter.
	 * Each one separated with the {@link #SEPARATOR} character. Each value will be
	 * replaced for every {@link #SEARCH_PARAMETER} character in the filter
	 * <p>
	 * In case a whole filter would be returned, it must be a {@link FilterGroup} as
	 * JSON
	 * 
	 * @param req The request info
	 * @return An string representing a Filter in JSON format
	 */
	String getFixedFilterParameters(SearchRequest req);

}
