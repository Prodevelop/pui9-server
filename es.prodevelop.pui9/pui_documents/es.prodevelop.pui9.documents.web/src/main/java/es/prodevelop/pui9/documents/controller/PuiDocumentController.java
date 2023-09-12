package es.prodevelop.pui9.documents.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.documents.dto.PuiDocumentLite;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsExtensionsException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsFileSizeException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsThumbnailException;
import es.prodevelop.pui9.documents.exceptions.PuiDocumentsUploadException;
import es.prodevelop.pui9.documents.model.dao.interfaces.IPuiDocumentDao;
import es.prodevelop.pui9.documents.model.dto.PuiDocumentRolePk;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocumentPk;
import es.prodevelop.pui9.documents.model.views.dao.interfaces.IVPuiDocumentDao;
import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.documents.service.interfaces.IPuiDocumentService;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.file.FileDownload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@PuiGenerated
@Controller
@Tag(name = "PUI Attached Documents")
@RequestMapping("/puidocument")
public class PuiDocumentController extends
		AbstractCommonController<IPuiDocumentPk, IPuiDocument, IVPuiDocument, IPuiDocumentDao, IVPuiDocumentDao, IPuiDocumentService> {
	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_DOCUMENT";
	}

	@PuiGenerated
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_DOCUMENT";
	}

	@Override
	public boolean allowInsert() {
		return false;
	}

	@Override
	public boolean allowTemplate() {
		return true;
	}

	@Override
	public boolean allowExport() {
		return false;
	}

	@Operation(summary = "Get all the available document roles", description = "Get all the available document roles")
	@GetMapping(value = "/getRoles", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getRoles() {
		return getService().getRoles();
	}

	@Operation(summary = "Get all the available extension for the given model", description = "Get all the available extension for the given model")
	@GetMapping(value = "/getExtensionsForModel", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getExtensionsForModel(@RequestParam String model) {
		return getService().getExtensionsForModel(model);
	}

	@Operation(summary = "Get all the documents of a registry", description = "Get all the documents of a registry")
	@GetMapping(value = "/getDocuments", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IPuiDocument> getDocuments(@RequestParam String model, @RequestParam String pk,
			@RequestParam(required = false) String role) {
		return getService().getDtoDocuments(model, pk, ObjectUtils.isEmpty(role) ? null : new PuiDocumentRolePk(role));
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_INSERT, value = METHOD_FUNCTIONALITY_INSERT)
	@Operation(summary = "Upload a new document", description = "Upload a new document associated to the given element")
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public IPuiDocument upload(@RequestParam MultipartFile file, PuiDocumentLite document)
			throws PuiDocumentsUploadException, PuiServiceInsertException, PuiDocumentsThumbnailException,
			PuiDocumentsExtensionsException, PuiDocumentsFileSizeException {
		return getService().upload(document);
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_GET, value = METHOD_FUNCTIONALITY_GET)
	@Operation(summary = "Download a document", description = "Download the document associated with the given PK")
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileDownload download(
			@Parameter(description = "The PK of the existing document", required = true) IPuiDocumentPk pk) {
		return getService().getFileDownload(pk);
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_GET, value = METHOD_FUNCTIONALITY_GET)
	@Operation(summary = "Get all models with documents", description = "Get all models with documents")
	@GetMapping(value = "/getModelsWithDocuments", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getModelsWithDocumentsdownload() {
		return getService().getModelsWithDocuments();
	}

	@Operation(summary = "Force a reload of the document configuracions", description = "Force a reload of the document configuracions")
	@GetMapping(value = "/reload")
	public void reload() {
		getService().reloadDocumentsInfo();
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_UPDATE, value = METHOD_FUNCTIONALITY_UPDATE)
	@Operation(summary = "Reload the thumbnails of ALL the images (executed in parallel thread)", description = "Reload the thumbnails of ALL the images (executed in parallel thread)")
	@GetMapping(value = "/reloadThumbnails")
	public void reloadThumbnails() {
		getService().reloadThumbnails();
	}

}