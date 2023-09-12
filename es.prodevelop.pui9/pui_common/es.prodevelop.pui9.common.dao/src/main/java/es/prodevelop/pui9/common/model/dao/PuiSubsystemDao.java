package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiSubsystemDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystem;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiSubsystemPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiSubsystemDao extends AbstractTableDao<IPuiSubsystemPk, IPuiSubsystem> implements IPuiSubsystemDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiSubsystem> findBySubsystem(String subsystem) throws PuiDaoFindException {
		return super.findByColumn(IPuiSubsystemPk.SUBSYSTEM_FIELD, subsystem);
	}
}
