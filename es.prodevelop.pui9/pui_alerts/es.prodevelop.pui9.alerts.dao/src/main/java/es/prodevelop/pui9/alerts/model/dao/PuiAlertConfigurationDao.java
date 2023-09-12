package es.prodevelop.pui9.alerts.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.alerts.model.dao.interfaces.IPuiAlertConfigurationDao;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfiguration;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfigurationPk;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@Repository
@PuiGenerated
public class PuiAlertConfigurationDao extends AbstractTableDao<IPuiAlertConfigurationPk, IPuiAlertConfiguration>
		implements IPuiAlertConfigurationDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfigurationPk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.DESCRIPTION_FIELD, description);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByColumnname(String columnname) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.COLUMN_NAME_FIELD, columnname);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.TYPE_FIELD, type);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByTimeunit(String timeunit) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.TIME_UNIT_FIELD, timeunit);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByTimevalue(Integer timevalue) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.TIME_VALUE_FIELD, timevalue);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByTimebeforeafter(String timebeforeafter)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.TIME_BEFORE_AFTER_FIELD, timebeforeafter);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByContent(String content) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.CONTENT_FIELD, content);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByEmails(String emails) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.EMAILS_FIELD, emails);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByDatetime(java.time.Instant datetime)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.DATETIME_FIELD, datetime);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAlertConfiguration> findByIscontenthtml(Integer iscontenthtml)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiAlertConfiguration.IS_CONTENT_HTML_FIELD, iscontenthtml);
	}
}
