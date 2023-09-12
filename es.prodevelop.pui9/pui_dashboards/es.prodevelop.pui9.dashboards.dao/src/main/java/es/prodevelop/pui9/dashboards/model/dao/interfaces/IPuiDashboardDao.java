package es.prodevelop.pui9.dashboards.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboard;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboardPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiDashboardDao extends ITableDao<IPuiDashboardPk, IPuiDashboard> {
	@PuiGenerated
	java.util.List<IPuiDashboard> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDashboard> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDashboard> findByDefinition(String definition) throws PuiDaoFindException;
}