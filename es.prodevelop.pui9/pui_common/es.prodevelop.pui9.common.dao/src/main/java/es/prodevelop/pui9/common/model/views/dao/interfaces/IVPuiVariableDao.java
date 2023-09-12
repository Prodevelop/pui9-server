package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiVariable;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiVariableDao extends IViewDao<IVPuiVariable> {
	@PuiGenerated
	java.util.List<IVPuiVariable> findByVariable(String variable) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiVariable> findByValue(String value) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiVariable> findByDescription(String description) throws PuiDaoFindException;
}
