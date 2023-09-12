package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiAuditDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiAudit;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiAuditDao extends AbstractViewDao<IVPuiAudit> implements IVPuiAuditDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiAudit> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAudit> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.DATETIME_FIELD, datetime);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAudit> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAudit> findByUsername(String username) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.USERNAME_FIELD, username);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAudit> findByClient(String client) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.CLIENT_FIELD, client);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAudit> findByIp(String ip) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.IP_FIELD, ip);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAudit> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.TYPE_FIELD, type);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAudit> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAudit> findByPk(String pk) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.PK_FIELD, pk);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiAudit> findByContent(String content) throws PuiDaoFindException {
		return super.findByColumn(IVPuiAudit.CONTENT_FIELD, content);
	}
}
