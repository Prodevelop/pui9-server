package es.prodevelop.pui9.dashboards.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.dao.interfaces.IPuiWidgetTypeDao;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidgetType;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidgetTypePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiWidgetTypeDao extends AbstractTableDao<IPuiWidgetTypePk, IPuiWidgetType> implements IPuiWidgetTypeDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiWidgetType> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiWidgetTypePk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiWidgetType> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiWidgetType.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiWidgetType> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IPuiWidgetType.TYPE_FIELD, type);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiWidgetType> findByComponent(String component) throws PuiDaoFindException {
		return super.findByColumn(IPuiWidgetType.COMPONENT_FIELD, component);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiWidgetType> findByDefinition(String definition) throws PuiDaoFindException {
		return super.findByColumn(IPuiWidgetType.DEFINITION_FIELD, definition);
	}
}