package es.prodevelop.pui9.documents.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentExtension;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentExtensionPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiDocumentExtensionDao extends ITableDao<IPuiDocumentExtensionPk, IPuiDocumentExtension> {
	@PuiGenerated
	java.util.List<IPuiDocumentExtension> findByExtension(String extension) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocumentExtension> findByMaxsize(Integer maxsize) throws PuiDaoFindException;
}
