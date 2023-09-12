package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiMultiInstanceProcessDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMultiInstanceProcess;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMultiInstanceProcessPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiMultiInstanceProcessDao extends AbstractTableDao<IPuiMultiInstanceProcessPk, IPuiMultiInstanceProcess>
		implements IPuiMultiInstanceProcessDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiMultiInstanceProcess> findById(String id) throws PuiDaoFindException {
		return super.findByColumn(IPuiMultiInstanceProcessPk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiMultiInstanceProcess> findByPeriod(Integer period) throws PuiDaoFindException {
		return super.findByColumn(IPuiMultiInstanceProcess.PERIOD_FIELD, period);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiMultiInstanceProcess> findByTimeunit(String timeunit) throws PuiDaoFindException {
		return super.findByColumn(IPuiMultiInstanceProcess.TIME_UNIT_FIELD, timeunit);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiMultiInstanceProcess> findByInstanceassigneeuuid(String instanceassigneeuuid)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiMultiInstanceProcess.INSTANCE_ASSIGNEE_UUID_FIELD, instanceassigneeuuid);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiMultiInstanceProcess> findByLatestexecution(java.time.Instant latestexecution)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiMultiInstanceProcess.LATEST_EXECUTION_FIELD, latestexecution);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiMultiInstanceProcess> findByLatestheartbeat(java.time.Instant latestheartbeat)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiMultiInstanceProcess.LATEST_HEARTBEAT_FIELD, latestheartbeat);
	}
}