package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiSessionDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSession;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSessionPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiSessionDao extends AbstractTableDao<IPuiSessionPk, IPuiSession> implements IPuiSessionDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiSession> findByExpiration(java.time.Instant expiration) throws PuiDaoFindException {
		return super.findByColumn(IPuiSession.EXPIRATION_FIELD, expiration);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiSession> findByPersistent(Integer persistent) throws PuiDaoFindException {
		return super.findByColumn(IPuiSession.PERSISTENT_FIELD, persistent);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiSession> findByLastuse(java.time.Instant lastuse) throws PuiDaoFindException {
		return super.findByColumn(IPuiSession.LASTUSE_FIELD, lastuse);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiSession> findByJwt(String jwt) throws PuiDaoFindException {
		return super.findByColumn(IPuiSession.JWT_FIELD, jwt);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiSession> findByUuid(String uuid) throws PuiDaoFindException {
		return super.findByColumn(IPuiSessionPk.UUID_FIELD, uuid);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiSession> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiSession.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiSession> findByCreated(java.time.Instant created) throws PuiDaoFindException {
		return super.findByColumn(IPuiSession.CREATED_FIELD, created);
	}
}
