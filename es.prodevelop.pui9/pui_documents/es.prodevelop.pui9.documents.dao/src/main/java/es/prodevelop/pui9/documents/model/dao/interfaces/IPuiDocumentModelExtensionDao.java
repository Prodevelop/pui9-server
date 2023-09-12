package es.prodevelop.pui9.documents.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentModelExtension;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentModelExtensionPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiDocumentModelExtensionDao
		extends ITableDao<IPuiDocumentModelExtensionPk, IPuiDocumentModelExtension> {
	@PuiGenerated
	java.util.List<IPuiDocumentModelExtension> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocumentModelExtension> findByExtension(String extension) throws PuiDaoFindException;
}
