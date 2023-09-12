package es.prodevelop.pui9.documents.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentDao;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiDocumentDao extends AbstractTableDao<IPuiDocumentPk, IPuiDocument> implements IPuiDocumentDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiDocument> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocumentPk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocument> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocument.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocument> findByPk(String pk) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocument.PK_FIELD, pk);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocument> findByLanguage(String language) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocument.LANGUAGE_FIELD, language);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocument> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocument.DESCRIPTION_FIELD, description);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocument> findByFilename(String filename) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocument.FILENAME_FIELD, filename);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocument> findByFilenameorig(String filenameorig) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocument.FILENAME_ORIG_FIELD, filenameorig);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocument> findByRole(String role) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocument.ROLE_FIELD, role);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocument> findByThumbnails(String thumbnails) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocument.THUMBNAILS_FIELD, thumbnails);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocument> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocument.DATETIME_FIELD, datetime);
	}
}
