package es.prodevelop.pui9.elasticsearch.services;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.SelectJoinStep;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiElasticsearchViewsDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViews;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.db.helpers.IDatabaseHelper;
import es.prodevelop.pui9.elasticsearch.analysis.JoinTableDef;
import es.prodevelop.pui9.elasticsearch.analysis.JoinType;
import es.prodevelop.pui9.elasticsearch.analysis.PuiElasticsearchViewsAnalysis;
import es.prodevelop.pui9.elasticsearch.enums.DocumentOperationType;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCreateIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchDeleteDocumentException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchInsertDocumentException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchSearchException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchUpdateDocumentException;
import es.prodevelop.pui9.elasticsearch.search.ESSearchResultItem;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchSearchingService;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dao.elasticsearch.PuiElasticSearchManager;
import es.prodevelop.pui9.model.dao.elasticsearch.utils.PuiElasticSearchIndexUtils;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.spring.configuration.AbstractAppSpringConfiguration;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import jakarta.json.spi.JsonProvider;

/**
 * Implementation for the API to manage Documents for ElasticSaerch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticSearchDocumentService {

	private static final String NO_LANG = "ZZ";
	private static final String PK_SEPARATOR = "#";

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private PuiElasticSearchIndexService indexService;

	@Autowired
	private IPuiElasticSearchSearchingService searchingService;

	@Autowired
	private PuiElasticsearchViewsAnalysis elasticViewsAnalysis;

	@Autowired
	private IPuiElasticsearchViewsDao elasticsearchViewsDao;

	@Autowired
	private IDatabaseHelper dbHelper;

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private PuiElasticSearchManager puiElasticSearchManager;

	@Autowired
	private PuiElasticSearchIndexUtils indexUtils;

	/**
	 * Set the value in the {@link AbstractAppSpringConfiguration} class of your
	 * project
	 */
	@Autowired
	@Qualifier("elasticsearchAppname")
	private String elasticsearchAppname;

	private final Map<String, List<String>> cacheViewIds = new LinkedHashMap<>();

	@PostConstruct
	private void postConstruct() {
		refreshViewIds();
	}

	/**
	 * Refresh the cache of index and indexable views
	 */
	public void refresh() {
		indexUtils.refresh();
		refreshViewIds();
	}

	/**
	 * Inserts the given DTO into ElasticSearch
	 * 
	 * @param dto      The Table DTO to be indexed
	 * @param view     The view to be indexed, where the DTO participates
	 * @param pkFilter The PK filter of the given DTO
	 * 
	 * @throws PuiElasticSearchInsertDocumentException If any error occurs while
	 *                                                 inserting the document
	 */
	public void insertDocument(ITableDto dtoPk, String view, FilterGroup pkFilter)
			throws PuiElasticSearchInsertDocumentException {
		try {
			innerInsertOrUpdate(dtoPk, view, pkFilter, DocumentOperationType.insert);
		} catch (PuiElasticSearchUpdateDocumentException e) {
			// do nothing, it's impossible that this exception were throws
		}
	}

	/**
	 * Updates the given DTO into ElasticSearch
	 * 
	 * @param dto      The Table DTO to be indexed
	 * @param view     The view to be indexed, where the DTO participates
	 * @param pkFilter The PK filter of the given DTO
	 * 
	 * @throws PuiElasticSearchUpdateDocumentException If any error occurs while
	 *                                                 updating the document
	 */
	public void updateDocument(ITableDto dtoPk, String view, FilterGroup pkFilter)
			throws PuiElasticSearchUpdateDocumentException {
		try {
			innerInsertOrUpdate(dtoPk, view, pkFilter, DocumentOperationType.update);
		} catch (PuiElasticSearchInsertDocumentException e) {
			// do nothing, it's impossible that this exception were throws
		}
	}

	/**
	 * Deletes the given DTO from ElasticSearch
	 * 
	 * @param dto      The Table DTO to be indexed
	 * @param view     The view to be indexed, where the DTO participates
	 * @param pkFilter The PK filter of the given DTO
	 * 
	 * @throws PuiElasticSearchDeleteDocumentException If any error occurs while
	 *                                                 deleting the document
	 */
	public void deleteDocument(ITableDto dtoPk, String view, FilterGroup pkFilter)
			throws PuiElasticSearchDeleteDocumentException {
		innerDeleteDocument(dtoPk, view, pkFilter);
	}

	/**
	 * Inserts the given DTO list into ElasticSearch. Ensure that the list of
	 * registries are not indexed
	 * 
	 * @param dtoList  The List of View DTO to be inserted
	 * @param language The language of the DTO to choose the correct index
	 */
	public void bulkInsertDocument(List<IViewDto> dtoList, PuiLanguage language) {
		if (ObjectUtils.isEmpty(dtoList)) {
			return;
		}

		Class<? extends IViewDto> viewDtoClass = dtoList.get(0).getClass();
		if (INullView.class.isAssignableFrom(viewDtoClass)) {
			return;
		}

		if (!indexService.existsIndex(viewDtoClass, language)) {
			try {
				indexService.createIndex(viewDtoClass, language);
			} catch (PuiElasticSearchCreateIndexException e) {
				return;
			}
		}

		String index = indexUtils.getIndexForLanguage(viewDtoClass, language);

		JsonpMapper jsonpMapper = getClient()._jsonpMapper();
		JsonProvider jsonProvider = getClient()._jsonpMapper().jsonProvider();

		BulkRequest.Builder br = new BulkRequest.Builder().refresh(Refresh.True);

		for (IViewDto dto : dtoList) {
			String id = getIdForDto(dto);
			String json = GsonSingleton.getSingleton().getGson().toJson(dto);
			Reader reader = new StringReader(json);

			JsonData data = JsonData.from(jsonProvider.createParser(reader), jsonpMapper);
			br.operations(op -> op.index(idx -> idx.index(index).id(id).document(data)));
		}

		BulkRequest request = br.build();
		if (ObjectUtils.isEmpty(request.operations())) {
			return;
		}

		BulkResponse response;
		try {
			response = getClient().bulk(request);
		} catch (ElasticsearchException | IOException e) {
			logger.error(e.getMessage(), e);
			return;
		}

		if (response.errors()) {
			for (BulkResponseItem item : response.items()) {
				if (item.error() != null) {
					logger.error(item.error().reason());
				}
			}
		}
	}

	private void innerInsertOrUpdate(ITableDto dtoPk, String view, FilterGroup pkFilter, DocumentOperationType opType)
			throws PuiElasticSearchInsertDocumentException, PuiElasticSearchUpdateDocumentException {
		Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
		if (viewDtoClass == null) {
			return;
		}

		String tableName = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(dtoPk.getClass()));
		List<LinkedList<JoinTableDef>> tableOrderList = elasticViewsAnalysis.getTableOrder(tableName, view);

		JsonpMapper jsonpMapper = getClient()._jsonpMapper();
		JsonProvider jsonProvider = jsonpMapper.jsonProvider();

		List<String> indices = indexUtils.getIndicesForDto(viewDtoClass);
		IViewDao<IViewDto> viewDao = PuiApplicationContext.getInstance()
				.getBean(daoRegistry.getDaoFromEntityName(view, false));
		if (viewDao == null) {
			return;
		}

		for (LinkedList<JoinTableDef> tableOrder : tableOrderList) {
			Map<String, String> nameAliasMap = tableOrder.descendingIterator().next().getNameAliasMap();
			Class<IDao<IDto>> entityDaoClass = daoRegistry
					.getDaoFromEntityName(tableOrder.descendingIterator().next().getTable().getName(), false);
			if (entityDaoClass == null) {
				continue;
			}

			IDao<IDto> entityDao = PuiApplicationContext.getInstance().getBean(entityDaoClass);
			List<IDto> result;
			try {
				Select<Record> query = getPartialSql(tableOrder, dtoPk);
				result = entityDao.executeCustomQuery(query);
			} catch (PuiDaoFindException e) {
				result = Collections.emptyList();
			} catch (UnsupportedOperationException e) {
				return;
			}

			if (ObjectUtils.isEmpty(result)) {
				continue;
			}

			Class<? extends IDto> dtoPkClass = result.get(0).getClass();
			FilterBuilder filterBuilder = FilterBuilder.newOrFilter();
			for (IDto res : result) {
				FilterBuilder filterBuilderReg = FilterBuilder.newAndFilter();
				for (String pkFieldName : DtoRegistry.getPkFields(dtoPkClass)) {
					String aliasField = pkFieldName;
					if (nameAliasMap.containsKey(pkFieldName)) {
						aliasField = nameAliasMap.get(pkFieldName);
					}
					if (DtoRegistry.getColumnNames(viewDtoClass).contains(aliasField)) {
						try {
							Object value = DtoRegistry.getJavaFieldFromFieldName(dtoPkClass, pkFieldName).get(res);
							if (DtoRegistry.getStringFields(dtoPkClass).contains(pkFieldName)) {
								filterBuilderReg.addEqualsExact(aliasField, (String) value);
							} else {
								filterBuilderReg.addEquals(aliasField, value);
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// do nothing
						} catch (NullPointerException e) {
							break;
						}
					}
				}
				filterBuilder.addGroup(filterBuilderReg);
			}

			// add pk filter
			filterBuilder = FilterBuilder.newAndFilter().addGroup(filterBuilder)
					.addGroup(FilterBuilder.newFilter(pkFilter));

			// obtain the registry from the view
			List<IViewDto> viewDtoList;
			try {
				viewDtoList = viewDao.findWhere(filterBuilder);
			} catch (PuiDaoFindException e) {
				// should never occurs
				viewDtoList = Collections.emptyList();
			}

			if (ObjectUtils.isEmpty(viewDtoList)) {
				continue;
			}

			Map<String, String> mapLangIndex = new LinkedHashMap<>();
			for (String index : indices) {
				PuiLanguage language = indexUtils.getLanguageFromIndex(index);
				if (language != null) {
					mapLangIndex.put(language.getIsocode(), index);
				} else {
					mapLangIndex.put(NO_LANG, index);
				}

				// create the index if not exists
				if (!indexService.existsIndex(viewDtoClass, language)) {
					try {
						indexService.createIndex(viewDtoClass, language);
					} catch (PuiElasticSearchCreateIndexException e) {
						// do nothing
					}
				}
			}

			BulkRequest.Builder br = new BulkRequest.Builder().refresh(Refresh.True);

			for (IViewDto viewDto : viewDtoList) {
				PuiLanguage dtoLang = PuiLanguageUtils.getLanguage(viewDto);
				String lang = NO_LANG;
				if (dtoLang != null) {
					lang = dtoLang.getIsocode();
				}
				String index = mapLangIndex.get(lang);

				String id = getIdForDto(viewDto);
				String json = GsonSingleton.getSingleton().getGson().toJson(viewDto);
				Reader reader = new StringReader(json);

				JsonData data = JsonData.from(jsonProvider.createParser(reader), jsonpMapper);
				if (opType.equals(DocumentOperationType.insert)) {
					br.operations(op -> op.index(idx -> idx.index(index).id(id).document(data)));
				} else if (opType.equals(DocumentOperationType.update)) {
					br.operations(
							op -> op.update(upd -> upd.index(index).id(id).action(a -> a.doc(data).docAsUpsert(true))));
				}
			}

			BulkRequest request = br.build();
			if (ObjectUtils.isEmpty(request.operations())) {
				continue;
			}

			BulkResponse response;
			try {
				response = getClient().bulk(request);
			} catch (ElasticsearchException | IOException e) {
				logger.error(e.getMessage(), e);
				switch (opType) {
				case insert:
					throw new PuiElasticSearchInsertDocumentException(e.getMessage());
				case update:
					throw new PuiElasticSearchUpdateDocumentException(e.getMessage());
				case delete:
				default:
					continue;
				}
			}

			if (response.errors()) {
				for (BulkResponseItem item : response.items()) {
					if (item.error() != null) {
						logger.error(item.error().reason());
					}
				}
			}
		}
	}

	private <V extends IViewDto> void innerDeleteDocument(ITableDto dtoPk, String view, FilterGroup pkFilter)
			throws PuiElasticSearchDeleteDocumentException {
		Class<V> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
		if (viewDtoClass == null) {
			return;
		}

		String table = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(dtoPk.getClass()));
		List<LinkedList<JoinTableDef>> tableOrderList = elasticViewsAnalysis.getTableOrder(table, view);

		List<String> indices = indexUtils.getIndicesForDto(viewDtoClass);

		FilterBuilder filterBuilder = FilterBuilder.newAndFilter();
		for (LinkedList<JoinTableDef> tableOrder : tableOrderList) {
			Map<String, String> nameAliasMap = tableOrder.descendingIterator().next().getNameAliasMap();

			for (String pkFieldName : DtoRegistry.getPkFields(dtoPk.getClass())) {
				String aliasField = pkFieldName;
				if (nameAliasMap.containsKey(pkFieldName)) {
					aliasField = nameAliasMap.get(pkFieldName);
				}
				if (DtoRegistry.getColumnNames(viewDtoClass).contains(aliasField)) {
					try {
						Object value = DtoRegistry.getJavaFieldFromFieldName(dtoPk.getClass(), pkFieldName).get(dtoPk);
						if (DtoRegistry.getStringFields(dtoPk.getClass()).contains(pkFieldName)) {
							filterBuilder.addEqualsExact(aliasField, (String) value);
						} else {
							filterBuilder.addEquals(aliasField, value);
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// do nothing
					} catch (NullPointerException e) {
						break;
					}
				}
			}
		}

		// add pk filter
		filterBuilder = FilterBuilder.newAndFilter().addGroup(filterBuilder)
				.addGroup(FilterBuilder.newFilter(pkFilter));

		for (String index : indices) {
			PuiLanguage language = indexUtils.getLanguageFromIndex(index);

			if (!indexService.existsIndex(viewDtoClass, language)) {
				continue;
			}

			// obtain the registry from Elastic Search
			List<ESSearchResultItem<V>> list;
			try {
				list = searchingService.findMultiple(viewDtoClass, filterBuilder, null, language);
			} catch (PuiElasticSearchSearchException e1) {
				// should never occurs
				continue;
			}

			if (ObjectUtils.isEmpty(list)) {
				continue;
			}

			BulkRequest.Builder br = new BulkRequest.Builder().refresh(Refresh.True);

			for (ESSearchResultItem<V> item : list) {
				br.operations(op -> op.delete(d -> d.index(index).id(item.getId())));
			}

			BulkRequest request = br.build();
			if (ObjectUtils.isEmpty(request.operations())) {
				continue;
			}

			BulkResponse response;
			try {
				response = getClient().bulk(request);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new PuiElasticSearchDeleteDocumentException(e.getMessage());
			}

			if (response.errors()) {
				for (BulkResponseItem item : response.items()) {
					if (item.error() != null) {
						logger.error(item.error().reason());
					}
				}
			}
		}
	}

	private String getIdForDto(IViewDto dto) {
		String entityName = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(dto.getClass()));
		List<String> ids;
		synchronized (cacheViewIds) {
			ids = cacheViewIds.get(entityName);
		}
		StringBuilder sb = new StringBuilder();

		for (Iterator<String> it = ids.iterator(); it.hasNext();) {
			String next = it.next();
			Field field = DtoRegistry.getJavaFieldFromColumnName(dto.getClass(), next);
			if (field == null) {
				field = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), next);
			}
			try {
				Object val = field.get(dto);
				if (val == null) {
					val = "null";
				}
				sb.append(val.toString());
			} catch (Exception e) {
				// do nothing
			}
			if (it.hasNext()) {
				sb.append(PK_SEPARATOR);
			}
		}

		return sb.toString();
	}

	private Select<Record> getPartialSql(LinkedList<JoinTableDef> tableOrder, ITableDto dto) {
		Select<Record> query = null;

		for (int i = tableOrder.size() - 1; i >= 0; i--) {
			JoinTableDef fromTableDef = tableOrder.get(i);

			Class<? extends IDto> dtoClass = daoRegistry.getDtoFromEntityName(fromTableDef.getTable().getName(), false,
					false);
			if (dtoClass == null || !DtoRegistry.isRegistered(dtoClass)) {
				return null;
			}
			List<String> pkFields = DtoRegistry.getPkFields(dtoClass);

			List<org.jooq.Field<Object>> projection = new ArrayList<>();
			for (Iterator<String> pkIt = pkFields.iterator(); pkIt.hasNext();) {
				String alias = fromTableDef.getAlias() != null ? fromTableDef.getAlias().first()
						: fromTableDef.getTable().getName();
				String column = DtoRegistry.getColumnNameFromFieldName(dtoClass, pkIt.next());

				projection.add(DSL.field(DSL.unquotedName(alias, column)));
			}

			Table<Record> from = fromTableDef.getTable();
			if (fromTableDef.getAlias() != null) {
				from = from.as(fromTableDef.getAlias());
			}
			Select<Record> thisSelect = dbHelper.getDSLContext().select(projection).from(from);

			JoinTableDef lastJoinDef = fromTableDef;
			for (i--; i >= 0; i--) {
				JoinTableDef joinTableDef = tableOrder.get(i);
				if (joinTableDef.getJoinType().equals(JoinType.FROM)) {
					i++;
					break;
				}

				lastJoinDef = joinTableDef;
				Table<Record> join = joinTableDef.getTable();
				if (joinTableDef.getAlias() != null) {
					join = join.as(joinTableDef.getAlias());
				}
				((SelectJoinStep<Record>) thisSelect).leftJoin(join).on(joinTableDef.getJoinCondition());
			}

			dtoClass = dto.getClass();
			pkFields = DtoRegistry.getPkFields(dtoClass);
			List<Condition> conditions = new ArrayList<>();
			for (Iterator<String> pkIt = pkFields.iterator(); pkIt.hasNext();) {
				String pkField = pkIt.next();
				String pkColumn = DtoRegistry.getColumnNameFromFieldName(dtoClass, pkField);
				String alias = lastJoinDef.getAlias() != null ? lastJoinDef.getAlias().first()
						: lastJoinDef.getTable().getName();
				Object value;
				try {
					value = DtoRegistry.getJavaFieldFromColumnName(dtoClass, pkColumn).get(dto);
				} catch (Exception e) {
					return null;
				}
				conditions.add(DSL.field(DSL.unquotedName(alias, pkColumn)).eq(DSL.inline(value)));
			}

			((SelectJoinStep<Record>) thisSelect).where(conditions);
			((SelectJoinStep<Record>) thisSelect).groupBy(projection);

			if (query != null && fromTableDef.getJoinType().equals(JoinType.FROM)) {
				query.union(thisSelect);
			} else {
				query = thisSelect;
			}
		}

		return query;
	}

	private void refreshViewIds() {
		try {
			synchronized (cacheViewIds) {
				cacheViewIds.clear();
				List<IPuiElasticsearchViews> list = elasticsearchViewsDao.findByAppname(elasticsearchAppname);
				cacheViewIds.putAll(list.stream()
						.collect(Collectors.toMap(IPuiElasticsearchViews::getViewname,
								esv -> Stream.of(esv.getIdentityfields().split(",", -1)).map(String::trim)
										.map(String::toLowerCase).collect(Collectors.toList()))));
			}
		} catch (PuiDaoFindException e) {
			// do nothing
		}
	}

	private ElasticsearchClient getClient() {
		return puiElasticSearchManager.getClient();
	}

}
