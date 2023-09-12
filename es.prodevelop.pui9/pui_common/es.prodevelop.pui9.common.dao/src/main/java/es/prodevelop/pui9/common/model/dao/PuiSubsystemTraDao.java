package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiSubsystemTraDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystemTra;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystemTraPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiSubsystemTraDao extends AbstractTableDao<IPuiSubsystemTraPk, IPuiSubsystemTra>
		implements IPuiSubsystemTraDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiSubsystemTra> findBySubsystem(String subsystem) throws PuiDaoFindException {
		return super.findByColumn(IPuiSubsystemTraPk.SUBSYSTEM_FIELD, subsystem);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiSubsystemTra> findByLang(String lang) throws PuiDaoFindException {
		return super.findByColumn(IPuiSubsystemTraPk.LANG_FIELD, lang);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiSubsystemTra> findByLangstatus(Integer langstatus) throws PuiDaoFindException {
		return super.findByColumn(IPuiSubsystemTra.LANG_STATUS_FIELD, langstatus);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiSubsystemTra> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiSubsystemTra.NAME_FIELD, name);
	}
}
