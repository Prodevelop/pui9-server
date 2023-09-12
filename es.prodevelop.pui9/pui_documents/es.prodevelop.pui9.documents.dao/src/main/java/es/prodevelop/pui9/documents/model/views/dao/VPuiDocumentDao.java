package es.prodevelop.pui9.documents.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.views.dao.interfaces.IVPuiDocumentDao;
import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiDocumentDao extends AbstractViewDao<IVPuiDocument> implements IVPuiDocumentDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByPk(String pk) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.PK_FIELD, pk);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.DESCRIPTION_FIELD, description);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByLanguage(String language) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.LANGUAGE_FIELD, language);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByFilenameorig(String filenameorig) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.FILENAME_ORIG_FIELD, filenameorig);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByFilename(String filename) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.FILENAME_FIELD, filename);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByRole(String role) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.ROLE_FIELD, role);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByRoledescription(String roledescription) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.ROLE_DESCRIPTION_FIELD, roledescription);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByThumbnails(String thumbnails) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.THUMBNAILS_FIELD, thumbnails);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByUrl(String url) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.URL_FIELD, url);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.DATETIME_FIELD, datetime);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocument> findByLang(String lang) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocument.LANG_FIELD, lang);
	}
}
