package es.prodevelop.pui9.dashboards.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.views.dao.interfaces.IVPuiWidgetTypeDao;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiWidgetType;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiWidgetTypeDao extends AbstractViewDao<IVPuiWidgetType> implements IVPuiWidgetTypeDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidgetType> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidgetType.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidgetType> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidgetType.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidgetType> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidgetType.TYPE_FIELD, type);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidgetType> findByComponent(String component) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidgetType.COMPONENT_FIELD, component);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidgetType> findByDefinition(String definition) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidgetType.DEFINITION_FIELD, definition);
	}
}