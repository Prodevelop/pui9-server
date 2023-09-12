package es.prodevelop.pui9.documents.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentModelExtensionDao;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentModelExtension;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentModelExtensionPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiDocumentModelExtensionDao
		extends AbstractTableDao<IPuiDocumentModelExtensionPk, IPuiDocumentModelExtension>
		implements IPuiDocumentModelExtensionDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiDocumentModelExtension> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocumentModelExtensionPk.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocumentModelExtension> findByExtension(String extension) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocumentModelExtensionPk.EXTENSION_FIELD, extension);
	}
}
