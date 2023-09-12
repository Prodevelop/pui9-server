package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiSubsystem;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiSubsystemDao extends IViewDao<IVPuiSubsystem> {
	@PuiGenerated
	java.util.List<IVPuiSubsystem> findBySubsystem(String subsystem) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiSubsystem> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiSubsystem> findByLang(String lang) throws PuiDaoFindException;
}
