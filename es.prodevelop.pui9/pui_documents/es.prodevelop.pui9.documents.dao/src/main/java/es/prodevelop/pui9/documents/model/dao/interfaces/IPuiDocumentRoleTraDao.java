package es.prodevelop.pui9.documents.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRoleTra;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRoleTraPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiDocumentRoleTraDao extends ITableDao<IPuiDocumentRoleTraPk, IPuiDocumentRoleTra> {
	@PuiGenerated
	java.util.List<IPuiDocumentRoleTra> findByRole(String role) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocumentRoleTra> findByLang(String lang) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocumentRoleTra> findByLangstatus(Integer langstatus) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocumentRoleTra> findByDescription(String description) throws PuiDaoFindException;
}
