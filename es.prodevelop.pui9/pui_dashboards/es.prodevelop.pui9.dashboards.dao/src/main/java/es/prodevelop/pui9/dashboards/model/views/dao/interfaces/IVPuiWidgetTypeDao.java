package es.prodevelop.pui9.dashboards.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiWidgetType;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiWidgetTypeDao extends IViewDao<IVPuiWidgetType> {
	@PuiGenerated
	java.util.List<IVPuiWidgetType> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiWidgetType> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiWidgetType> findByType(String type) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiWidgetType> findByComponent(String component) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiWidgetType> findByDefinition(String definition) throws PuiDaoFindException;
}