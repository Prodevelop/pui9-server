package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiProfileDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiProfile;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiProfileDao extends AbstractViewDao<IVPuiProfile> implements IVPuiProfileDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiProfile> findByProfile(String profile) throws PuiDaoFindException {
		return super.findByColumn(IVPuiProfile.PROFILE_FIELD, profile);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiProfile> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IVPuiProfile.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiProfile> findByLang(String lang) throws PuiDaoFindException {
		return super.findByColumn(IVPuiProfile.LANG_FIELD, lang);
	}
}
