package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSession;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSessionPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiSessionDao extends ITableDao<IPuiSessionPk, IPuiSession> {
	@PuiGenerated
	java.util.List<IPuiSession> findByExpiration(java.time.Instant expiration) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiSession> findByPersistent(Integer persistent) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiSession> findByLastuse(java.time.Instant lastuse) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiSession> findByJwt(String jwt) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiSession> findByUuid(String uuid) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiSession> findByUsr(String usr) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiSession> findByCreated(java.time.Instant created) throws PuiDaoFindException;
}
