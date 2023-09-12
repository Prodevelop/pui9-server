package es.prodevelop.pui9.docgen.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenModel;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenModelPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiDocgenModelDao extends ITableDao<IPuiDocgenModelPk, IPuiDocgenModel> {
	@PuiGenerated
	java.util.List<IPuiDocgenModel> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocgenModel> findByLabel(String label) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocgenModel> findByIdentityfields(String identityfields) throws PuiDaoFindException;
}
