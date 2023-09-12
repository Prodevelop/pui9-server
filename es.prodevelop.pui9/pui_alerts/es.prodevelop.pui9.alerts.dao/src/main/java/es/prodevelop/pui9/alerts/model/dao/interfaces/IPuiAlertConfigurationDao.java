package es.prodevelop.pui9.alerts.model.dao.interfaces;

import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfiguration;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfigurationPk;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiAlertConfigurationDao extends ITableDao<IPuiAlertConfigurationPk, IPuiAlertConfiguration> {
	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByDescription(String description) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByColumnname(String columnname) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByType(String type) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByTimeunit(String timeunit) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByTimevalue(Integer timevalue) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByTimebeforeafter(String timebeforeafter) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByContent(String content) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByEmails(String emails) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByUsr(String usr) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAlertConfiguration> findByIscontenthtml(Integer iscontenthtml) throws PuiDaoFindException;
}
