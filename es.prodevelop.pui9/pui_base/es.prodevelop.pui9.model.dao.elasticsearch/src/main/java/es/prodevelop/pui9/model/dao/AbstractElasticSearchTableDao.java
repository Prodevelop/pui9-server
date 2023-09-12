package es.prodevelop.pui9.model.dao;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jooq.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.MaxAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.SumAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.ExistsRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.util.ObjectBuilder;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCreateIndexException;
import es.prodevelop.pui9.eventlistener.ThreadDaoEvents;
import es.prodevelop.pui9.eventlistener.event.DeleteDaoEvent;
import es.prodevelop.pui9.eventlistener.event.InsertDaoEvent;
import es.prodevelop.pui9.eventlistener.event.UpdateDaoEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;
import es.prodevelop.pui9.exceptions.PuiDaoAttributeLengthException;
import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiDaoNoNumericColumnException;
import es.prodevelop.pui9.exceptions.PuiDaoNullParametersException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiDaoSumException;
import es.prodevelop.pui9.exceptions.PuiDaoUpdateException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dao.elasticsearch.PuiElasticSearchManager;
import es.prodevelop.pui9.model.dao.elasticsearch.utils.PuiElasticSearchIndexUtils;
import es.prodevelop.pui9.model.dao.elasticsearch.utils.PuiElasticSearchQueryUtils;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.utils.PuiLanguage;
import jakarta.json.spi.JsonProvider;

public abstract class AbstractElasticSearchTableDao<TPK extends ITableDto, T extends TPK> extends AbstractDao<T>
		implements ITableDao<TPK, T> {

	private static final String MAX_VALUE_AGGREGATION = "maxValue";

	@Autowired
	protected PuiElasticSearchQueryUtils queryUtils;

	@Autowired
	private PuiElasticSearchIndexUtils indexUtils;

	@Autowired
	private ThreadDaoEvents threadDaoEvents;

	@Autowired
	private PuiElasticSearchManager elasticSearchManager;

	private JsonpMapper jsonpMapper;
	private JsonProvider jsonProvider;

	@PostConstruct
	private void postConstructElastic() throws PuiElasticSearchCreateIndexException {
		jsonpMapper = getClient()._jsonpMapper();
		jsonProvider = jsonpMapper.jsonProvider();

		if (!indexUtils.existIndex(dtoClass)) {
			boolean created = indexUtils.createIndex(dtoClass);
			if (!created) {
				throw new PuiElasticSearchCreateIndexException(dtoClass);
			}
		}
	}

	@Override
	public T insert(T dto) throws PuiDaoInsertException {
		if (dto == null) {
			return null;
		}

		List<T> list = bulkInsert(Collections.singletonList(dto));
		return !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public List<T> bulkInsert(List<T> dtoList) throws PuiDaoInsertException {
		if (dtoList == null) {
			return Collections.emptyList();
		}

		try {
			for (T dto : dtoList) {
				checkValues(dto);
			}
		} catch (PuiDaoNullParametersException | PuiDaoAttributeLengthException e) {
			throw new PuiDaoInsertException(e);
		}

		try {
			setAutoincrementableValues(dtoList);
		} catch (PuiDaoNoNumericColumnException e) {
			throw new PuiDaoInsertException(e);
		}

		BulkRequest.Builder br = new BulkRequest.Builder().refresh(Refresh.True);
		for (T dto : dtoList) {
			modifyBeforeInsert(dto);
			String id = String.valueOf(dto.createPk().hashCode());
			String json = GsonSingleton.getSingleton().getGson().toJson(dto);
			Reader reader = new StringReader(json);

			JsonData data = JsonData.from(jsonProvider.createParser(reader), jsonpMapper);
			br.operations(op -> op.index(idx -> idx.index(getIndexName()).id(id).document(data)));
		}

		BulkRequest request = br.build();
		BulkResponse response;
		try {
			response = getClient().bulk(request);
		} catch (ElasticsearchException | IOException e) {
			throw new PuiDaoInsertException(e, -1);
		}

		if (response.errors()) {
			for (BulkResponseItem item : response.items()) {
				if (item.error() != null) {
					logger.error(item.error().reason());
				}
			}
		} else {
			afterInsert(dtoList);
		}

		return dtoList;
	}

	protected void modifyBeforeInsert(T dto) {
		// do nothing
	}

	protected void afterInsert(List<T> dtoList) {
		dtoList.forEach(dto -> {
			if (daoRegistry.getAllTableDaoLang().contains(daoRegistry.getDaoFromDto(dtoClass))) {
				return;
			}
			threadDaoEvents.addEventType(new InsertDaoEvent(dto));
		});
	}

	@Override
	public T update(T dto) throws PuiDaoUpdateException {
		if (dto == null) {
			return null;
		}

		T oldDto;
		try {
			oldDto = findOne(dto.<TPK>createPk());
		} catch (PuiDaoFindException e) {
			throw new PuiDaoUpdateException(e);
		}

		List<T> list = Collections.singletonList(dto);
		list = innerBulkUpdate(list);

		if (!ObjectUtils.isEmpty(list)) {
			afterUpdate(oldDto, list);
		}

		return dto;
	}

	@Override
	public List<T> bulkUpdate(List<T> dtoList) throws PuiDaoUpdateException {
		if (dtoList == null) {
			return Collections.emptyList();
		}

		dtoList = innerBulkUpdate(dtoList);

		if (!ObjectUtils.isEmpty(dtoList)) {
			afterUpdate(null, dtoList);
		}

		return dtoList;
	}

	private List<T> innerBulkUpdate(List<T> dtoList) throws PuiDaoUpdateException {
		if (dtoList == null) {
			return Collections.emptyList();
		}

		try {
			for (T dto : dtoList) {
				checkValues(dto);
			}
		} catch (PuiDaoNullParametersException | PuiDaoAttributeLengthException e) {
			throw new PuiDaoUpdateException(e);
		}

		BulkRequest.Builder br = new BulkRequest.Builder().refresh(Refresh.True);
		for (T dto : dtoList) {
			modifyBeforeUpdate(dto);
			String id = String.valueOf(dto.createPk().hashCode());
			String json = GsonSingleton.getSingleton().getGson().toJson(dto);
			Reader reader = new StringReader(json);

			JsonData data = JsonData.from(jsonProvider.createParser(reader), jsonpMapper);
			br.operations(op -> op.update(upd -> upd.index(getIndexName()).id(id).action(a -> a.doc(data))));
		}

		BulkRequest request = br.build();
		BulkResponse response;
		try {
			response = getClient().bulk(request);
		} catch (ElasticsearchException | IOException e) {
			throw new PuiDaoUpdateException(e, -1);
		}

		if (response.errors()) {
			for (BulkResponseItem item : response.items()) {
				if (item.error() != null) {
					logger.error(item.error().reason());
				}
			}
		}

		return dtoList;
	}

	protected void modifyBeforeUpdate(T dto) {
		// do nothing
	}

	/**
	 * Perform some actions after updating the DTO. By default, the
	 * {@link UpdateDaoEvent} will be fired, and all the associated
	 * {@link PuiListener} will be executed
	 * 
	 * @param oldDto  The old DTO
	 * @param dtoList The updated DTO
	 */
	protected void afterUpdate(T oldDto, List<T> dtoList) {
		dtoList.forEach(dto -> {
			if (daoRegistry.getAllTableDaoLang().contains(daoRegistry.getDaoFromDto(dtoClass))) {
				return;
			}
			threadDaoEvents.addEventType(new UpdateDaoEvent(dto, oldDto));
		});
	}

	@Override
	public TPK patch(TPK dtoPk, Map<String, Object> fieldValuesMap) throws PuiDaoSaveException {
		if (dtoPk == null) {
			return null;
		}

		bulkPatch(Collections.singletonList(dtoPk), fieldValuesMap);

		return dtoPk;
	}

	@Override
	public void bulkPatch(List<TPK> dtoPkList, Map<String, Object> fieldValuesMap) throws PuiDaoUpdateException {
		if (ObjectUtils.isEmpty(dtoPkList)) {
			return;
		}

		List<TPK> newList = new ArrayList<>(dtoPkList);
		newList.removeIf(dtoPk -> !checkPkFields(dtoPk));

		if (ObjectUtils.isEmpty(newList)) {
			return;
		}

		Map<String, Object> columnValuesMap = convertFieldsToColumns(fieldValuesMap);
		if (columnValuesMap.isEmpty()) {
			return;
		}

		BulkRequest.Builder br = new BulkRequest.Builder().refresh(Refresh.True);
		for (TPK dtoPk : newList) {
			String id = String.valueOf(dtoPk.hashCode());
			String json = GsonSingleton.getSingleton().getGson().toJson(columnValuesMap);
			Reader reader = new StringReader(json);

			JsonData data = JsonData.from(jsonProvider.createParser(reader), jsonpMapper);
			br.operations(op -> op.update(upd -> upd.index(getIndexName()).id(id).action(a -> a.doc(data))));
		}

		BulkRequest request = br.build();
		BulkResponse response;
		try {
			response = getClient().bulk(request);
		} catch (ElasticsearchException | IOException e) {
			throw new PuiDaoUpdateException(e, -1);
		}

		if (response.errors()) {
			for (BulkResponseItem item : response.items()) {
				if (item.error() != null) {
					logger.error(item.error().reason());
				}
			}
		} else {
			afterPatch(newList, fieldValuesMap);
		}
	}

	/**
	 * Perform some actions after updating the DTO. By default, the
	 * {@link UpdateDaoEvent} will be fired, and all the associated
	 * {@link PuiListener} will be executed
	 * 
	 * @param oldDto  The old DTO
	 * @param dtoList The updated DTO
	 */
	protected void afterPatch(List<TPK> dtoPkList, Map<String, Object> fieldValuesMap) {
		dtoPkList.forEach(dtoPk -> {
			if (daoRegistry.getAllTableDaoLang().contains(daoRegistry.getDaoFromDto(dtoClass))) {
				return;
			}
			threadDaoEvents.addEventType(new UpdateDaoEvent(dtoPk, fieldValuesMap));
		});
	}

	@Override
	public TPK delete(TPK dtoPk) throws PuiDaoDeleteException {
		if (dtoPk == null) {
			return null;
		}

		bulkDelete(Collections.singletonList(dtoPk));

		return dtoPk;
	}

	@Override
	public List<TPK> bulkDelete(List<TPK> dtoPkList) throws PuiDaoDeleteException {
		if (ObjectUtils.isEmpty(dtoPkList)) {
			return Collections.emptyList();
		}

		List<TPK> newList = new ArrayList<>(dtoPkList);
		newList.removeIf(dtoPk -> !checkPkFields(dtoPk));

		if (ObjectUtils.isEmpty(newList)) {
			return Collections.emptyList();
		}

		BulkRequest.Builder br = new BulkRequest.Builder().refresh(Refresh.True);
		for (TPK dtoPk : newList) {
			String id = String.valueOf(dtoPk.hashCode());
			br.operations(op -> op.delete(d -> d.index(getIndexName()).id(id)));
		}

		BulkRequest request = br.build();
		BulkResponse response;
		try {
			response = getClient().bulk(request);
		} catch (ElasticsearchException | IOException e) {
			throw new PuiDaoDeleteException(e, -1);
		}

		if (response.errors()) {
			for (BulkResponseItem item : response.items()) {
				if (item.error() != null) {
					logger.error(item.error().reason());
				}
			}
		} else {
			afterDelete(newList);
		}

		return newList;
	}

	/**
	 * Perform some actions after deleting the DTO. By default, the
	 * {@link DeleteDaoEvent} will be fired, and all the associated
	 * {@link PuiListener} will be executed
	 * 
	 * @param dtoPkList The deleted DTO list
	 */
	protected void afterDelete(List<TPK> dtoPkList) {
		dtoPkList.forEach(dtoPk -> {
			if (daoRegistry.getAllTableDaoLang().contains(daoRegistry.getDaoFromDto(dtoClass))) {
				return;
			}
			threadDaoEvents.addEventType(new DeleteDaoEvent(dtoPk));
		});
	}

	@Override
	public void deleteAll() throws PuiDaoDeleteException {
		deleteWhere((FilterBuilder) null);
	}

	@Override
	public void deleteAll(PuiLanguage language) throws PuiDaoDeleteException {
		deleteAll();
	}

	@Override
	public void deleteWhere(FilterBuilder filterBuilder) throws PuiDaoDeleteException {
		DeleteByQueryRequest.Builder requestBuilder = new DeleteByQueryRequest.Builder();
		requestBuilder.index(getIndexName());
		if (filterBuilder != null) {
			ObjectBuilder<?> filtersBuilder = queryUtils.processFilters(dtoClass, filterBuilder.asFilterGroup());
			if (filtersBuilder != null) {
				co.elastic.clients.elasticsearch._types.query_dsl.Query query = ((QueryVariant) filtersBuilder.build())
						._toQuery();
				requestBuilder.query(query);
			} else {
				requestBuilder.query(QueryBuilders.bool().build()._toQuery());
			}
		} else {
			requestBuilder.query(QueryBuilders.bool().build()._toQuery());
		}

		try {
			getClient().deleteByQuery(requestBuilder.build());
		} catch (ElasticsearchException | IOException e) {
			throw new PuiDaoDeleteException(e, -1);
		}
	}

	@Override
	public boolean exists(TPK dtoPk) throws PuiDaoFindException {
		if (dtoPk == null) {
			return false;
		}

		boolean validPk = checkPkFields(dtoPk);
		if (!validPk) {
			return false;
		}

		String id = String.valueOf(dtoPk.hashCode());
		ExistsRequest request = ExistsRequest.of(er -> er.index(getIndexName()).id(id));
		try {
			return getClient().exists(request).value();
		} catch (ElasticsearchException | IOException e) {
			throw new PuiDaoFindException(e);
		}
	}

	@Override
	public T findOne(TPK dtoPk) throws PuiDaoFindException {
		if (dtoPk == null) {
			return null;
		}

		boolean validPk = checkPkFields(dtoPk);
		if (!validPk) {
			return null;
		}

		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		FilterBuilder filterBuilder = FilterBuilder.newAndFilter();
		for (Iterator<String> it = pkColumnNames.iterator(); it.hasNext();) {
			String next = it.next();
			try {
				Field pkField = mapPk.get(next);
				Object val = FieldUtils.readField(pkField, dtoPk, true);
				if (DtoRegistry.getStringFields(dtoClass).contains(next)) {
					filterBuilder.addEqualsExact(next, val.toString());
				} else {
					filterBuilder.addEquals(next, val);
				}
			} catch (Exception e) {
				// do nothing
			}
		}

		List<T> list = findWhere(filterBuilder);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public T findOne(TPK dtoPk, PuiLanguage language) throws PuiDaoFindException {
		return findOne(dtoPk);
	}

	@Override
	public T findOne(FilterBuilder filterBuilder) throws PuiDaoFindException {
		return findOne(filterBuilder, (OrderBuilder) null);
	}

	@Override
	public T findOne(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiDaoFindException {
		SearchRequest req = new SearchRequest();
		req.setDtoClass(dtoClass);
		req.setModel(DtoRegistry.getEntityFromDto(dtoClass));
		req.setPage(1);
		req.setRows(1);
		req.setQueryLang(null);
		req.setQueryText(null);
		req.setQueryFields(null);
		req.setQueryFlexible(false);
		req.setFilter(filterBuilder != null ? filterBuilder.asFilterGroup() : null);
		req.setOrder(orderBuilder != null ? orderBuilder.getOrders() : null);

		SearchResponse<T> response;
		try {
			response = findPaginated(req);
		} catch (PuiDaoListException e) {
			throw new PuiDaoFindException(e);
		}
		return !ObjectUtils.isEmpty(response.getData()) ? response.getData().get(0) : null;
	}

	@Override
	public T findOne(FilterBuilder filterBuilder, PuiLanguage language) throws PuiDaoFindException {
		return findOne(filterBuilder);
	}

	@Override
	public List<T> findAll() throws PuiDaoFindException {
		return findWhere((FilterBuilder) null, (OrderBuilder) null);
	}

	@Override
	public List<T> findAll(PuiLanguage language) throws PuiDaoFindException {
		return findAll();
	}

	@Override
	public List<T> findAll(OrderBuilder orderBuilder) throws PuiDaoFindException {
		return findWhere((FilterBuilder) null, orderBuilder);
	}

	@Override
	public List<T> findAll(OrderBuilder orderBuilder, PuiLanguage language) throws PuiDaoFindException {
		return findAll(orderBuilder);
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder) throws PuiDaoFindException {
		return findWhere(filterBuilder, (OrderBuilder) null);
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiDaoFindException {
		SearchRequest req = new SearchRequest();
		req.setDtoClass(dtoClass);
		req.setModel(DtoRegistry.getEntityFromDto(dtoClass));
		req.setPage(1);
		req.setRows(-1);
		req.setQueryLang(null);
		req.setQueryText(null);
		req.setQueryFields(null);
		req.setQueryFlexible(false);
		req.setFilter(filterBuilder != null ? filterBuilder.asFilterGroup() : null);
		req.setOrder(orderBuilder != null ? orderBuilder.getOrders() : null);

		SearchResponse<T> response;
		try {
			response = findPaginated(req);
		} catch (PuiDaoListException e) {
			throw new PuiDaoFindException(e);
		}
		return response.getData();
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder, PuiLanguage language) throws PuiDaoFindException {
		return findWhere(filterBuilder);
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiDaoFindException {
		return findWhere(filterBuilder, orderBuilder);
	}

	@Override
	public Long count() throws PuiDaoCountException {
		return count(null);
	}

	@Override
	public Long count(FilterBuilder filterBuilder) throws PuiDaoCountException {
		CountRequest.Builder requestBuilder = new CountRequest.Builder();
		requestBuilder.index(getIndexName());
		if (filterBuilder != null) {
			ObjectBuilder<?> filtersBuilder = queryUtils.processFilters(dtoClass, filterBuilder.asFilterGroup());
			if (filtersBuilder != null) {
				co.elastic.clients.elasticsearch._types.query_dsl.Query query = ((QueryVariant) filtersBuilder.build())
						._toQuery();
				requestBuilder.query(query);
			}
		}

		try {
			return getClient().count(requestBuilder.build()).count();
		} catch (ElasticsearchException | IOException e) {
			throw new PuiDaoCountException(e);
		}
	}

	@Override
	public Long count(String column, boolean distinct, FilterBuilder filterBuilder) throws PuiDaoCountException {
		return count(filterBuilder);
	}

	@Override
	public BigDecimal sum(String column) throws PuiDaoSumException {
		return sum(column, null);
	}

	@Override
	public BigDecimal sum(String column, FilterBuilder filterBuilder) throws PuiDaoSumException {
		if (!DtoRegistry.getNumericFields(dtoClass).contains(column)
				&& !DtoRegistry.getFloatingFields(dtoClass).contains(column)) {
			return null;
		}

		String fieldName = DtoRegistry.getFieldNameFromColumnName(dtoClass, column);

		co.elastic.clients.elasticsearch.core.SearchRequest request = co.elastic.clients.elasticsearch.core.SearchRequest
				.of(sr -> sr.aggregations(column,
						Aggregation.of(a -> a.sum(SumAggregation.of(sa -> sa.field(fieldName))))));

		double val;
		try {
			co.elastic.clients.elasticsearch.core.SearchResponse<Void> response = getClient().search(request,
					Void.class);
			val = response.aggregations().get(column).sum().value();
		} catch (ElasticsearchException | IOException e) {
			val = 0;
		}

		return BigDecimal.valueOf(val);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Number> N getNextValue(String columnName, FilterBuilder filterBuilder)
			throws PuiDaoNoNumericColumnException {
		N value = getMaxValue(columnName, filterBuilder);

		if (value instanceof BigDecimal) {
			value = (N) ((BigDecimal) value).add(BigDecimal.ONE);
		} else if (value instanceof Integer) {
			value = (N) Integer.valueOf(((Integer) value) + 1);
		} else if (value instanceof Long) {
			value = (N) Long.valueOf(((Long) value) + 1L);
		} else if (value instanceof Double) {
			value = (N) Double.valueOf(((Double) value) + 1);
		} else if (value instanceof Float) {
			value = (N) Float.valueOf(((Float) value) + 1);
		} else if (value instanceof BigInteger) {
			value = (N) ((BigInteger) value).add(BigInteger.ONE);
		}

		return value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Number> N getMaxValue(String columnName, FilterBuilder filterBuilder)
			throws PuiDaoNoNumericColumnException {
		String fieldName = DtoRegistry.getFieldNameFromColumnName(dtoClass, columnName);

		co.elastic.clients.elasticsearch.core.SearchRequest request = co.elastic.clients.elasticsearch.core.SearchRequest
				.of(sr -> sr.aggregations(MAX_VALUE_AGGREGATION,
						Aggregation.of(a -> a.max(MaxAggregation.of(ma -> ma.field(fieldName))))));

		Object val = null;
		try {
			co.elastic.clients.elasticsearch.core.SearchResponse<Void> response = getClient().search(request,
					Void.class);
			val = response.aggregations().get(MAX_VALUE_AGGREGATION).max().value();
		} catch (ElasticsearchException | IOException e1) {
			val = 0;
		}

		Field field = DtoRegistry.getJavaFieldFromFieldName(dtoClass, fieldName);

		N value = null;
		// if no values, instantiate a new one
		try {
			BigDecimal bd = new BigDecimal(val.toString());
			value = (N) field.getType().getConstructor(String.class).newInstance(bd.toString());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			return null;
		}

		return value;
	}

	@Override
	public void executePaginagedOperation(SearchRequest req, Consumer<T> consumer, Consumer<List<T>> bulkConsumer) {
		req.setPage(SearchRequest.DEFAULT_PAGE);
		req.setPerformCount(false);

		while (true) {
			List<T> list;
			try {
				list = findPaginated(req).getData();
			} catch (PuiDaoListException e) {
				list = Collections.emptyList();
			}

			if (ObjectUtils.isEmpty(list)) {
				break;
			}

			if (consumer != null) {
				for (T obj : list) {
					consumer.accept(obj);
				}
			} else if (bulkConsumer != null) {
				bulkConsumer.accept(list);
			}

			req.setPage(req.getPage() + 1);
		}
	}

	@Override
	public SearchResponse<T> findPaginated(SearchRequest req) throws PuiDaoListException {
		req.setDtoClass(dtoClass);
		co.elastic.clients.elasticsearch.core.SearchRequest request = queryUtils.buildQuery(req);

		try {
			co.elastic.clients.elasticsearch.core.SearchResponse<T> response = getClient().search(request, dtoClass);

			SearchResponse<T> searchResponse = new SearchResponse<>();
			searchResponse.setCurrentPage(req.getPage());
			searchResponse.setCurrentRecords(response.hits().hits().size());
			searchResponse.setTotalRecords(response.hits().total().value());

			// remove inner hits due to collapsing by distinct values on a column
			for (Hit<T> h : response.hits().hits()) {
				if (!ObjectUtils.isEmpty(h.innerHits())) {
					h.innerHits().forEach((s, ihr) -> {
						long innerHitTotal = ihr.hits().total().value();
						searchResponse.setTotalRecords(searchResponse.getTotalRecords() - innerHitTotal + 1);
					});
				}
			}

			searchResponse.setTotalPages(searchResponse.getTotalRecords() / req.getRows());
			if (searchResponse.getTotalRecords() % req.getRows() > 0) {
				searchResponse.setTotalPages(searchResponse.getTotalPages() + 1);
			}
			searchResponse.setData(response.hits().hits().stream().map(Hit<T>::source).collect(Collectors.toList()));

			return searchResponse;
		} catch (ElasticsearchException | IOException e) {
			throw new PuiDaoListException(e);
		}
	}

	@Override
	public void executeQuery(Query query) throws PuiDaoFindException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> executeCustomQuery(Query query) throws PuiDaoFindException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> executeCustomQueryWithParameters(Query query, List<Object> parameters) throws PuiDaoFindException {
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends ITableDao<TPK, T>> getDaoClass() {
		return (Class<? extends ITableDao<TPK, T>>) super.getDaoClass();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<TPK> getDtoPkClass() {
		if (ITableDto.class.isAssignableFrom(dtoClass)) {
			// is a table
			return (Class<TPK>) dtoClass.getSuperclass();
		} else {
			// is a view
			return (Class<TPK>) dtoClass;
		}
	}

	@Override
	public ITableDao<ITableDto, ITableDto> getTableTranslationDao() {
		return null;
	}

	@Override
	protected void setAutoincrementableValues(List<T> dtoList) throws PuiDaoNoNumericColumnException {
		List<String> fieldNames = new ArrayList<>();
		fieldNames.addAll(DtoRegistry.getAutoincrementableFieldNames(dtoClass));
		fieldNames.addAll(DtoRegistry.getSequenceFields(dtoClass));

		for (String fieldName : fieldNames) {
			try {
				Field field = DtoRegistry.getJavaFieldFromFieldName(getDtoClass(), fieldName);

				String columnName = DtoRegistry.getColumnNameFromFieldName(getDtoClass(), fieldName);
				FilterBuilder filterBuilder = getAutoincrementableColumnFilter(dtoList.get(0), columnName);

				Number nextId = getNextValue(columnName, filterBuilder);

				for (T dto : dtoList) {
					if (FieldUtils.readField(field, dto, true) != null) {
						continue;
					}

					FieldUtils.writeField(field, dto, nextId, true);

					if (nextId instanceof Long) {
						nextId = nextId.longValue() + 1;
					} else if (nextId instanceof Integer) {
						nextId = nextId.intValue() + 1;
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do nothing
			}
		}
	}

	protected String getIndexName() {
		List<String> indices = indexUtils.getIndicesForDto(dtoClass);
		if (ObjectUtils.isEmpty(indices)) {
			throw new IllegalArgumentException(new Exception("No index for DTO " + dtoClass.getSimpleName()));
		}
		return indices.get(0);
	}

	/**
	 * Check that all the fields that belong to the PK have value
	 * 
	 * @param dtoPk The DTO PK to check
	 * @return true if all's ok; false if any pk field is null
	 */
	private boolean checkPkFields(TPK dtoPk) {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		for (String pkColumnName : pkColumnNames) {
			try {
				Field pkField = mapPk.get(pkColumnName);
				if (pkField == null) {
					return false;
				}

				Object val = FieldUtils.readField(pkField, dtoPk, true);
				if (val == null) {
					// PK attribute should always have value
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}

	protected ElasticsearchClient getClient() {
		return elasticSearchManager.getClient();
	}

}
