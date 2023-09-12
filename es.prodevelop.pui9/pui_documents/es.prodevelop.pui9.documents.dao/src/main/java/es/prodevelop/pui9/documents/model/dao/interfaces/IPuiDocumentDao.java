package es.prodevelop.pui9.documents.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiDocumentDao extends ITableDao<IPuiDocumentPk, IPuiDocument> {
	@PuiGenerated
	java.util.List<IPuiDocument> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocument> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocument> findByPk(String pk) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocument> findByLanguage(String language) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocument> findByDescription(String description) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocument> findByFilename(String filename) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocument> findByFilenameorig(String filenameorig) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocument> findByRole(String role) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocument> findByThumbnails(String thumbnails) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocument> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException;
}
