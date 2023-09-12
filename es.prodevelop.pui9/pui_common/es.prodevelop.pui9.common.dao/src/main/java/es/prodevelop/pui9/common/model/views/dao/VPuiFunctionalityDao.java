package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiFunctionalityDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiFunctionality;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiFunctionalityDao extends AbstractViewDao<IVPuiFunctionality> implements IVPuiFunctionalityDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiFunctionality> findByFunctionality(String functionality) throws PuiDaoFindException {
		return super.findByColumn(IVPuiFunctionality.FUNCTIONALITY_FIELD, functionality);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiFunctionality> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IVPuiFunctionality.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiFunctionality> findBySubsystem(String subsystem) throws PuiDaoFindException {
		return super.findByColumn(IVPuiFunctionality.SUBSYSTEM_FIELD, subsystem);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiFunctionality> findBySubsystemname(String subsystemname) throws PuiDaoFindException {
		return super.findByColumn(IVPuiFunctionality.SUBSYSTEM_NAME_FIELD, subsystemname);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiFunctionality> findByLang(String lang) throws PuiDaoFindException {
		return super.findByColumn(IVPuiFunctionality.LANG_FIELD, lang);
	}
}
