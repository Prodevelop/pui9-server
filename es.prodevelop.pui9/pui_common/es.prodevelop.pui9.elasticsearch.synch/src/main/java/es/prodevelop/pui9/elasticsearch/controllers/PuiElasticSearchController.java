package es.prodevelop.pui9.elasticsearch.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.controller.AbstractPuiController;
import es.prodevelop.pui9.elasticsearch.analysis.PuiElasticsearchViewsAnalysis;
import es.prodevelop.pui9.elasticsearch.enums.DocumentOperationType;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchDeleteIndexException;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.elasticsearch.services.PuiElasticSearchDocumentService;
import es.prodevelop.pui9.elasticsearch.services.PuiElasticSearchIndexService;
import es.prodevelop.pui9.elasticsearch.synchronization.PuiElasticSearchLiveSynchronization;
import es.prodevelop.pui9.elasticsearch.synchronization.PuiElasticSearchSynchronization;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.eventlistener.event.DeleteDaoEvent;
import es.prodevelop.pui9.eventlistener.event.InsertDaoEvent;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.eventlistener.event.UpdateDaoEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.utils.IPuiObject;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller provide some useful methods to manage Elastic Search for the
 * application. It allows to activate or deactivate it, get the information of
 * the indices, reindex an index, delete an index, index a new document...
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Tag(name = "PUI ElasticSearch")
@RequestMapping("/elasticsearch")
public class PuiElasticSearchController extends AbstractPuiController {

	private static final String ID_FUNCTIONALITY_ADMIN = "admin";
	private static final String FUNCTIONALITY_ADMIN = "ADMIN_ELASTICSEARCH";

	@Autowired
	private IPuiElasticSearchEnablement elasticSearchEnablement;

	@Autowired
	private PuiElasticSearchIndexService elasticSearchIndex;

	@Autowired
	private PuiElasticSearchDocumentService elasticSearchDocument;

	@Autowired
	private PuiElasticSearchSynchronization elasticSearchSynchronization;

	@Autowired
	private PuiElasticSearchLiveSynchronization elasticSearchLiveSynchronization;

	@Autowired
	private PuiElasticsearchViewsAnalysis elasticViewsAnalysis;

	@Autowired
	private DaoRegistry daoRegistry;

	private Map<String, List<PkFieldInfo>> tablePkCache = new LinkedHashMap<>();

	@Override
	protected String getReadFunctionality() {
		return "READ_ELASTICSEARCH";
	}

	@Override
	protected String getWriteFunctionality() {
		return "WRITE_ELASTICSEARCH";
	}

	/**
	 * Refreshes all the configuration, caches and indexable views
	 */
	@PuiFunctionality(id = ID_FUNCTIONALITY_ADMIN, value = FUNCTIONALITY_ADMIN)
	@Operation(summary = "Refresh ElasticSearch", description = "Refresh ElasticSearch configuration (caches and indexable views)")
	@GetMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
	public void refresh() {
		elasticViewsAnalysis.refresh();
		elasticSearchDocument.refresh();
		elasticSearchLiveSynchronization.refresh();
	}

	/**
	 * Allow to reindex the index represented by the given view name
	 * 
	 * @param view  The name of the view you want to reindex the associated index
	 *              (or indices, in case of translated view)
	 * @param force If set to true, the index will be always reindexed; if set to
	 *              false, it will be reindexed only if it's not valid (distinct
	 *              number of registries in the database view and in the index)
	 * @return A message saying that Elastic Search is being updated
	 */
	@PuiFunctionality(id = ID_FUNCTIONALITY_ADMIN, value = FUNCTIONALITY_ADMIN)
	@Operation(summary = "Reindex ElasticSearch", description = "Reindex ElasticSearch depending on the given parameters")
	@GetMapping(value = "/reindex", produces = MediaType.APPLICATION_JSON_VALUE)
	public String reindex(
			@Parameter(description = "The view name of the index to be reindexed") @RequestParam(required = false) String view,
			@Parameter(description = "Force reindex, even if it's valid") @RequestParam(required = false) boolean force) {
		elasticSearchSynchronization.synchronizeInBackground(view, force);

		StringBuilder sb = new StringBuilder();
		sb.append("Elastic Search synchronization process is running in background. ");
		sb.append("It may take several minutes until it finishes, ");
		sb.append("depending on the number of views to index ");
		sb.append("and the number of registries of each view, ");
		sb.append("so be patient... \\^_^/");

		return sb.toString();
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_ADMIN, value = FUNCTIONALITY_ADMIN)
	@Operation(summary = "Stop Synchronizing Elastic Search", description = "Stop Synchronizing Elastic Search")
	@GetMapping(value = "/stopReindex", produces = { MediaType.APPLICATION_JSON_VALUE })
	public void stopReindex() {
		elasticSearchSynchronization.stopSyncronizing();
	}

	/**
	 * Activate or deactivate Elastic Search for this application
	 * 
	 * @param active true to activate; false to deactivate
	 */
	@PuiFunctionality(id = ID_FUNCTIONALITY_ADMIN, value = FUNCTIONALITY_ADMIN)
	@Operation(summary = "Activate or deactivate ElasticSearch", description = "Activate or deactivate ElasticSearch status")
	@GetMapping(value = "/setActive")
	public void setActive(
			@Parameter(description = "Active or deactive", required = true) @RequestParam Boolean active) {
		elasticSearchEnablement.setElasticSearchActive(active);
	}

	/**
	 * Set an index represented by the given view as blocked of unblocked. Valid if
	 * you want to disable Elastic Search only for certain views
	 * 
	 * @param view    The name of the view you want to block or unblock the
	 *                associated index (or indices, in case of translated view)
	 * @param blocked true to block; false to unblock
	 */
	@PuiFunctionality(id = ID_FUNCTIONALITY_ADMIN, value = FUNCTIONALITY_ADMIN)
	@Operation(summary = "Set index as blocked or not", description = "Set an index as blocked or not")
	@GetMapping(value = "/setBlocked")
	public void setBlocked(
			@Parameter(description = "The view that represents the index", required = true) @RequestParam String view,
			@Parameter(description = "Blocked or not", required = true) @RequestParam boolean blocked) {
		view = view.trim().toLowerCase();
		if (!daoRegistry.existsDaoForEntity(view)) {
			return;
		}

		Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
		if (viewDtoClass == null) {
			return;
		}

		if (blocked) {
			elasticSearchEnablement.addBlockedView(viewDtoClass);
		} else {
			elasticSearchEnablement.removeBlockedView(viewDtoClass);
		}
	}

	/**
	 * Delete the index represented by the given view
	 * 
	 * @param view The name of the view you want to delete the associated index (or
	 *             indices, in case of translated view)
	 * @throws PuiElasticSearchDeleteIndexException If any error occurs while
	 *                                              deleting the Index
	 */
	@PuiFunctionality(id = ID_FUNCTIONALITY_ADMIN, value = FUNCTIONALITY_ADMIN)
	@Operation(summary = "Delete the index of the given view", description = "Delete the index of the given view")
	@DeleteMapping(value = "/deleteIndex")
	public void deleteIndex(
			@Parameter(description = "The view that represents the index", required = true) @RequestParam String view)
			throws PuiElasticSearchDeleteIndexException {
		view = view.trim().toLowerCase();
		if (!daoRegistry.existsDaoForEntity(view)) {
			return;
		}

		Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
		if (viewDtoClass == null) {
			return;
		}

		elasticSearchIndex.deleteIndex(viewDtoClass);
	}

	/**
	 * Indicate Elastic Search that a document should be inserted, updated or
	 * deleted from the index.
	 * <p>
	 * It is commonly used in applications that not only the own application manages
	 * the used database. This case, the other applications should call this method
	 * in order to maintain synchronized the Elastic Search indices
	 * 
	 * @param type  The operation type: insert, update or delete
	 * @param table The name of the affected table
	 * @param pk    The PK of the registry to be processed
	 * @return Always returns true if all worked fine
	 * @throws PuiException If Elastic Search is not enabled for the application; if
	 *                      the table is not mapped in the code or if the PK is not
	 *                      well formed for the given table
	 */
	@SuppressWarnings("rawtypes")
	@PuiFunctionality(id = ID_FUNCTIONALITY_INSERT, value = METHOD_FUNCTIONALITY_INSERT)
	@Operation(summary = "Insert, Update or Delete the given document from the given view", description = "Insert, Update or Delete the given document from the given view")
	@PostMapping(value = "/document", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void document(
			@Parameter(description = "Type of operation", required = true) @RequestParam DocumentOperationType type,
			@Parameter(description = "Name of the table to operate with", required = true) @RequestParam String table,
			@Parameter(description = "PK of the element", required = true) @RequestBody String pk) throws PuiException {
		table = table.trim().toLowerCase();
		Class<? extends ITableDao> tableDaoClass;
		if (ITableDao.class.isAssignableFrom(daoRegistry.getDaoFromEntityName(table, false))) {
			// table really is a table
			tableDaoClass = daoRegistry.getDaoFromEntityName(table, false);
		} else {
			// table maybe is a view? find the associated table
			Class<? extends IViewDao> viewDaoClass = daoRegistry.getDaoFromEntityName(table, false);
			tableDaoClass = daoRegistry.getTableDaoFromViewDao(viewDaoClass);
		}

		if (tableDaoClass == null) {
			throw new PuiException("The given table is not mapped in the code");
		}

		Class<? extends ITableDto> tableDtoClass = daoRegistry.getDtoFromDao(tableDaoClass, true);
		if (tableDtoClass == null) {
			return;
		}

		ITableDto dtoPk;
		try {
			dtoPk = GsonSingleton.getSingleton().getGson().fromJson(pk, tableDtoClass);
		} catch (Exception e) {
			throw new PuiException("Could not parse the given Dto Pk to the given table");
		}

		PuiEvent event = null;
		switch (type) {
		case insert:
			event = new InsertDaoEvent(dtoPk);
			break;
		case update:
			event = new UpdateDaoEvent(dtoPk);
			break;
		case delete:
			event = new DeleteDaoEvent(dtoPk);
			break;
		}

		logger.debug("Received an '" + type + "' operation for Elastic Search against table '" + table + "' with pk '"
				+ pk + "'");

		getEventLauncher().fireSync(event);
	}

	/**
	 * Count the number of registries in the database
	 * 
	 * @param view The view to be checked
	 * @return The number of registries in the database
	 * @throws PuiException If an exception occurs while counting
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PuiFunctionality(id = ID_FUNCTIONALITY_GET, value = METHOD_FUNCTIONALITY_GET)
	@Operation(summary = "Number of elements indexed for the view", description = "Get the number of indexed elements for the given view")
	@GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public Long count(@Parameter(description = "The view", required = true) @RequestParam String view)
			throws PuiException {
		Class<? extends IDao> daoClass = daoRegistry.getDaoFromEntityName(view, false);
		if (daoClass == null) {
			return -1L;
		}

		IDao dao;
		try {
			dao = PuiApplicationContext.getInstance().getBean(daoClass);
		} catch (Exception e) {
			return -1L;
		}

		Long count = dao.count();
		if (PuiLanguageUtils.hasLanguageSupport(dao.getDtoClass())) {
			count /= PuiLanguageUtils.getLanguageCount();
		}

		return count;
	}

	/**
	 * Get a complete information about the current status of all the indices of
	 * Elastic Search
	 * 
	 * @return The Elastic Search information
	 * @throws PuiException If any error occur while collecting the information
	 */
	@SuppressWarnings("rawtypes")
	@PuiFunctionality(id = ID_FUNCTIONALITY_GET, value = METHOD_FUNCTIONALITY_GET)
	@Operation(summary = "Get ElasticSearch information", description = "Get ElasticSearch information of the server")
	@GetMapping(value = "/getInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ElasticInfo getInfo() throws PuiException {
		ElasticInfo info = new ElasticInfo();

		info.available = elasticSearchEnablement.isElasticSearchAvailable();
		info.active = elasticSearchEnablement.isElasticSearchActive();
		info.isSynchronizingAnyView = elasticSearchEnablement.isSynchronizingAnyView();

		if (!info.active) {
			return info;
		}

		info.models = elasticViewsAnalysis.getIndexableModels();

		for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
			info.languages.add(it.next());
		}

		if (info.languages.size() == 1) {
			info.languages.clear();
		}

		Collections.sort(info.languages);

		List<String> indices = elasticSearchIndex.getAllIndices();

		Map<String, Long> counts = elasticSearchIndex.countIndex(indices);

		Map<String, Map<PuiLanguage, Long>> countsLanguage = new LinkedHashMap<>();
		for (Entry<String, Long> entry : counts.entrySet()) {
			String index = entry.getKey();
			Long count = entry.getValue();
			String tableIndexName = index;

			if (!ObjectUtils.isEmpty(elasticSearchIndex.getIndexPrefix())) {
				tableIndexName = tableIndexName.replaceAll(elasticSearchIndex.getIndexPrefix() + "_", "");
			}

			PuiLanguage language = null;
			for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
				PuiLanguage next = it.next();
				if (tableIndexName.endsWith("_" + next.getIsocode())) {
					language = next;
					tableIndexName = tableIndexName.substring(0, tableIndexName.lastIndexOf('_'));
					break;
				}
			}

			if (!daoRegistry.existsDaoForEntity(tableIndexName)) {
				continue;
			}

			if (!countsLanguage.containsKey(tableIndexName)) {
				countsLanguage.put(tableIndexName, new LinkedHashMap<>());
			}

			countsLanguage.get(tableIndexName).put(language, count);
		}

		for (Entry<String, Map<PuiLanguage, Long>> entry : countsLanguage.entrySet()) {
			String viewName = entry.getKey();
			Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(viewName, false, false);
			if (viewDtoClass == null) {
				continue;
			}

			IndexInfo iInfo = new IndexInfo();
			iInfo.name = viewName;
			if (elasticSearchEnablement.isSynchronizingView(viewDtoClass)) {
				iInfo.status = IndexStatus.synchronizing;
			} else if (elasticSearchEnablement.isViewBlocked(viewDtoClass)) {
				iInfo.status = IndexStatus.blocked;
			}

			if (entry.getValue().size() == 1) {
				iInfo.count = entry.getValue().values().iterator().next();
				for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
					PuiLanguage lang = it.next();
					LangInfo li = new LangInfo();
					li.language = lang.getIsocode();
					li.count = -1L;
					iInfo.counts.add(li);
				}
			} else {
				Long prevCount = -1L;
				for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
					PuiLanguage lang = it.next();
					Long count = entry.getValue().get(lang) != null ? entry.getValue().get(lang) : 0L;
					if (prevCount.equals(-1L)) {
						prevCount = count;
					}
					LangInfo li = new LangInfo();
					li.language = lang.getIsocode();
					li.count = count;
					if (!prevCount.equals(-1L) && !prevCount.equals(count)
							&& !iInfo.status.equals(IndexStatus.blocked)) {
						iInfo.status = IndexStatus.error;
					}
					iInfo.counts.add(li);
				}
			}

			Collections.sort(iInfo.counts);

			Set<String> relatedTables = elasticViewsAnalysis.getTablesParticipatingInView(viewName);
			for (String table : relatedTables) {
				RelatedTableInfo rtInfo = new RelatedTableInfo();
				rtInfo.name = table;

				if (!tablePkCache.containsKey(table)) {
					if (!daoRegistry.existsDaoForEntity(table)) {
						continue;
					}
					Class<? extends ITableDao> tableDaoClass = daoRegistry.getDaoFromEntityName(table, false);
					if (tableDaoClass == null) {
						tablePkCache.put(table, null);
						continue;
					}
					Class<? extends IDto> tableDtoClass = daoRegistry.getDtoFromDao(tableDaoClass, true);
					if (tableDtoClass == null) {
						tablePkCache.put(table, null);
						continue;
					}

					List<String> fieldNames = DtoRegistry.getPkFields(tableDtoClass);
					Set<String> models = daoRegistry.getModelIdFromDao(tableDaoClass);
					String tableModelId = ObjectUtils.isEmpty(models) ? null : models.iterator().next();
					List<PkFieldInfo> pkInfoList = new ArrayList<>();

					for (String fieldName : fieldNames) {
						String fieldLabel = tableModelId != null ? (tableModelId + "." + fieldName) : fieldName;
						ColumnType type = null;
						if (DtoRegistry.getStringFields(tableDtoClass).contains(fieldName)) {
							type = ColumnType.text;
						} else if (DtoRegistry.getNumericFields(tableDtoClass).contains(fieldName)) {
							type = ColumnType.numeric;
						} else if (DtoRegistry.getFloatingFields(tableDtoClass).contains(fieldName)) {
							type = ColumnType.decimal;
						} else if (DtoRegistry.getDateTimeFields(tableDtoClass).contains(fieldName)) {
							type = ColumnType.datetime;
						} else if (DtoRegistry.getBooleanFields(tableDtoClass).contains(fieldName)) {
							type = ColumnType.logic;
						}
						PkFieldInfo pkField = new PkFieldInfo();
						pkField.name = fieldName;
						pkField.label = fieldLabel;
						pkField.type = type;
						pkInfoList.add(pkField);
					}

					tablePkCache.put(table, pkInfoList);
				}

				rtInfo.pk = tablePkCache.get(table);
				if (rtInfo.pk == null) {
					rtInfo.pk = new ArrayList<>();
				}

				iInfo.relatedTables.add(rtInfo);
			}

			Collections.sort(iInfo.relatedTables);

			info.indices.add(iInfo);
		}

		Collections.sort(info.indices);

		return info;
	}

	@SuppressWarnings("unused")
	private class ElasticInfo implements IPuiObject {

		private static final long serialVersionUID = 1L;

		Boolean available = false;
		Boolean active = false;
		Boolean isSynchronizingAnyView = false;
		List<String> models = new ArrayList<>();
		List<PuiLanguage> languages = new ArrayList<>();
		List<IndexInfo> indices = new ArrayList<>();
	}

	@SuppressWarnings("unused")
	private class IndexInfo implements Comparable<IndexInfo> {
		String name = "";
		Long count = -1L;
		List<LangInfo> counts = new ArrayList<>();
		Long bbdd = -1L;
		IndexStatus status = IndexStatus.wait;
		List<RelatedTableInfo> relatedTables = new ArrayList<>();

		@Override
		public int compareTo(IndexInfo o) {
			return name.compareTo(o.name);
		}
	}

	@SuppressWarnings("unused")
	private class LangInfo implements Comparable<LangInfo> {
		String language;
		Long count;

		@Override
		public int compareTo(LangInfo o) {
			return language.compareTo(o.language);
		}
	}

	private class RelatedTableInfo implements Comparable<RelatedTableInfo> {
		String name = "";
		List<PkFieldInfo> pk = new ArrayList<>();

		@Override
		public int compareTo(RelatedTableInfo o) {
			return name.compareTo(o.name);
		}
	}

	@SuppressWarnings("unused")
	private class PkFieldInfo {
		String name = "";
		String label = "";
		ColumnType type = ColumnType.text;
		String value = null;
	}

	private enum IndexStatus {
		wait, valid, synchronizing, blocked, error
	}

}
