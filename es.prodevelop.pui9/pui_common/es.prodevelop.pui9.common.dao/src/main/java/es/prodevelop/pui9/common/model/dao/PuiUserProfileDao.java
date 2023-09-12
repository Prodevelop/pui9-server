package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserProfileDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfile;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfilePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiUserProfileDao extends AbstractTableDao<IPuiUserProfilePk, IPuiUserProfile>
		implements IPuiUserProfileDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiUserProfile> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserProfilePk.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserProfile> findByProfile(String profile) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserProfilePk.PROFILE_FIELD, profile);
	}
}
