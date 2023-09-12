package es.prodevelop.pui9.dashboards.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.views.dao.interfaces.IVPuiWidgetDao;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiWidget;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiWidgetDao extends AbstractViewDao<IVPuiWidget> implements IVPuiWidgetDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidget> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidget.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidget> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidget.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidget> findByTypeid(Integer typeid) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidget.TYPEID_FIELD, typeid);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidget> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidget.TYPE_FIELD, type);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidget> findByComponent(String component) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidget.COMPONENT_FIELD, component);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiWidget> findByDefinition(String definition) throws PuiDaoFindException {
		return super.findByColumn(IVPuiWidget.DEFINITION_FIELD, definition);
	}
}