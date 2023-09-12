package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilterPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiUserModelFilterDao extends ITableDao<IPuiUserModelFilterPk, IPuiUserModelFilter> {
	@PuiGenerated
	java.util.List<IPuiUserModelFilter> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUserModelFilter> findByUsr(String usr) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUserModelFilter> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUserModelFilter> findByLabel(String label) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUserModelFilter> findByIsdefault(Integer isdefault) throws PuiDaoFindException;
}
