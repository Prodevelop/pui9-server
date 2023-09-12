package es.prodevelop.pui9.alerts.model.views.dao.interfaces;

import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlertConfiguration;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiAlertConfigurationDao extends IViewDao<IVPuiAlertConfiguration> {
	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByDescription(String description) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByColumnname(String columnname) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByType(String type) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByTimeunit(String timeunit) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByTimevalue(Integer timevalue) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByTimebeforeafter(String timebeforeafter) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByContent(String content) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByEmails(String emails) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByUsr(String usr) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAlertConfiguration> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;
}
