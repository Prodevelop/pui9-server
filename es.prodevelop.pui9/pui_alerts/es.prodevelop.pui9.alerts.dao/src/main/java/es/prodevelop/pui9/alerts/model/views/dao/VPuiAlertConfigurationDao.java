package es.prodevelop.pui9.alerts.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.alerts.model.views.dao.interfaces.IVPuiAlertConfigurationDao;
import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlertConfiguration;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiAlertConfigurationDao extends AbstractViewDao<IVPuiAlertConfiguration>
		implements IVPuiAlertConfigurationDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.DESCRIPTION_FIELD, description);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByColumnname(String columnname) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.COLUMN_NAME_FIELD, columnname);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.TYPE_FIELD, type);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByTimeunit(String timeunit) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.TIME_UNIT_FIELD, timeunit);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByTimevalue(Integer timevalue) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.TIME_VALUE_FIELD, timevalue);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByTimebeforeafter(String timebeforeafter)
			throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.TIME_BEFORE_AFTER_FIELD, timebeforeafter);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByContent(String content) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.CONTENT_FIELD, content);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByEmails(String emails) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.EMAILS_FIELD, emails);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlertConfiguration> findByDatetime(java.time.Instant datetime)
			throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlertConfiguration.DATETIME_FIELD, datetime);
	}
}
