package es.prodevelop.pui9.elasticsearch.services;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.util.ObjectBuilder;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchSearchException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchViewBlockedException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchViewNotIndexableException;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.elasticsearch.search.ESSearchResult;
import es.prodevelop.pui9.elasticsearch.search.ESSearchResultItem;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchSearchingService;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.elasticsearch.PuiElasticSearchManager;
import es.prodevelop.pui9.model.dao.elasticsearch.utils.PuiElasticSearchIndexUtils;
import es.prodevelop.pui9.model.dao.elasticsearch.utils.PuiElasticSearchQueryUtils;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.order.OrderDirection;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

/**
 * Implementation for the API to manage the Searchs for ElasticSaerch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticSearchSearchingService implements IPuiElasticSearchSearchingService {

	@Autowired
	private IPuiElasticSearchEnablement puiElasticSearchEnablement;

	@Autowired
	private PuiElasticSearchQueryUtils queryUtils;

	@Autowired
	private PuiElasticSearchManager puiElasticSearchManager;

	@Autowired
	private PuiElasticSearchIndexUtils indexUtils;

	@Override
	public <V extends IViewDto> Long count(Class<V> dtoClass, FilterBuilder filterBuilder)
			throws PuiElasticSearchSearchException {
		String index = indexUtils.getIndexForLanguage(dtoClass, PuiLanguageUtils.getDefaultLanguage());
		CountRequest.Builder requestBuilder = new CountRequest.Builder();
		requestBuilder.index(index);

		if (filterBuilder != null) {
			ObjectBuilder<?> filtersBuilder = queryUtils.processFilters(dtoClass, filterBuilder.asFilterGroup());
			if (filtersBuilder != null) {
				requestBuilder.query(((QueryVariant) filtersBuilder.build())._toQuery());
			}
		}

		try {
			CountResponse response = getClient().count(requestBuilder.build());
			return response.count();
		} catch (ElasticsearchException | IOException e) {
			throw new PuiElasticSearchSearchException(e.getMessage());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V extends IViewDto, N extends Number> N getMaxValue(Class<V> dtoClass, String column,
			FilterBuilder filterBuilder) throws PuiElasticSearchSearchException {
		OrderBuilder orderBuilder = OrderBuilder.newOrder(Order.newOrder(column, OrderDirection.desc));
		ESSearchResultItem<V> item = findOne(dtoClass, filterBuilder, orderBuilder, null);

		N value = null;
		Field field = DtoRegistry.getJavaFieldFromColumnName(dtoClass, column);
		if (item != null) {
			Object dto = item.getDto();
			try {
				value = (N) FieldUtils.readField(field, dto, true);
			} catch (Exception e) {
				value = null;
			}
		}

		if (value == null) {
			// if no values, instantiate a new one
			try {
				value = (N) field.getType().getConstructor(String.class).newInstance("0");
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				return null;
			}
		}

		return value;
	}

	@Override
	public <V extends IViewDto> ESSearchResultItem<V> findOne(Class<V> dtoClass, FilterBuilder filterBuilder,
			OrderBuilder orderBuilder, PuiLanguage language) throws PuiElasticSearchSearchException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			throw new PuiElasticSearchSearchException("NullView");
		}

		SearchRequest req = new SearchRequest();
		req.setDtoClass(dtoClass);
		req.setModel(DtoRegistry.getEntityFromDto(dtoClass));
		req.setPage(1);
		req.setRows(1);
		req.setQueryLang(language != null ? language.getIsocode() : null);
		req.setQueryText(null);
		req.setQueryFields(null);
		req.setQueryFlexible(false);
		req.setFilter(filterBuilder != null ? filterBuilder.asFilterGroup() : null);
		req.setOrder(orderBuilder != null ? orderBuilder.getOrders() : null);

		ESSearchResult<V> result = doSearch(req);
		return !ObjectUtils.isEmpty(result.getItems()) ? result.getItems().get(0) : null;
	}

	@Override
	public <V extends IViewDto> List<ESSearchResultItem<V>> findMultiple(Class<V> dtoClass, FilterBuilder filterBuilder,
			OrderBuilder orderBuilder, PuiLanguage language) throws PuiElasticSearchSearchException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			throw new PuiElasticSearchSearchException("NullView");
		}

		SearchRequest req = new SearchRequest();
		req.setDtoClass(dtoClass);
		req.setModel(DtoRegistry.getEntityFromDto(dtoClass));
		req.setPage(1);
		req.setRows(SearchRequest.NUM_MAX_ROWS);
		req.setQueryLang(language != null ? language.getIsocode() : null);
		req.setQueryText(null);
		req.setQueryFields(null);
		req.setQueryFlexible(false);
		req.setFilter(filterBuilder != null ? filterBuilder.asFilterGroup() : null);
		req.setOrder(orderBuilder != null ? orderBuilder.getOrders() : null);

		ESSearchResult<V> result = doSearch(req);
		return result.getItems();
	}

	@Override
	public <V extends IViewDto> SearchResponse<V> findPaginated(SearchRequest req)
			throws PuiElasticSearchSearchException {
		ESSearchResult<V> result = doSearch(req);

		SearchResponse<V> res = new SearchResponse<>();
		res.setCurrentPage(req.getPage());
		res.setCurrentRecords(result.getDtoList().size());
		res.setTotalRecords(result.getTotal());
		res.setTotalPages(result.getTotal() / req.getRows());
		if (result.getTotal() % req.getRows() > 0) {
			res.setTotalPages(res.getTotalPages() + 1);
		}
		res.setData(result.getDtoList());
		res.setSumData(result.getSumData());

		return res;
	}

	/**
	 * Executes the search over the ElasticSearch server
	 * 
	 * @param req The search request
	 * @return The result of the search
	 * @throws PuiElasticSearchSearchException If an error occurs while searching
	 *                                         the documents that fits the given
	 *                                         filter
	 */
	@SuppressWarnings("unchecked")
	private <V extends IViewDto> ESSearchResult<V> doSearch(SearchRequest req) throws PuiElasticSearchSearchException {
		Class<V> dtoClass = (Class<V>) req.getDtoClass();
		if (dtoClass == null) {
			throw new PuiElasticSearchSearchException("Doesn't exist the DTO for the model " + req.getModel());
		}

		if (puiElasticSearchEnablement.isViewBlocked(dtoClass)) {
			throw new PuiElasticSearchViewBlockedException(dtoClass.getSimpleName());
		}

		if (!puiElasticSearchEnablement.isViewIndexable(dtoClass)) {
			throw new PuiElasticSearchViewNotIndexableException(dtoClass.getSimpleName());
		}

		co.elastic.clients.elasticsearch.core.SearchRequest request = queryUtils.buildQuery(req);

		// Parse the response
		co.elastic.clients.elasticsearch.core.SearchResponse<V> response;
		try {
			response = getClient().search(request, dtoClass);
		} catch (ElasticsearchException e) {
			throw new PuiElasticSearchSearchException(e.response().error().metadata().toString());
		} catch (IOException e) {
			throw new PuiElasticSearchSearchException(e.getMessage());
		}

		ESSearchResult<V> result = new ESSearchResult<>(response.hits().total().value(), response.took());
		for (Hit<V> h : response.hits().hits()) {
			result.addItem(new ESSearchResultItem<>(h.id(), h.source()));

			// remove inner hits due to collapsing by distinct values on a column
			if (!ObjectUtils.isEmpty(h.innerHits())) {
				h.innerHits().forEach((s, ihr) -> {
					long innerHitTotal = ihr.hits().total().value();
					result.setTotal(result.getTotal() - innerHitTotal + 1);
				});
			}
		}

		for (String sumColumn : req.getSumColumns()) {
			if (!DtoRegistry.getNumericFields(dtoClass).contains(sumColumn)
					&& !DtoRegistry.getFloatingFields(dtoClass).contains(sumColumn)) {
				continue;
			}

			double val = response.aggregations().get(sumColumn).sum().value();
			BigDecimal value = BigDecimal.valueOf(val);
			result.addSumData(sumColumn, value);
		}

		return result;
	}

	private ElasticsearchClient getClient() {
		return puiElasticSearchManager.getClient();
	}

}
