package es.prodevelop.pui9.documents.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentRoleDao;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRole;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRolePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiDocumentRoleDao extends AbstractTableDao<IPuiDocumentRolePk, IPuiDocumentRole>
		implements IPuiDocumentRoleDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiDocumentRole> findByRole(String role) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocumentRolePk.ROLE_FIELD, role);
	}
}
