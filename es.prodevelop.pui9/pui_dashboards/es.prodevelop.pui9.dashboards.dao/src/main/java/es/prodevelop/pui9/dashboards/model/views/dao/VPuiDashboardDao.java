package es.prodevelop.pui9.dashboards.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.views.dao.interfaces.IVPuiDashboardDao;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiDashboard;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiDashboardDao extends AbstractViewDao<IVPuiDashboard> implements IVPuiDashboardDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiDashboard> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDashboard.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDashboard> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDashboard.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDashboard> findByDefinition(String definition) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDashboard.DEFINITION_FIELD, definition);
	}
}