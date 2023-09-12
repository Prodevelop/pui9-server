package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiAuditDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAuditPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiAuditDao extends AbstractTableDao<IPuiAuditPk, IPuiAudit> implements IPuiAuditDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiAudit> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiAuditPk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAudit> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAudit> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.TYPE_FIELD, type);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAudit> findByPk(String pk) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.PK_FIELD, pk);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAudit> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.DATETIME_FIELD, datetime);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAudit> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAudit> findByIp(String ip) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.IP_FIELD, ip);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAudit> findByContent(String content) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.CONTENT_FIELD, content);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiAudit> findByClient(String client) throws PuiDaoFindException {
		return super.findByColumn(IPuiAudit.CLIENT_FIELD, client);
	}
}
