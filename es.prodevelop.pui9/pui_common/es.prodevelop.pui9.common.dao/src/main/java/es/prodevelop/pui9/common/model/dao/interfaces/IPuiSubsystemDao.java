package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystem;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystemPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiSubsystemDao extends ITableDao<IPuiSubsystemPk, IPuiSubsystem> {
	@PuiGenerated
	java.util.List<IPuiSubsystem> findBySubsystem(String subsystem) throws PuiDaoFindException;
}
