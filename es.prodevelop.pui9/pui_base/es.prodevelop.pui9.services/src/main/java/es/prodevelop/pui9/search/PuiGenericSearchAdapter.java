package es.prodevelop.pui9.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.service.interfaces.IService;
import es.prodevelop.pui9.service.registry.ServiceRegistry;

/**
 * This is the default implementation for the search adapter for PUI9. It uses
 * directly the Service to perform the search, without taking into account
 * filters, orders and nothing configured in any table of PUI9
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiGenericSearchAdapter implements IPuiSearchAdapter {

	@Autowired
	private ServiceRegistry serviceRegistry;

	@Autowired
	private DaoRegistry daoRegistry;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <TYPE> SearchResponse<TYPE> search(SearchRequest req) throws PuiServiceGetException {
		if (ObjectUtils.isEmpty(req.getQueryLang())) {
			req.setQueryLang(PuiUserSession.getSessionLanguage().getIsocode());
		}

		req.setDtoClass(daoRegistry.getViewDtoFromModelId(req.getModel()));
		if (req.getDtoClass() == null) {
			req.setDtoClass(daoRegistry.getTableDtoFromModelId(req.getModel(), false));
		}
		if (req.getDtoClass() == null) {
			req.setDtoClass(DtoRegistry.getDtoFromEntity(req.getModel()));
		}

		Class<? extends IService> serviceClass = null;
		Class<? extends IDao> daoClass = null;
		if (req.getDtoClass() != null) {
			serviceClass = serviceRegistry.getServiceFromDto(req.getDtoClass());
			daoClass = daoRegistry.getDaoFromDto(req.getDtoClass());
		}

		if (serviceClass != null) {
			IService service = PuiApplicationContext.getInstance().getBean(serviceClass);
			if (IViewDto.class.isAssignableFrom(req.getDtoClass())) {
				return service.searchView(req);
			} else if (ITableDto.class.isAssignableFrom(req.getDtoClass())) {
				return service.searchTable(req);
			} else {
				return new SearchResponse<>();
			}
		}

		if (daoClass != null) {
			IDao dao = PuiApplicationContext.getInstance().getBean(daoClass);
			try {
				return dao.findPaginated(req);
			} catch (PuiDaoListException e) {
				throw new PuiServiceGetException(e);
			}
		}

		return new SearchResponse<>();
	}

}