package es.prodevelop.pui9.notifications.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;
import es.prodevelop.pui9.notifications.model.dao.interfaces.IPuiUserFcmDao;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcmPk;

@PuiGenerated
@Repository
public class PuiUserFcmDao extends AbstractTableDao<IPuiUserFcmPk, IPuiUserFcm> implements IPuiUserFcmDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiUserFcm> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserFcm.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserFcm> findByToken(String token) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserFcmPk.TOKEN_FIELD, token);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserFcm> findByLastuse(java.time.Instant lastuse) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserFcm.LAST_USE_FIELD, lastuse);
	}
}
