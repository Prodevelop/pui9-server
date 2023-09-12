package es.prodevelop.pui9.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.model.dao.elasticsearch.PuiElasticSearchManager;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

/**
 * Implementation of the API {@link IPuiElasticSearchEnablement}
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticSearchEnablement implements IPuiElasticSearchEnablement {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private PuiElasticSearchManager manager;

	private List<Class<? extends IViewDto>> blockedViews = new ArrayList<>();
	private List<Class<? extends IViewDto>> indexableViews = new ArrayList<>();
	private List<Class<? extends IViewDto>> synchronizingViews = new ArrayList<>();

	@Override
	public boolean isElasticSearchAvailable() {
		return manager.isConnected();
	}

	@Override
	public void setElasticSearchActive(boolean active) {
		logger.debug("Elastic Search active status changed to '" + active + "'");

		if (!Objects.equals(isElasticSearchActive(), active)) {
			variableService.modifyVariable(PuiVariableValues.ELASTICSEARCH_ACTIVE.name(), String.valueOf(active));
			try {
				// wait some time to update the variable into elastic before disconnecting
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			if (active) {
				manager.reconnect();
			}
		}

		if (active) {
			blockedViews.clear();
			synchronizingViews.clear();
		}
	}

	@Override
	public boolean isElasticSearchActive() {
		if (!variableService.getVariable(Boolean.class, PuiVariableValues.ELASTICSEARCH_ACTIVE.name()).booleanValue()) {
			return false;
		}

		return manager.isConnected();
	}

	@Override
	public boolean isSynchronizingAnyView() {
		return !synchronizingViews.isEmpty();
	}

	@Override
	public boolean isSynchronizingView(Class<? extends IViewDto> dtoClass) {
		return synchronizingViews.contains(dtoClass);
	}

	@Override
	public void addSynchronizingView(Class<? extends IViewDto> dtoClass) {
		if (dtoClass != null && !synchronizingViews.contains(dtoClass)) {
			synchronizingViews.add(dtoClass);
			addBlockedView(dtoClass);
		}
	}

	@Override
	public void removeSynchronizingView(Class<? extends IViewDto> dtoClass) {
		if (dtoClass != null && synchronizingViews.contains(dtoClass)) {
			synchronizingViews.remove(dtoClass);
			removeBlockedView(dtoClass);
		}
	}

	@Override
	public void addBlockedView(Class<? extends IViewDto> dtoClass) {
		if (dtoClass != null && !blockedViews.contains(dtoClass)) {
			logger.debug("View " + dtoClass.getSimpleName() + " is added to black list in Elastic Search");
			blockedViews.add(dtoClass);
		}
	}

	@Override
	public void removeBlockedView(Class<? extends IViewDto> dtoClass) {
		if (dtoClass != null && blockedViews.contains(dtoClass)) {
			logger.debug("View " + dtoClass.getSimpleName() + " is removed from black list in Elastic Search");
			blockedViews.remove(dtoClass);
		}
	}

	@Override
	public boolean isViewBlocked(Class<? extends IViewDto> dtoClass) {
		if (dtoClass == null) {
			return false;
		}
		return blockedViews.contains(dtoClass);
	}

	@Override
	public void addIndexableView(Class<? extends IViewDto> dtoClass) {
		if (dtoClass != null && !indexableViews.contains(dtoClass)) {
			indexableViews.add(dtoClass);
		}
	}

	@Override
	public void removeIndexableView(Class<? extends IViewDto> dtoClass) {
		if (dtoClass != null && indexableViews.contains(dtoClass)) {
			indexableViews.remove(dtoClass);
		}
	}

	@Override
	public boolean isViewIndexable(Class<? extends IViewDto> dtoClass) {
		if (dtoClass == null) {
			return false;
		}
		return indexableViews.contains(dtoClass);
	}

}
