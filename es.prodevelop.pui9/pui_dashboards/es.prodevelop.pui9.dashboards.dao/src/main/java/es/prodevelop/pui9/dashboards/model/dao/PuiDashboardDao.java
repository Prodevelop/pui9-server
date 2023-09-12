package es.prodevelop.pui9.dashboards.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.dao.interfaces.IPuiDashboardDao;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboard;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboardPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiDashboardDao extends AbstractTableDao<IPuiDashboardPk, IPuiDashboard> implements IPuiDashboardDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiDashboard> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiDashboardPk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDashboard> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiDashboard.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDashboard> findByDefinition(String definition) throws PuiDaoFindException {
		return super.findByColumn(IPuiDashboard.DEFINITION_FIELD, definition);
	}
}