package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiLanguageDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLanguage;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiLanguageDao extends AbstractViewDao<IVPuiLanguage> implements IVPuiLanguageDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiLanguage> findByIsocode(String isocode) throws PuiDaoFindException {
		return super.findByColumn(IVPuiLanguage.ISOCODE_FIELD, isocode);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiLanguage> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IVPuiLanguage.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiLanguage> findByIsdefault(Integer isdefault) throws PuiDaoFindException {
		return super.findByColumn(IVPuiLanguage.ISDEFAULT_FIELD, isdefault);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiLanguage> findByEnabled(Integer enabled) throws PuiDaoFindException {
		return super.findByColumn(IVPuiLanguage.ENABLED_FIELD, enabled);
	}
}
