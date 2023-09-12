package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiAudit;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiAuditDao extends IViewDao<IVPuiAudit> {
	@PuiGenerated
	java.util.List<IVPuiAudit> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAudit> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAudit> findByUsr(String usr) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAudit> findByUsername(String username) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAudit> findByClient(String client) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAudit> findByIp(String ip) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAudit> findByType(String type) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAudit> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAudit> findByPk(String pk) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiAudit> findByContent(String content) throws PuiDaoFindException;
}
