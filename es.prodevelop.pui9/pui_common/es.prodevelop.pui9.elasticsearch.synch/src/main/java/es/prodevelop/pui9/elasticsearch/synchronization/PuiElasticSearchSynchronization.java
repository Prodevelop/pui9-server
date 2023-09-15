package es.prodevelop.pui9.elasticsearch.synchronization;

import java.lang.Thread.State;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViews;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.elasticsearch.analysis.PuiElasticsearchViewsAnalysis;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCountException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCreateIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchDeleteIndexException;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.elasticsearch.services.PuiElasticSearchDocumentService;
import es.prodevelop.pui9.elasticsearch.services.PuiElasticSearchIndexService;
import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.threads.PuiMultiInstanceProcessBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

/**
 * Implementation for ElasticSearch synchronization process. Allows to
 * synchronize documents on demand (for instance, from a Web Service)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PuiElasticSearchSynchronization {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private IPuiElasticSearchEnablement elasticSearchEnablement;

	@Autowired
	private PuiElasticSearchIndexService indexService;

	@Autowired
	private PuiElasticSearchDocumentService documentService;

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private PuiElasticsearchViewsAnalysis elasticViewsAnalysis;

	@Autowired
	private PuiMultiInstanceProcessBackgroundExecutors multiInstanceProcessBackExec;

	private Thread thread;
	private boolean cancelRequest = false;

	@PostConstruct
	private void postConstruct() {
		Long initDelay = PuiMultiInstanceProcessBackgroundExecutors.getNextExecutionDelayAsMinutes(3, 0);

		multiInstanceProcessBackExec.registerNewExecutor("ElasticSearch_NightSynchronizer", initDelay,
				TimeUnit.DAYS.toMinutes(1), TimeUnit.MINUTES, () -> synchronize(null, false));
	}

	/**
	 * Stops the current syncronization
	 */
	public void stopSyncronizing() {
		if (thread != null) {
			cancelRequest = true;
			for (;;) {
				if (thread.getState().equals(State.TERMINATED)) {
					break;
				}
			}
		}
	}

	/**
	 * Start the synchronization in background
	 * 
	 * @param view  The view to be synched
	 * @param force If should force recreating the index
	 */
	public void synchronizeInBackground(String view, boolean force) {
		stopSyncronizing();

		thread = new Thread(() -> this.synchronize(view, force), "PuiThread_ElasticSearchSynchronizerInBackground");
		thread.start();
	}

	/**
	 * Do the Elastic Search synchronization from the Database. You can indicate to
	 * force the reindex or not
	 * 
	 * @param view  The name of the view to force reindexing. By default, null
	 * @param force If true, the views will always be reindexed; if false, only will
	 *              be reindexed if the number of registers differs from the DDBB
	 *              View and the Elastic Search Index
	 */
	private void synchronize(String view, Boolean force) {
		logger.debug("Starting Elastic Search synchronization");
		cancelRequest = false;

		// force elasticsearch reconnect to the server
		boolean currentStatus = elasticSearchEnablement.isElasticSearchActive();
		elasticSearchEnablement.setElasticSearchActive(true);
		if (!currentStatus) {
			elasticSearchEnablement.setElasticSearchActive(currentStatus);
		}

		// check if elastic search is available or not. Maybe it is not connected to any
		// node
		if (!elasticSearchEnablement.isElasticSearchAvailable()) {
			logger.debug("Elastic Search is not available");
			return;
		}

		Class<? extends IViewDto> paramViewDtoClass = null;
		if (!ObjectUtils.isEmpty(view)) {
			view = view.trim().toLowerCase();
			paramViewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
			if (paramViewDtoClass == null) {
				return;
			}
		}

		for (IPuiElasticsearchViews iv : elasticViewsAnalysis.getIndexableViews()) {
			Class<? extends IViewDao> viewDaoClass = daoRegistry.getDaoFromEntityName(iv.getParsedViewName(), false);
			if (viewDaoClass == null) {
				continue;
			}

			IViewDao viewDao = PuiApplicationContext.getInstance().getBean(viewDaoClass);
			if (viewDao == null) {
				continue;
			}

			Class<? extends IViewDto> dtoClass = daoRegistry.getDtoFromDao(viewDaoClass, false);
			if (!elasticSearchEnablement.isViewIndexable(dtoClass)) {
				continue;
			}

			// if a view name is passed as parameter, only continue if the current view is
			// the same
			if (paramViewDtoClass != null && !paramViewDtoClass.isAssignableFrom(dtoClass)) {
				continue;
			}

			elasticSearchEnablement.addSynchronizingView(dtoClass);

			boolean hasLanguage = DtoRegistry.getJavaFieldFromColumnName(dtoClass, IDto.LANG_COLUMN_NAME) != null;

			// check index exists
			boolean exists = indexService.existsIndex(dtoClass);
			if (exists && force.booleanValue()) {
				exists = false;
				try {
					indexService.deleteIndex(dtoClass);
				} catch (PuiElasticSearchDeleteIndexException e) {
					// should never happens
					elasticSearchEnablement.removeSynchronizingView(dtoClass);
					return;
				}
			}

			if (exists) {
				// if exists, check the index content size to be correct (all the indices
				// (languages) of the same view should has the same size)
				try {
					long indexCount = indexService.countIndex(dtoClass);

					// if success, obtain the view content size
					FilterBuilder filterBuilder = null;
					if (hasLanguage) {
						// if dto has language, add the filter for it using the default language
						filterBuilder = FilterBuilder.newOrFilter()
								.addEqualsExact(IDto.LANG_COLUMN_NAME,
										PuiLanguageUtils.getDefaultLanguage().getIsocode())
								.addIsNull(IDto.LANG_COLUMN_NAME);
					}

					long viewCount;
					try {
						viewCount = viewDao.count(filterBuilder);
					} catch (PuiDaoCountException e) {
						logger.debug("The View '" + dtoClass.getSimpleName() + "' doesn't exist");
						elasticSearchEnablement.removeSynchronizingView(dtoClass);
						return;
					}

					// if size from view and elastic search doesn't coincide, delete the index
					if (indexCount != viewCount) {
						logger.debug("The View and the Index size is not the same for '" + dtoClass.getSimpleName()
								+ "'. Delete the indices and create them again");
						try {
							indexService.deleteIndex(dtoClass);
							exists = false;
						} catch (PuiElasticSearchDeleteIndexException e1) {
							// should never happens
							elasticSearchEnablement.removeSynchronizingView(dtoClass);
							return;
						}
					}
				} catch (PuiElasticSearchCountException e) {
					// if an error in the count exists, remove the index. This is because the
					// distint indices for each language has distinct size
					logger.debug("The Indices for '" + dtoClass.getSimpleName()
							+ "' has distinct size. Delete the indices and create them again");
					try {
						indexService.deleteIndex(dtoClass);
						exists = false;
					} catch (PuiElasticSearchDeleteIndexException e1) {
						// should never happens
						elasticSearchEnablement.removeSynchronizingView(dtoClass);
						return;
					}
				}
			}

			if (!exists) {
				// if the index doesn't exists, create it...
				logger.debug("Creating indices for '" + viewDao + "'");
				try {
					indexService.createIndex(dtoClass);
				} catch (PuiElasticSearchCreateIndexException e) {
					// should never happens
					logger.error("Error while indexing the View '" + dtoClass.getSimpleName() + "'", e);
					elasticSearchEnablement.removeSynchronizingView(dtoClass);
					return;
				}

				Iterator<PuiLanguage> it;
				if (hasLanguage) {
					it = PuiLanguageUtils.getLanguagesIterator();
					logger.debug("Filling indices for '" + dtoClass.getSimpleName() + "' (all languages)");
				} else {
					it = Collections.singletonList(PuiLanguageUtils.getDefaultLanguage()).iterator();
					logger.debug("Filling indices for '" + dtoClass.getSimpleName() + "'");
				}

				String columnNameId = null;
				Field fieldId = null;
				OrderBuilder order = OrderBuilder.newOrder();

				if (iv.getParsedIdFields().size() == 1) {
					columnNameId = iv.getParsedIdFields().get(0);
					fieldId = DtoRegistry.getJavaFieldFromColumnName(dtoClass, columnNameId);
					order.addOrder(Order.newOrderAsc(columnNameId));
				} else {
					for (String idField : iv.getParsedIdFields()) {
						order.addOrder(Order.newOrderAsc(idField));
					}
				}

				// and fill it
				while (it.hasNext()) {
					PuiLanguage language = it.next();

					int totalItems = 0;
					int page = 1;
					int rows = 1000;
					Object latestId = null;

					SearchRequest req = new SearchRequest();
					req.setOrder(order.getOrders());
					req.setPerformCount(false);
					req.setPage(page);
					req.setRows(rows);
					req.setQueryLang(language.getIsocode());

					while (!cancelRequest) {
						List<IViewDto> list;
						if (columnNameId != null) {
							if (latestId != null) {
								req.setFilter(FilterBuilder.newAndFilter().addGreaterThan(columnNameId, latestId)
										.asFilterGroup());
							}
						} else {
							req.setPage(page++);
						}

						long start = System.currentTimeMillis();
						try {
							list = viewDao.findPaginated(req).getData();
						} catch (PuiDaoListException e) {
							list = Collections.emptyList();
						}
						long end = System.currentTimeMillis();
						totalItems += list.size();
						logger.info("Retrieved " + list.size() + " registries (total: " + totalItems + ") from DB in "
								+ (end - start) + " ms");

						if (ObjectUtils.isEmpty(list)) {
							break;
						}

						start = System.currentTimeMillis();
						documentService.bulkInsertDocument(list, language);
						end = System.currentTimeMillis();
						logger.info(
								"Indexed " + list.size() + " registries to ElasticSearch in " + (end - start) + " ms");

						if (fieldId != null) {
							try {
								latestId = FieldUtils.readField(fieldId, list.get(list.size() - 1), true);
							} catch (IllegalAccessException e1) {
								// do nothing
							}
						}
					}
				}

				logger.debug("The view '" + dtoClass.getSimpleName() + "' is now synchronized");
			} else {
				// the View and the Index are synchronized
				logger.debug("The view '" + dtoClass.getSimpleName() + "' is yet synchronized");
			}

			elasticSearchEnablement.removeSynchronizingView(dtoClass);
		}

		logger.debug("Finishing Elastic Search synchronization");
	}

}
