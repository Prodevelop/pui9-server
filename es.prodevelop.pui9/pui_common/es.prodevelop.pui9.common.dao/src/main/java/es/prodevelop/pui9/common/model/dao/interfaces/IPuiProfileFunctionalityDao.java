package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileFunctionalityPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiProfileFunctionalityDao extends ITableDao<IPuiProfileFunctionalityPk, IPuiProfileFunctionality> {
	@PuiGenerated
	java.util.List<IPuiProfileFunctionality> findByProfile(String profile) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiProfileFunctionality> findByFunctionality(String functionality) throws PuiDaoFindException;
}
