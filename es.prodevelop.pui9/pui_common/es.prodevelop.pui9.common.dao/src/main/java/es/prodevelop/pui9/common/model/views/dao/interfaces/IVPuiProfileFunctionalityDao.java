package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiProfileFunctionality;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiProfileFunctionalityDao extends IViewDao<IVPuiProfileFunctionality> {
	@PuiGenerated
	java.util.List<IVPuiProfileFunctionality> findByProfile(String profile) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiProfileFunctionality> findByProfilename(String profilename) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiProfileFunctionality> findByFunctionality(String functionality) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiProfileFunctionality> findByLang(String lang) throws PuiDaoFindException;
}
