package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLanguage;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiLanguageDao extends IViewDao<IVPuiLanguage> {
	@PuiGenerated
	java.util.List<IVPuiLanguage> findByIsocode(String isocode) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiLanguage> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiLanguage> findByIsdefault(Integer isdefault) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiLanguage> findByEnabled(Integer enabled) throws PuiDaoFindException;
}
