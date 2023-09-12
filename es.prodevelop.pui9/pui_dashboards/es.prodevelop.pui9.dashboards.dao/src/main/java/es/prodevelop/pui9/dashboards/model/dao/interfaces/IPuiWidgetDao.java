package es.prodevelop.pui9.dashboards.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidget;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidgetPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiWidgetDao extends ITableDao<IPuiWidgetPk, IPuiWidget> {
	@PuiGenerated
	java.util.List<IPuiWidget> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiWidget> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiWidget> findByTypeid(Integer typeid) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiWidget> findByDefinition(String definition) throws PuiDaoFindException;
}