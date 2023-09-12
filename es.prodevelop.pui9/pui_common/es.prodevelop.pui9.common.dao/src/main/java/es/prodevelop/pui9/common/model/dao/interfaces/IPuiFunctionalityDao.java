package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiFunctionalityDao extends ITableDao<IPuiFunctionalityPk, IPuiFunctionality> {
	@PuiGenerated
	java.util.List<IPuiFunctionality> findByFunctionality(String functionality) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiFunctionality> findBySubsystem(String subsystem) throws PuiDaoFindException;
}
