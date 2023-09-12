package es.prodevelop.pui9.dashboards.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidgetType;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidgetTypePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiWidgetTypeDao extends ITableDao<IPuiWidgetTypePk, IPuiWidgetType> {
	@PuiGenerated
	java.util.List<IPuiWidgetType> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiWidgetType> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiWidgetType> findByType(String type) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiWidgetType> findByComponent(String component) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiWidgetType> findByDefinition(String definition) throws PuiDaoFindException;
}