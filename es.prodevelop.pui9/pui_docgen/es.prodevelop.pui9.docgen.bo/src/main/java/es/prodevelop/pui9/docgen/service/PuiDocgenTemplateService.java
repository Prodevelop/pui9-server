package es.prodevelop.pui9.docgen.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.google.common.io.Files;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.classpath.PuiClassLoaderUtils;
import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.exceptions.PuiCommonNoFileException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.docgen.dto.DocgenMapping;
import es.prodevelop.pui9.docgen.dto.DocgenMappingList;
import es.prodevelop.pui9.docgen.dto.DocgenParameterList;
import es.prodevelop.pui9.docgen.dto.DocgenUserMapping;
import es.prodevelop.pui9.docgen.dto.GenerateDocgenRequest;
import es.prodevelop.pui9.docgen.dto.PuiDocgenLite;
import es.prodevelop.pui9.docgen.dto.StringList;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenGenerateException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenModelNotExistsException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenNoElementsException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenNoParserException;
import es.prodevelop.pui9.docgen.exceptions.PuiDocgenUploadingTemplateException;
import es.prodevelop.pui9.docgen.fields.ISystemField;
import es.prodevelop.pui9.docgen.fields.SystemFieldsRegistry;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenAttributeDao;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenModelDao;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.dto.PuiDocgenModelPk;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttribute;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenModel;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.docgen.model.views.dao.interfaces.IVPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.parsers.DocumentParserRegistry;
import es.prodevelop.pui9.docgen.parsers.IDocumentParser;
import es.prodevelop.pui9.docgen.service.interfaces.IPuiDocgenTemplateService;
import es.prodevelop.pui9.docgen.utils.PuiDocgenTemplateExtended;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.file.PuiDocumentDefinition;
import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.INullTable;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.service.AbstractService;

@PuiGenerated
@Service
public class PuiDocgenTemplateService extends
		AbstractService<IPuiDocgenTemplatePk, IPuiDocgenTemplate, IVPuiDocgenTemplate, IPuiDocgenTemplateDao, IVPuiDocgenTemplateDao>
		implements IPuiDocgenTemplateService {

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private IPuiModelDao puimodelDao;

	@Autowired
	private IPuiDocgenAttributeDao docgenAttributeDao;

	@Autowired
	private IPuiDocgenModelDao docgenModelDao;

	@Override
	protected void afterNew(IPuiDocgenTemplate dto) throws PuiServiceException {
		dto.setFilter(FilterGroup.andGroup());
		dto.setMapping(new DocgenMappingList());
		dto.setParameters(new DocgenParameterList());
		dto.setModels(new StringList());
		dto.setColumnfilename(new StringList());
	}

	@Override
	protected void beforeDelete(IPuiDocgenTemplate dto) throws PuiServiceException {
		// when deleting the template in the database, also delete the file form
		// the file system
		deleteFile(dto);
	}

	@Override
	public List<DocgenMapping> getTemplateMapping(PuiDocgenLite document) throws PuiDocgenNoParserException {
		List<String> tags = getTags(document.getFile().getOriginalFileName(), document.getFile().getInputStream());
		if (tags == null) {
			return Collections.emptyList();
		}

		// make a MappingDto for each found tag
		List<DocgenMapping> mappings = new ArrayList<>();
		for (String tag : tags) {
			DocgenMapping mapping = new DocgenMapping();
			mapping.setTag(tag);
			mappings.add(mapping);
		}

		return mappings;
	}

	@Override
	public List<String> getSystemFields() {
		return SystemFieldsRegistry.getSingleton().getAllSystemFieldNames();
	}

	@Override
	public IPuiDocgenTemplate uploadTemplate(PuiDocgenTemplateExtended template)
			throws PuiServiceInsertException, PuiServiceUpdateException, PuiDocgenUploadingTemplateException {
		try {
			// upload the file to the file system
			uploadFile(template);

			if (template.getId() == null) {
				// insert the element in the database
				return insert(template.asPuiDocgenTemplate());
			} else {
				// delete the old file and update the filename
				IPuiDocgenTemplate old = getTableDao().findOne(template.createPk());
				deleteFile(old);
				return patch(template.createPk(),
						Collections.singletonMap(IPuiDocgenTemplate.FILENAME_FIELD, template.getFilename()));
			}
		} catch (PuiServiceInsertException e) {
			deleteFile(template);
			throw e;
		} catch (PuiServiceUpdateException e) {
			throw e;
		} catch (PuiException e) {
			throw new PuiDocgenUploadingTemplateException();
		}
	}

	@Override
	public FileDownload downloadTemplate(IPuiDocgenTemplatePk dtoPk)
			throws PuiServiceGetException, PuiCommonNoFileException {
		IPuiDocgenTemplate dto = getByPk(dtoPk);
		File file = getTemplateFile(dto);

		String name = Files.getNameWithoutExtension(dto.getFilename());
		String extension = Files.getFileExtension(dto.getFilename());

		// remove timestamp from the filename
		String filename = name.substring(0, name.lastIndexOf('_'));
		filename += "." + extension;

		return new FileDownload(file, filename);
	}

	@Override
	public FileDownload downloadSampleTemplate() throws PuiCommonNoFileException {
		InputStream is = PuiClassLoaderUtils.getClassLoader()
				.getResourceAsStream(IPuiDocgenTemplateService.SAMPLE_TEMPLATE);
		return new FileDownload(is, IPuiDocgenTemplateService.SAMPLE_TEMPLATE);
	}

	@Override
	public List<String> getModelsWithDocgen() {
		List<IPuiDocgenTemplate> all;
		try {
			all = getTableDao().findAll();
		} catch (PuiDaoFindException e) {
			all = Collections.emptyList();
		}

		Set<String> models = new HashSet<>();
		for (IPuiDocgenTemplate template : all) {
			if (template.getModels() == null) {
				continue;
			}

			template.getModels().forEach(model -> models.add(model.trim()));
		}

		List<String> modelsList = new ArrayList<>(models);
		Collections.sort(modelsList);
		return modelsList;
	}

	@Override
	public List<IPuiDocgenTemplate> getMatchingTemplates(String model) throws PuiServiceGetException {
		try {
			// get all the templates that has the same model than the given one
			FilterBuilder filterBuilder = FilterBuilder.newAndFilter()
					.addContainsExact(IPuiDocgenTemplate.MODELS_COLUMN, model);
			List<IPuiDocgenTemplate> docgenTemplateList = getTableDao().findWhere(filterBuilder);
			Collections.sort(docgenTemplateList, new PuIPuiDocgenTemplateComparator());

			return docgenTemplateList;
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FileDownload generate(GenerateDocgenRequest req)
			throws PuiServiceGetException, PuiDocgenModelNotExistsException, PuiDocgenNoElementsException,
			PuiDocgenGenerateException, PuiDocgenNoParserException, PuiCommonNoFileException {
		try {
			IPuiDocgenTemplate template = getByPk(req.getPk());
			if (ObjectUtils.isEmpty(req.getModel())) {
				req.setModel(template.getMainmodel());
			}
			IDao dao = getDaoFromModel(req.getModel());

			if (req.isFromClient() && PuiUserSession.getCurrentSession() != null) {
				req.setZoneId(PuiUserSession.getCurrentSession().getZoneId());
			}
			FilterBuilder finalFilterBuilder = getFinalFilter(template, req.buildSearchFilter(dao.getDtoClass()),
					req.getParameters());

			List<IDto> list = dao.findWhere(finalFilterBuilder);
			if (list.isEmpty()) {
				throw new PuiDocgenNoElementsException();
			}

			return doGenerate(req.getModel(), template, req.getMappings(), list, req.isGeneratePdf());
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	/**
	 * Method to refresh mappings so the client don't need to download the file
	 */
	@Override
	public List<DocgenMapping> getTemplateMappingFromId(IPuiDocgenTemplatePk pk)
			throws PuiDocgenNoParserException, PuiCommonNoFileException, PuiServiceGetException {
		FileDownload docGenTemplate = downloadTemplate(pk);
		PuiDocgenLite docgenLite = new PuiDocgenLite();
		PuiDocumentDefinition documentDefinition = new PuiDocumentDefinition();
		documentDefinition.setInputStream(docGenTemplate.getInputStream());
		documentDefinition.setOriginalFileName(docGenTemplate.getFilename());
		docgenLite.setFile(documentDefinition);
		return getTemplateMapping(docgenLite);
	}

	private File getTemplateFile(IPuiDocgenTemplate template) throws PuiCommonNoFileException {
		String path = getDocgenPath();
		path += template.getFilename();
		File file = new File(path);
		if (!file.exists()) {
			throw new PuiCommonNoFileException();
		}

		return file;
	}

	private IDocumentParser getDocumentParser(String filename) throws PuiDocgenNoParserException {
		IDocumentParser parser = DocumentParserRegistry.getSingleton().getDocumentParser(filename);
		if (parser == null) {
			throw new PuiDocgenNoParserException(filename);
		}
		return parser;
	}

	private void modifyMappingsWithUserValues(IPuiDocgenTemplate template, List<DocgenUserMapping> mappingValueList)
			throws PuiServiceGetException {
		try {
			// modify the mappings with the user values
			List<IPuiDocgenAttribute> tableAttributes = docgenAttributeDao.findAll();
			for (DocgenMapping mapp : template.getMapping()) {
				switch (mapp.getOrigin()) {
				case U:
					for (Iterator<DocgenUserMapping> it = mappingValueList.iterator(); it.hasNext();) {
						DocgenUserMapping next = it.next();
						if (mapp.getField().equals(next.getField())) {
							mapp.setField(next.getVal());
						}
					}
					break;
				case T:
					for (IPuiDocgenAttribute tableAttr : tableAttributes) {
						if (mapp.getField().equals(tableAttr.getId())) {
							mapp.setField(tableAttr.getValue());
						}
					}
					break;
				case S:
					ISystemField systemField = SystemFieldsRegistry.getSingleton().getSystemField(mapp.getField());
					mapp.setField(systemField != null ? systemField.getValue() : "");
					break;
				default:
					break;
				}
			}
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	/**
	 * Upload the given template file (specified with bytes) to the file system. It
	 * appends the current timestamp to the filename, in order to avoid duplicates
	 */
	private void uploadFile(PuiDocgenTemplateExtended template) throws PuiException {
		String docgenPath = getDocgenPath();

		File folders = new File(docgenPath);
		boolean createFolders = folders.mkdirs();
		if (!createFolders && !folders.exists()) {
			throw new PuiException("Could not create 'docgen' folder");
		}

		String name = FilenameUtils.getBaseName(template.getFilename());
		String extension = FilenameUtils.getExtension(template.getFilename());

		// add timestamp to the filename
		String filename = name + "_" + System.currentTimeMillis() + "." + extension;
		template.setFilename(filename);

		String filePath = docgenPath + template.getFilename();
		File file = new File(filePath);
		try {
			java.nio.file.Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			logger.error("Error while deleting the docgen template", e);
		}

		try {
			Files.write(IOUtils.toByteArray(template.getFile().getInputStream()), file);

			if (SystemUtils.IS_OS_UNIX) {
				Set<PosixFilePermission> perms = new HashSet<>();
				// user permission
				perms.add(PosixFilePermission.OWNER_READ);
				perms.add(PosixFilePermission.OWNER_WRITE);
				// group permissions
				perms.add(PosixFilePermission.GROUP_READ);
				// others permissions removed
				perms.remove(PosixFilePermission.OTHERS_READ);

				java.nio.file.Files.setPosixFilePermissions(file.toPath(), perms);
			}
		} catch (IOException e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Deletes the associated file of the Template from the file system
	 */
	private void deleteFile(IPuiDocgenTemplate dto) {
		String docgenPath = getDocgenPath();
		String filePath = docgenPath + dto.getFilename();
		File file = new File(filePath);
		try {
			java.nio.file.Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			logger.error("Error while deleting the docgen template", e);
		}
	}

	/**
	 * Get the list of tags of the given File (represented by the InputStream)
	 */
	private List<String> getTags(String fileName, InputStream inputStream) throws PuiDocgenNoParserException {
		return getDocumentParser(fileName).getTags(inputStream);
	}

	private String getDocgenPath() {
		String docsPath = variableService.getVariable(PuiVariableValues.DOCGEN_PATH.name());
		if (!docsPath.endsWith("/")) {
			docsPath += "/";
		}

		String tagStart = "[$][{]";
		String tagEnd = "[}]";
		String tagRegex = tagStart + "([^{]*)" + tagEnd;

		List<String> allMatches = new ArrayList<>();
		Matcher m = Pattern.compile(tagRegex).matcher(docsPath);
		while (m.find()) {
			allMatches.add(m.group());
		}

		String replaced = docsPath;
		for (String match : allMatches) {
			String prop = match.replaceAll(tagStart, "").replaceAll(tagEnd, "");
			String propVal = System.getProperty(prop);
			if (propVal != null) {
				replaced = replaced.replace(match, propVal);
			}
		}

		return replaced;
	}

	private FilterBuilder getTemplateFilterBuilder(IPuiDocgenTemplate template) {
		FilterGroup templateFilter = FilterGroup.andGroup();

		if (template.getFilter() != null && !ObjectUtils.isEmpty(template.getFilter().toString())) {
			templateFilter.addGroup(template.getFilter());
		}

		return FilterBuilder.newFilter(templateFilter);
	}

	private FilterBuilder getFinalFilter(IPuiDocgenTemplate template, FilterBuilder gridFilterBuilder,
			List<AbstractFilterRule> parameters) {
		FilterBuilder templateFilterBuilder = getTemplateFilterBuilder(template);

		if (gridFilterBuilder != null && !ObjectUtils.isEmpty(gridFilterBuilder.toString())) {
			templateFilterBuilder.addGroup(gridFilterBuilder);
		}

		if (!ObjectUtils.isEmpty(parameters)) {
			FilterGroup parametersFilter = FilterGroup.andGroup();
			parameters.forEach(parametersFilter::addRule);

			templateFilterBuilder.addGroup(FilterBuilder.newFilter(parametersFilter));
		}

		return templateFilterBuilder;
	}

	private FileDownload doGenerate(String modelName, IPuiDocgenTemplate template,
			List<DocgenUserMapping> mappingValueList, List<IDto> list, boolean isGeneratePdf)
			throws PuiDocgenNoParserException, PuiCommonNoFileException, PuiServiceGetException,
			PuiDocgenModelNotExistsException, PuiDocgenGenerateException {
		IDocumentParser parser = getDocumentParser(template.getFilename());
		File file = getTemplateFile(template);
		List<String> pkFields = getPkFieldNamesList(modelName);
		modifyMappingsWithUserValues(template, mappingValueList);

		try {
			return parser.parse(file, list, pkFields, template.getMapping(), template.getColumnfilename(),
					isGeneratePdf);
		} catch (Exception e) {
			throw new PuiDocgenGenerateException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> getPkFieldNamesList(String model)
			throws PuiServiceGetException, PuiDocgenModelNotExistsException {
		List<IPuiModel> puiModels;
		try {
			puiModels = puimodelDao.findByModel(model);
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}

		if (ObjectUtils.isEmpty(puiModels)) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		String entityName = puiModels.get(0).getEntity();

		Class<? extends IDto> dtoClass = getDaoRegistry().getDtoFromEntityName(entityName, false, true);
		if (dtoClass == null) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		if (IViewDto.class.isAssignableFrom(dtoClass)) {
			dtoClass = getDaoRegistry().getTableDtoFromViewDto((Class<IViewDto>) dtoClass);
		}

		if (dtoClass != null && !INullTable.class.isAssignableFrom(dtoClass)) {
			return DtoRegistry.getPkFields(dtoClass);
		} else {
			IPuiDocgenModel docgenModel = null;
			try {
				docgenModel = docgenModelDao.findOne(new PuiDocgenModelPk(model));
			} catch (PuiDaoFindException e) {
				// do nothing
			}

			if (docgenModel == null || docgenModel.getIdentityfields() == null) {
				throw new PuiDocgenModelNotExistsException(model);
			}

			return Arrays.asList(docgenModel.getIdentityfields().split(",", -1));
		}
	}

	@SuppressWarnings("rawtypes")
	private IDao getDaoFromModel(String model) throws PuiDocgenModelNotExistsException {
		Class<? extends IDao> daoClass = getDaoClassFromModel(model);
		IDao dao = PuiApplicationContext.getInstance().getBean(daoClass);
		if (dao == null) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		return dao;
	}

	@SuppressWarnings("rawtypes")
	private Class<? extends IDao> getDaoClassFromModel(String model) throws PuiDocgenModelNotExistsException {
		List<IPuiModel> puiModels;
		try {
			puiModels = puimodelDao.findByModel(model);
		} catch (PuiDaoFindException e) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		if (ObjectUtils.isEmpty(puiModels)) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		Class<? extends IDao> daoClass = getDaoRegistry().getDaoFromEntityName(puiModels.get(0).getEntity(), false);
		if (daoClass == null) {
			throw new PuiDocgenModelNotExistsException(model);
		}

		return daoClass;
	}

	/**
	 * Compare the templates by its name
	 */
	private class PuIPuiDocgenTemplateComparator implements Comparator<IPuiDocgenTemplate> {

		@Override
		public int compare(IPuiDocgenTemplate o1, IPuiDocgenTemplate o2) {
			return o1.getName().compareTo(o2.getName());
		}

	}

}