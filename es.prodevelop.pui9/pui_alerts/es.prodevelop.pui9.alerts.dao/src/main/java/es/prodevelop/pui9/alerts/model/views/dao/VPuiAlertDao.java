package es.prodevelop.pui9.alerts.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.alerts.model.views.dao.interfaces.IVPuiAlertDao;
import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlert;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@Repository
@PuiGenerated
public class VPuiAlertDao extends AbstractViewDao<IVPuiAlert> implements IVPuiAlertDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.DESCRIPTION_FIELD, description);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.TYPE_FIELD, type);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findByColumnname(String columnname) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.COLUMN_NAME_FIELD, columnname);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findByPk(String pk) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.PK_FIELD, pk);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findByProcessed(Integer processed) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.PROCESSED_FIELD, processed);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findByRead(Integer read) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.READ_FIELD, read);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findByContent(String content) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.CONTENT_FIELD, content);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findByEmails(String emails) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.EMAILS_FIELD, emails);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAlert> findByLaunchingdatetime(java.time.Instant launchingdatetime)
			throws PuiDaoFindException {
		return super.findByColumn(IVPuiAlert.LAUNCHING_DATETIME_FIELD, launchingdatetime);
	}
}
