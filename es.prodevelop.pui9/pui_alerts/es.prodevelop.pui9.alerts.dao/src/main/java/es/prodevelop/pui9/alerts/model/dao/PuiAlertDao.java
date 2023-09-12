package es.prodevelop.pui9.alerts.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.alerts.model.dao.interfaces.IPuiAlertDao;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertPk;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@Repository
@PuiGenerated
public class PuiAlertDao extends AbstractTableDao<IPuiAlertPk, IPuiAlert> implements IPuiAlertDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiAlert> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertPk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlert> findByAlertconfigid(Integer alertconfigid) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlert.ALERT_CONFIG_ID_FIELD, alertconfigid);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlert> findByPk(String pk) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlert.PK_FIELD, pk);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlert> findByProcessed(Integer processed) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlert.PROCESSED_FIELD, processed);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlert> findByLaunchingdatetime(java.time.Instant launchingdatetime)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiAlert.LAUNCHING_DATETIME_FIELD, launchingdatetime);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlert> findByRead(Integer read) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlert.READ_FIELD, read);
	}
}
