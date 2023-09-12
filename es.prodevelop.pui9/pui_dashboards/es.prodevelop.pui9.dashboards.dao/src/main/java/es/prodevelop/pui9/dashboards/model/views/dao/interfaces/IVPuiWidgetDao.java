package es.prodevelop.pui9.dashboards.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiWidget;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiWidgetDao extends IViewDao<IVPuiWidget> {
	@PuiGenerated
	java.util.List<IVPuiWidget> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiWidget> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiWidget> findByTypeid(Integer typeid) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiWidget> findByType(String type) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiWidget> findByComponent(String component) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiWidget> findByDefinition(String definition) throws PuiDaoFindException;
}