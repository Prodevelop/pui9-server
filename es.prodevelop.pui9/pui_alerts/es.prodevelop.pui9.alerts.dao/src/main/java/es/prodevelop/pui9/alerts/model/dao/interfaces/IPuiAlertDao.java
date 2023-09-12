package es.prodevelop.pui9.alerts.model.dao.interfaces;

import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertPk;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiAlertDao extends ITableDao<IPuiAlertPk, IPuiAlert> {
	@PuiGenerated
	java.util.List<IPuiAlert> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlert> findByAlertconfigid(Integer alertconfigid) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlert> findByPk(String pk) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlert> findByProcessed(Integer processed) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlert> findByLaunchingdatetime(java.time.Instant launchingdatetime) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlert> findByRead(Integer read) throws PuiDaoFindException;
}
