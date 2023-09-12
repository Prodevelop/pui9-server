package es.prodevelop.pui9.elasticsearch.synchronization;

import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.elasticsearch.analysis.PuiElasticsearchViewsAnalysis;
import es.prodevelop.pui9.elasticsearch.enums.DocumentOperationType;
import es.prodevelop.pui9.elasticsearch.eventlistener.listener.AbstractElasticSearchListener;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.elasticsearch.services.PuiElasticSearchDocumentService;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.websocket.PuiWebSocket;

/**
 * Live synchronization process for ElasticSearch. It is a backgroud process
 * that is always being executed, and is fired on each
 * {@link AbstractElasticSearchListener} instance.<br>
 * <br>
 * It has a queue for each {@link ITableDto} class for not blocking amongst
 * them. Each operation of a {@link ITableDto} type is treated in a synchonized
 * way. That means that if multiple operations of a same DTO type are received,
 * they are processed one by one each (if two operations over the same element
 * are received, it will wait <b>1 second</b> between these executions)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticSearchLiveSynchronization {

	private static final String WEBSOCKET_MODELS_URL = PuiWebSocket.TOPIC_DESTINATION + "model/";

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private IPuiElasticSearchEnablement elasticEnablement;

	@Autowired
	private PuiElasticSearchDocumentService documentService;

	@Autowired
	private PuiElasticsearchViewsAnalysis elasticViewsAnalysis;

	@Autowired
	private PuiWebSocket websocketUtils;

	private final ElasticSearchOperation finalizerESO = new ElasticSearchOperation();
	private Map<Class<? extends ITableDto>, Boolean> mapDtoIndexable;
	private Map<Class<? extends ITableDto>, List<ViewIndexableInfo>> mapDtoQueues;
	private Map<Class<? extends IViewDto>, ViewIndexableInfo> mapViewQueue;
	private boolean debug;

	@PostConstruct
	private void postConstruct() {
		mapDtoIndexable = new LinkedHashMap<>();
		mapDtoQueues = new LinkedHashMap<>();
		mapViewQueue = new LinkedHashMap<>();
		debug = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("jdwp") >= 0;
	}

	@PreDestroy
	private void preDestroy() {
		mapViewQueue.values().forEach(vii -> vii.queue.add(finalizerESO));
	}

	public void refresh() {
		mapDtoIndexable.clear();
	}

	public void queueOperation(ITableDto dtoPk, DocumentOperationType operation, Long transactionId) {
		Class<? extends ITableDto> tableDtoClass = dtoPk.getClass();
		if (!isDtoIndexable(tableDtoClass)) {
			return;
		}

		analyzeDtoViews(tableDtoClass);

		FilterGroup pkFilter = FilterGroup.createFilterForDtoPk(dtoPk);
		mapDtoQueues.get(tableDtoClass).forEach(queue -> queue.addOperation(dtoPk, operation, pkFilter, transactionId));
	}

	private boolean isDtoIndexable(Class<? extends ITableDto> tableDtoClass) {
		if (!DtoRegistry.isRegistered(tableDtoClass)) {
			return false;
		}

		if (!mapDtoIndexable.containsKey(tableDtoClass)) {
			String table = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(tableDtoClass));

			boolean shouldBeQueued = false;
			for (String view : elasticViewsAnalysis.getViewsUsingTable(table)) {
				Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
				if (viewDtoClass == null) {
					continue;
				}

				if (elasticEnablement.isViewIndexable(viewDtoClass)) {
					shouldBeQueued = true;
					break;
				}
			}
			mapDtoIndexable.put(tableDtoClass, shouldBeQueued);
		}

		return mapDtoIndexable.get(tableDtoClass);
	}

	private synchronized void analyzeDtoViews(Class<? extends ITableDto> tableDtoClass) {
		if (mapDtoQueues.containsKey(tableDtoClass)) {
			return;
		}

		mapDtoQueues.put(tableDtoClass, new ArrayList<>());

		String table = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(tableDtoClass));
		Set<String> views = elasticViewsAnalysis.getViewsUsingTable(table);

		for (String view : views) {
			Class<? extends IViewDto> viewDtoClass;
			try {
				viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				continue;
			}
			if (viewDtoClass == null) {
				continue;
			}
			if (!elasticEnablement.isViewIndexable(viewDtoClass)) {
				continue;
			}

			if (PuiApplicationContext.getInstance().getBean(daoRegistry.getDaoFromEntityName(view, false)) == null) {
				continue;
			}

			ViewIndexableInfo vii = mapViewQueue.computeIfAbsent(viewDtoClass, vdc -> new ViewIndexableInfo(vdc, view));
			mapDtoQueues.get(tableDtoClass).add(vii);
		}
	}

	private class ViewIndexableInfo {
		private Class<? extends IViewDto> viewDtoClass;
		private String view;
		private AtomicLong sequence;
		private Map<Long, AtomicInteger> mapTransactionOperations;
		private Cache<Integer, Integer> hashCache;
		private PriorityBlockingQueue<ElasticSearchOperation> queue;
		private Thread thread;

		public ViewIndexableInfo(Class<? extends IViewDto> viewDtoClass, String view) {
			this.viewDtoClass = viewDtoClass;
			this.view = view;
			this.sequence = new AtomicLong(0);
			this.mapTransactionOperations = new LinkedHashMap<>();
			this.hashCache = CacheBuilder.newBuilder().expireAfterAccess(1500, TimeUnit.MILLISECONDS).build();
			this.queue = new PriorityBlockingQueue<>();
		}

		public synchronized void addOperation(ITableDto dtoPk, DocumentOperationType operation, FilterGroup pkFilter,
				Long transactionId) {
			if (!mapTransactionOperations.containsKey(transactionId)) {
				mapTransactionOperations.put(transactionId, new AtomicInteger(1));
			} else {
				mapTransactionOperations.get(transactionId).getAndIncrement();
			}

			ElasticSearchOperation eso = new ElasticSearchOperation(dtoPk, viewDtoClass, operation, pkFilter, view,
					transactionId, sequence.getAndIncrement());
			if (debug) {
				logger.debug("*** Queue: " + eso);
			}
			synchronized (this) {
				if (thread == null || thread.getState().equals(State.BLOCKED)
						|| thread.getState().equals(State.TERMINATED)) {
					thread = new Thread(new ViewQueue(), "PuiThread_ElasticSearch_View_" + view);
					thread.start();
				}
			}
			queue.add(eso);
		}

		private class ViewQueue implements Runnable {
			@Override
			public void run() {
				ElasticSearchOperation eso;
				try {
					while (!(eso = queue.take()).equals(finalizerESO)) {
						// if elastic is not available, the message will be lost
						if (!elasticEnablement.isElasticSearchAvailable()) {
							continue;
						}

						// if view is being synched, queue again the message and try to reprocess it
						// after 5 seconds
						if (elasticEnablement.isSynchronizingView(viewDtoClass)) {
							queue.add(eso);
							Thread.sleep(5000);
							continue;
						}

						// if the current DTO was processed in the last second, queue it again and
						// reprocess it after 100 milliseconds
						int hashCode = eso.dtoPk.hashCode();
						if (hashCache.asMap().containsKey(hashCode)) {
							queue.add(eso);
							Thread.sleep(100);
							continue;
						}
						hashCache.put(hashCode, hashCode);

						try {
							eso.process();
						} catch (PuiException e) {
							queue.add(eso);
							Thread.sleep(5000);
							continue;
						}

						// try to send websocket to client to refresh it
						if (eso.transactionId != null) {
							if (mapTransactionOperations.get(eso.transactionId).decrementAndGet() == 0) {
								mapTransactionOperations.remove(eso.transactionId);
								new Thread(() -> sendWebsocketMessage(viewDtoClass),
										"PuiThread_ElasticSearch_SendWebSocket").start();
							}
						} else {
							new Thread(() -> sendWebsocketMessage(viewDtoClass),
									"PuiThread_ElasticSearch_SendWebSocket").start();
						}
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (IllegalArgumentException e) {
					logger.debug("Error while operating with Thread", e);
				}
			}

			private void sendWebsocketMessage(Class<? extends IViewDto> viewDtoClass) {
				Set<String> models = daoRegistry.getModelIdFromDto(viewDtoClass);
				if (ObjectUtils.isEmpty(models)) {
					return;
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

				models.forEach(model -> websocketUtils.sendMessage(WEBSOCKET_MODELS_URL + model, null));
			}

		}

	}

	private class ElasticSearchOperation implements Comparable<ElasticSearchOperation> {
		private Long transactionId;
		private Long seqNum;
		private ITableDto dtoPk;
		private DocumentOperationType operation;
		private FilterGroup pkFilter;
		private String view;
		private String toString;

		public ElasticSearchOperation() {
			this.transactionId = Long.MAX_VALUE;
			this.seqNum = Long.MAX_VALUE;
			this.toString = "FINALIZER_OBJECT";
		}

		public ElasticSearchOperation(ITableDto dtoPk, Class<? extends IViewDto> viewDtoClass,
				DocumentOperationType operation, FilterGroup pkFilter, String view, Long transactionId, Long seqNum) {
			this.transactionId = transactionId;
			this.seqNum = seqNum;
			this.dtoPk = dtoPk;
			this.operation = operation;
			this.pkFilter = pkFilter;
			this.view = view;

			StringBuilder sb = new StringBuilder();
			sb.append("View: ");
			sb.append(viewDtoClass.getSimpleName());
			sb.append("; Table: ");
			sb.append(dtoPk.getClass().getSimpleName());
			sb.append("; PK: ");
			sb.append(dtoPk);
			sb.append("; Operation: ");
			sb.append(operation);
			sb.append("; TransactionId: ");
			sb.append(transactionId != null ? transactionId : "NT");
			sb.append("; SequenceNum: ");
			sb.append(seqNum);

			toString = sb.toString();
		}

		private void process() throws PuiException {
			if (debug) {
				logger.debug("*** Process: " + toString);
			}

			switch (operation) {
			case insert:
				documentService.insertDocument(dtoPk, view, pkFilter);
				break;
			case update:
				documentService.updateDocument(dtoPk, view, pkFilter);
				break;
			case delete:
				documentService.deleteDocument(dtoPk, view, pkFilter);
				break;
			}
		}

		@Override
		public int compareTo(ElasticSearchOperation o) {
			if (transactionId == null && o.transactionId != null) {
				return -1;
			} else if (transactionId != null && o.transactionId == null) {
				return 1;
			} else if (transactionId == null && o.transactionId == null) {
				if (seqNum < o.seqNum) {
					return -1;
				} else if (seqNum > o.seqNum) {
					return 1;
				} else {
					return 0;
				}
			} else if (transactionId != null && o.transactionId != null) {
				if (transactionId < o.transactionId) {
					return -1;
				} else if (transactionId > o.transactionId) {
					return 1;
				} else {
					if (seqNum < o.seqNum) {
						return -1;
					} else if (seqNum > o.seqNum) {
						return 1;
					} else {
						return 0;
					}
				}
			} else {
				return 0;
			}
		}

		@Override
		public String toString() {
			return toString;
		}

	}

}
