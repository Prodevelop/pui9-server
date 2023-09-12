package es.prodevelop.pui9.documents.service.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.dto.PuiDocumentLite;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsExtensionsException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsFileSizeException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsThumbnailException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsUploadException;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentDao;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentPk;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentRolePk;
import es.prodevelop.pui9.documents.model.views.dao.interfaces.IVPuiDocumentDao;
import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.service.interfaces.IService;

@PuiGenerated
public interface IPuiDocumentService
		extends IService<IPuiDocumentPk, IPuiDocument, IVPuiDocument, IPuiDocumentDao, IVPuiDocumentDao> {

	/**
	 * Get all existing roles
	 * 
	 * @return The list of roles
	 */
	List<String> getRoles();

	/**
	 * Get all allowd extensions for given model
	 * 
	 * @param model The model to check
	 * @return The list of allowed extensions
	 */
	List<String> getExtensionsForModel(String model);

	/**
	 * Upload a document into the server. Creates the registry in database and the
	 * file in the filesystem
	 * 
	 * @param document The document info
	 * @return The created database registry
	 * @throws PuiDocumentsUploadException     If an error uploading the document
	 *                                         occurred. Should never happen
	 * @throws PuiServiceInsertException       If an error inserting to the database
	 *                                         happens
	 * @throws PuiDocumentsThumbnailException  If an error generating the thumbnail
	 *                                         happens
	 * @throws PuiDocumentsExtensionsException If no extension allowed
	 * @throws PuiDocumentsFileSizeException   If filesize is higher than allowed
	 */
	@Transactional(rollbackFor = PuiException.class)
	IPuiDocument upload(PuiDocumentLite document) throws PuiDocumentsUploadException, PuiServiceInsertException,
			PuiDocumentsThumbnailException, PuiDocumentsExtensionsException, PuiDocumentsFileSizeException;

	/**
	 * Get the list of documents associated to the given registry
	 * 
	 * @param dto The registry
	 * @return The list of documents
	 */
	List<IPuiDocument> getDtoDocuments(ITableDto dto);

	/**
	 * Get the list of documents associated to the given PK of the given model,
	 * filtered by the given role
	 * 
	 * @param model The model of the registry
	 * @param pk    The PK of the registry
	 * @param role  The role of the documents to get
	 * @return The list of documents that accomplish the conditions
	 */
	List<IPuiDocument> getDtoDocuments(String model, String pk, IPuiDocumentRolePk role);

	/**
	 * Get the document associated to the given PK of the given model, with a given
	 * name
	 * 
	 * @param model    The model of the registry
	 * @param pk       The PK of the registry
	 * @param filename The file name of the document
	 * @return The related document
	 */
	IPuiDocument getDocumentFromFileName(String model, String pk, String filename);

	/**
	 * Check if the file related to the given document exists in the filesystem
	 * 
	 * @param dto The document to check
	 * @return True if exists; false if not
	 */
	boolean existsDocumentFile(IPuiDocument dto);

	/**
	 * Get a downloadable file of the related document PK
	 * 
	 * @param pk The PK of the document
	 * @return A downloadable file
	 */
	FileDownload getFileDownload(IPuiDocumentPk pk);

	/**
	 * Get a downloadable file of the related document
	 * 
	 * @param dto The document
	 * @return A downloadable file
	 */
	FileDownload getFileDownload(IPuiDocument dto);

	/**
	 * Get the list of models with documents enabled
	 * 
	 * @return The list of models with documents
	 */
	List<String> getModelsWithDocuments();

	/**
	 * Reload the info of the documents that could have documents
	 */
	void reloadDocumentsInfo();

	/**
	 * Get the Base Path of the documents in the filesystem
	 * 
	 * @return The path in the filesystem
	 */
	String getBaseDocumentsPath();

	/**
	 * Convert a DTO into an String of its PK
	 * 
	 * @param dto The document
	 * @return The String representing its PK
	 */
	String getPkAsStringFromDto(ITableDto dto);

	/**
	 * Convert a DTO into a Map with the values of its PK
	 * 
	 * @param dto The document
	 * @return The Map with the PK values
	 */
	Map<String, Object> getPkAsMapFromDto(ITableDto dto);

	/**
	 * Convert a map with the PK of a registry into an String of its PK
	 * 
	 * @param model The model of the registry
	 * @param pkMap The Map with the values of the registry PK
	 * @return The String representing its PK
	 */
	String getPkAsStringFromMap(String model, Map<String, Object> pkMap);

	/**
	 * Duplicate all the documents associated to the DTOs in the map. This process
	 * could be heavy in time
	 * 
	 * @param dtoPkMap            A Map with the OLD DTO PK and the NEW DTO PK
	 * @param executeInBackground If the process should be executed in background
	 * @throws PuiException If any exception happen in the process
	 */
	@Transactional(rollbackFor = PuiException.class)
	void duplicateAllDocumentsOfDtos(Map<ITableDto, ITableDto> dtoPkMap, boolean executeInBackground)
			throws PuiException;

	/**
	 * Duplicate all the documents associated to the given DTO PK. This process
	 * could be heavy in time
	 * 
	 * @param oldDtoPk            The old PK of the DTO
	 * @param newDtoPk            The new PK of the DTO
	 * @param executeInBackground If the process should be executed in background
	 * @throws PuiException If any exception happen in the process
	 */
	@Transactional(rollbackFor = PuiException.class)
	void duplicateAllDocumentsOfDto(ITableDto oldDtoPk, ITableDto newDtoPk, boolean executeInBackground)
			throws PuiException;

	/**
	 * Reload all the thumbnails. This process could be heavy in time
	 */
	void reloadThumbnails();
}
