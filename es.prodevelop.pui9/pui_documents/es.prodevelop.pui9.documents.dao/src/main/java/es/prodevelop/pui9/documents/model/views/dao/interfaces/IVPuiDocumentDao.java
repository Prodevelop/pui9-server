package es.prodevelop.pui9.documents.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiDocumentDao extends IViewDao<IVPuiDocument> {
	@PuiGenerated
	java.util.List<IVPuiDocument> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByPk(String pk) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByDescription(String description) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByLanguage(String language) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByFilename(String filename) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByFilenameorig(String filenameorig) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByRole(String role) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByRoledescription(String roledescription) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByThumbnails(String thumbnails) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByUrl(String url) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiDocument> findByLang(String lang) throws PuiDaoFindException;
}
