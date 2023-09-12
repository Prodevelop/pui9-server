package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMultiInstanceProcess;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiMultiInstanceProcessPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiMultiInstanceProcessDao extends ITableDao<IPuiMultiInstanceProcessPk, IPuiMultiInstanceProcess> {
	@PuiGenerated
	java.util.List<IPuiMultiInstanceProcess> findById(String id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMultiInstanceProcess> findByPeriod(Integer period) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMultiInstanceProcess> findByTimeunit(String timeunit) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMultiInstanceProcess> findByInstanceassigneeuuid(String instanceassigneeuuid)
			throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMultiInstanceProcess> findByLatestexecution(java.time.Instant latestexecution)
			throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiMultiInstanceProcess> findByLatestheartbeat(java.time.Instant latestheartbeat)
			throws PuiDaoFindException;
}