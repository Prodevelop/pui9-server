package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilterPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiModelFilterDao extends ITableDao<IPuiModelFilterPk, IPuiModelFilter> {
	@PuiGenerated
	java.util.List<IPuiModelFilter> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiModelFilter> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiModelFilter> findByLabel(String label) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiModelFilter> findByDescription(String description) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiModelFilter> findByFilter(String filter) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiModelFilter> findByIsdefault(Integer isdefault) throws PuiDaoFindException;
}
