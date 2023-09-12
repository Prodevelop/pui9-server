package es.prodevelop.pui9.dashboards.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.dao.interfaces.IPuiWidgetDao;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidget;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidgetPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiWidgetDao extends AbstractTableDao<IPuiWidgetPk, IPuiWidget> implements IPuiWidgetDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiWidget> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiWidgetPk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiWidget> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiWidget.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiWidget> findByTypeid(Integer typeid) throws PuiDaoFindException {
		return super.findByColumn(IPuiWidget.TYPEID_FIELD, typeid);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiWidget> findByDefinition(String definition) throws PuiDaoFindException {
		return super.findByColumn(IPuiWidget.DEFINITION_FIELD, definition);
	}
}