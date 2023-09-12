package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiAuditDao extends ITableDao<IPuiAuditPk, IPuiAudit> {
	@PuiGenerated
	java.util.List<IPuiAudit> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAudit> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAudit> findByType(String type) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAudit> findByPk(String pk) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAudit> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAudit> findByUsr(String usr) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAudit> findByIp(String ip) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAudit> findByContent(String content) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiAudit> findByClient(String client) throws PuiDaoFindException;
}
