package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiSubsystemDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiSubsystem;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiSubsystemDao extends AbstractViewDao<IVPuiSubsystem> implements IVPuiSubsystemDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiSubsystem> findBySubsystem(String subsystem) throws PuiDaoFindException {
		return super.findByColumn(IVPuiSubsystem.SUBSYSTEM_FIELD, subsystem);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiSubsystem> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IVPuiSubsystem.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiSubsystem> findByLang(String lang) throws PuiDaoFindException {
		return super.findByColumn(IVPuiSubsystem.LANG_FIELD, lang);
	}
}
