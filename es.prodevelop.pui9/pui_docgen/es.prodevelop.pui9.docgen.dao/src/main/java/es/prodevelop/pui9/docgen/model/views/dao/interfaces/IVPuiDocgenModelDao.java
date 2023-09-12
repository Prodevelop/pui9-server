package es.prodevelop.pui9.docgen.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenModel;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiDocgenModelDao extends IViewDao<IVPuiDocgenModel> {
	@PuiGenerated
	java.util.List<IVPuiDocgenModel> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocgenModel> findByEntity(String entity) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocgenModel> findByLabel(String label) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocgenModel> findByIdentityfields(String identityfields) throws PuiDaoFindException;
}
