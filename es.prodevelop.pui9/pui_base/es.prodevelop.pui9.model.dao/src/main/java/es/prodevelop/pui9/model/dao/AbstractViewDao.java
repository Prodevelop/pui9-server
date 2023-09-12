package es.prodevelop.pui9.model.dao;

import org.springframework.beans.factory.annotation.Autowired;

import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchSearchException;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchSearchingService;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;

/**
 * This abstract class provides the implementation of the all the View DAO for
 * JDBC approach. It implements {@link IViewDao} interface for bringing the
 * necessary methods to manage the views
 * 
 * @param <T> The whole {@link IDto} class that represents this DAO Class
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractViewDao<T extends IViewDto> extends AbstractDatabaseDao<T> implements IViewDao<T> {

	@Autowired(required = false)
	private IPuiElasticSearchEnablement elasticSearchEnablement;

	@Autowired(required = false)
	private IPuiElasticSearchSearchingService elasticSearchSearchingService;

	@Override
	public SearchResponse<T> findPaginated(SearchRequest req) throws PuiDaoListException {
		if (req.isFromClient() && PuiUserSession.getCurrentSession() != null) {
			req.setZoneId(PuiUserSession.getCurrentSession().getZoneId());
		}
		if (req.getDtoClass() == null) {
			req.setDtoClass(getDtoClass());
		}
		if (elasticSearchEnablement != null && elasticSearchEnablement.isViewIndexable(getDtoClass())
				&& elasticSearchEnablement.isElasticSearchAvailable()
				&& elasticSearchEnablement.isElasticSearchActive()) {
			try {
				return elasticSearchSearchingService.findPaginated(req);
			} catch (PuiElasticSearchSearchException e) {
				return super.findPaginated(req);
			}
		} else {
			return super.findPaginated(req);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends IViewDao<T>> getDaoClass() {
		return (Class<? extends IViewDao<T>>) super.getDaoClass();
	}

}