package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiUserFunctionalityDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUserFunctionality;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiUserFunctionalityDao extends AbstractViewDao<IVPuiUserFunctionality>
		implements IVPuiUserFunctionalityDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiUserFunctionality> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUserFunctionality.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiUserFunctionality> findByFunctionality(String functionality) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUserFunctionality.FUNCTIONALITY_FIELD, functionality);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiUserFunctionality> findByFunctionalityname(String functionalityname)
			throws PuiDaoFindException {
		return super.findByColumn(IVPuiUserFunctionality.FUNCTIONALITY_NAME_FIELD, functionalityname);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiUserFunctionality> findByProfile(String profile) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUserFunctionality.PROFILE_FIELD, profile);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiUserFunctionality> findByProfilename(String profilename) throws PuiDaoFindException {
		return super.findByColumn(IVPuiUserFunctionality.PROFILE_NAME_FIELD, profilename);
	}
}
