package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenu;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMenuPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiMenuDao extends ITableDao<IPuiMenuPk, IPuiMenu> {
	@PuiGenerated
	java.util.List<IPuiMenu> findByNode(Integer node) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMenu> findByParent(Integer parent) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMenu> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMenu> findByComponent(String component) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMenu> findByFunctionality(String functionality) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMenu> findByLabel(String label) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMenu> findByIconlabel(String iconlabel) throws PuiDaoFindException;
}
