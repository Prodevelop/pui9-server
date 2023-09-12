package es.prodevelop.pui9.dashboards.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiDashboard;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiDashboardDao extends IViewDao<IVPuiDashboard> {
	@PuiGenerated
	java.util.List<IVPuiDashboard> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDashboard> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDashboard> findByDefinition(String definition) throws PuiDaoFindException;
}