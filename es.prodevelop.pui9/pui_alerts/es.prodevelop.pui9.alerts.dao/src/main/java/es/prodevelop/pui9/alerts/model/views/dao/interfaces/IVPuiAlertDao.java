package es.prodevelop.pui9.alerts.model.views.dao.interfaces;

import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlert;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiAlertDao extends IViewDao<IVPuiAlert> {
	@PuiGenerated
	java.util.List<IVPuiAlert> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlert> findByDescription(String description) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlert> findByType(String type) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlert> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlert> findByColumnname(String columnname) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlert> findByPk(String pk) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlert> findByProcessed(Integer processed) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlert> findByRead(Integer read) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlert> findByContent(String content) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlert> findByEmails(String emails) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlert> findByLaunchingdatetime(java.time.Instant launchingdatetime) throws PuiDaoFindException;
}
