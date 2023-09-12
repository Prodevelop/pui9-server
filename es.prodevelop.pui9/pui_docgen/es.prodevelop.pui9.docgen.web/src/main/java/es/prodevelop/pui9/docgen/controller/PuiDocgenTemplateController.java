package es.prodevelop.pui9.docgen.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.exceptions.PuiCommonNoFileException;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.docgen.dto.DocgenMapping;
import es.prodevelop.pui9.docgen.dto.GenerateDocgenRequest;
import es.prodevelop.pui9.docgen.dto.PuiDocgenLite;
import es.prodevelop.pui9.docgen.eventlistener.event.DocgenGenerateEvent;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenGenerateException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenModelNotExistsException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenNoElementsException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenNoParserException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenUploadingTemplateException;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.docgen.model.views.dao.interfaces.IVPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.service.interfaces.IPuiDocgenTemplateService;
import es.prodevelop.pui9.docgen.utils.PuiDocgenTemplateExtended;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.file.FileDownload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@PuiGenerated
@Controller
@Tag(name = "PUI Docgen Templates")
@RequestMapping("/puidocgentemplate")
public class PuiDocgenTemplateController extends
		AbstractCommonController<IPuiDocgenTemplatePk, IPuiDocgenTemplate, IVPuiDocgenTemplate, IPuiDocgenTemplateDao, IVPuiDocgenTemplateDao, IPuiDocgenTemplateService> {

	private static final String GEN_PUI_DOCGEN_FUNC = "GEN_PUI_DOCGEN";
	private static final String WRITE_PUI_DOCGEN_FUNC = "WRITE_PUI_DOCGEN";

	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return WRITE_PUI_DOCGEN_FUNC;
	}

	@PuiGenerated
	@Override
	protected String getWriteFunctionality() {
		return WRITE_PUI_DOCGEN_FUNC;
	}

	@Override
	protected String getListFunctionality() {
		return GEN_PUI_DOCGEN_FUNC;
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

	@PuiFunctionality(id = "getTemplateMapping", value = WRITE_PUI_DOCGEN_FUNC)
	@Operation(summary = "All the mapping Tags found in a document", description = "Retrieve all the tags found in the given document")
	@PostMapping(value = "/getTemplateMapping", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DocgenMapping> getTemplateMapping(@RequestParam MultipartFile file,
			@Parameter(hidden = true) PuiDocgenLite document) throws PuiDocgenNoParserException {
		return getService().getTemplateMapping(document);
	}

	@PuiFunctionality(id = "getSystemFields", value = WRITE_PUI_DOCGEN_FUNC)
	@Operation(summary = "All system fields available for Docgen", description = "Get the list of all system attributes/fields available to be usen in any Docgen")
	@GetMapping(value = "/getSystemFields", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getSystemFields() {
		return getService().getSystemFields();
	}

	@PuiFunctionality(id = "uploadTemplate", value = WRITE_PUI_DOCGEN_FUNC)
	@Operation(summary = "Upload a new Docgen Template", description = "Creates a new Docgen Template with the given data and associated document")
	@PostMapping(value = "/uploadTemplate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public IPuiDocgenTemplate uploadTemplate(@RequestParam MultipartFile file,
			@Parameter(description = "The docgen information", required = true) PuiDocgenTemplateExtended template)
			throws PuiServiceInsertException, PuiServiceUpdateException, PuiDocgenUploadingTemplateException {
		return getService().uploadTemplate(template);
	}

	@PuiFunctionality(id = "downloadTemplate", value = WRITE_PUI_DOCGEN_FUNC)
	@Operation(summary = "Download the document template", description = "Download the document of the Docgen template")
	@GetMapping(value = "/downloadTemplate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileDownload downloadTemplate(
			@Parameter(description = "The PK of the docgen template", required = true) IPuiDocgenTemplatePk pk)
			throws PuiServiceGetException, PuiCommonNoFileException {
		return getService().downloadTemplate(pk);
	}

	@PuiFunctionality(id = "downloadSampleTemplate", value = WRITE_PUI_DOCGEN_FUNC)
	@Operation(summary = "Download a sample Docgen template", description = "Download a sample Docgen template")
	@GetMapping(value = "/downloadSampleTemplate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileDownload downloadSampleTemplate() throws PuiCommonNoFileException {
		return getService().downloadSampleTemplate();
	}

	@PuiFunctionality(id = "getModelsWithDocgen", value = GEN_PUI_DOCGEN_FUNC)
	@Operation(summary = "Get the list of models with docgen action available", description = "Get the list of models with docgen action available")
	@GetMapping(value = "/getModelsWithDocgen", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getModelsWithDocgen() {
		return getService().getModelsWithDocgen();
	}

	@PuiFunctionality(id = "getToGenerate", value = GEN_PUI_DOCGEN_FUNC)
	@Operation(summary = "Get a Docgen registry to be generated", description = "Get a Docgen registry to be generated")
	@GetMapping(value = "/getToGenerate", produces = MediaType.APPLICATION_JSON_VALUE)
	public IPuiDocgenTemplate getToGenerate(
			@Parameter(description = "The PK of the Docgen Template", required = true) IPuiDocgenTemplatePk pk)
			throws PuiServiceGetException {
		return getService().getByPk(pk);
	}

	@PuiFunctionality(id = "getMatchingTemplates", value = GEN_PUI_DOCGEN_FUNC)
	@Operation(summary = "Matching templates for given dtoModelId, docgenModelId and PK", description = "Get matching templates for given dtoModelId, docgenModelId and PK")
	@GetMapping(value = "/getMatchingTemplates", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IPuiDocgenTemplate> getMatchingTemplates(
			@Parameter(description = "The model Id", required = true) @RequestParam String model)
			throws PuiServiceGetException {
		return getService().getMatchingTemplates(model);
	}

	@PuiFunctionality(id = "generate", value = GEN_PUI_DOCGEN_FUNC)
	@Operation(summary = "Generate the document", description = "Generate the document")
	@PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileDownload generate(
			@Parameter(description = "The Info for the template", required = true) @RequestBody GenerateDocgenRequest req)
			throws PuiServiceGetException, PuiDocgenModelNotExistsException, PuiDocgenNoElementsException,
			PuiDocgenGenerateException, PuiDocgenNoParserException, PuiCommonNoFileException {
		req.setFromClient(true);
		FileDownload fd = getService().generate(req);

		getEventLauncher().fireAsync(new DocgenGenerateEvent(req, fd));

		return fd;
	}

	@PuiFunctionality(id = "getTemplateMappingFromId", value = WRITE_PUI_DOCGEN_FUNC)
	@Operation(summary = "All the mapping Tags found in the document of a given template", description = "All the mapping Tags found in the document of a given template")
	@GetMapping(value = "/getTemplateMappingFromId", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DocgenMapping> getTemplateMappingFromId(
			@Parameter(description = "The PK of the docgen template", required = true) IPuiDocgenTemplatePk pk)
			throws PuiServiceGetException, PuiCommonNoFileException, PuiDocgenNoParserException {
		return getService().getTemplateMappingFromId(pk);
	}

}