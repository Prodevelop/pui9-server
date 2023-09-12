package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiProfile;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiProfileDao extends IViewDao<IVPuiProfile> {
	@PuiGenerated
	java.util.List<IVPuiProfile> findByProfile(String profile) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiProfile> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiProfile> findByLang(String lang) throws PuiDaoFindException;
}
