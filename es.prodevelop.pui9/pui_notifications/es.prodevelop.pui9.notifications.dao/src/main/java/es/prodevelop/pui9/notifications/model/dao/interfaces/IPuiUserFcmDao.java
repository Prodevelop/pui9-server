package es.prodevelop.pui9.notifications.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcmPk;

@PuiGenerated
public interface IPuiUserFcmDao extends ITableDao<IPuiUserFcmPk, IPuiUserFcm> {
	@PuiGenerated
	java.util.List<IPuiUserFcm> findByUsr(String usr) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUserFcm> findByToken(String token) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUserFcm> findByLastuse(java.time.Instant lastuse) throws PuiDaoFindException;
}
