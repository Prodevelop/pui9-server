package es.prodevelop.pui9.documents.components;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.documents.service.interfaces.IPuiDocumentService;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.threads.PuiMultiInstanceProcessBackgroundExecutors;

/**
 * This component has a Timer to periodically check Documents table and Files.
 * If there exists any File that is not pointed by any Document, the File will
 * be automatically deleted. Furthermore, if there exist any Document that
 * points to a 'phantom' File, the Document will be deleted too.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class CleanDocuments {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private IPuiDocumentService documentService;

	@Autowired
	private PuiMultiInstanceProcessBackgroundExecutors multiInstanceProcessBackExec;

	@PostConstruct
	private void postConstruct() {
		multiInstanceProcessBackExec.registerNewExecutor("CleanDocuments", 0, 1, TimeUnit.HOURS, () -> {
			if (!isEnabled()) {
				return;
			}
			try {
				checkFiles();
				checkDatabase();
			} catch (Exception e) {
				// do nothing
			}
		});
	}

	private boolean isEnabled() {
		return variableService.getVariable(Boolean.class, PuiVariableValues.DOCUMENTS_CLEAN_ENABLED.name())
				&& docPathExists() && docPathHasFiles();
	}

	private boolean docPathExists() {
		String basePath = documentService.getBaseDocumentsPath();
		File baseFolder = new File(basePath);
		return baseFolder.exists();
	}

	private boolean docPathHasFiles() {
		String basePath = documentService.getBaseDocumentsPath();
		File baseFolder = new File(basePath);
		String[] files = baseFolder.list();
		return files != null && files.length > 0;
	}

	/**
	 * Check every Folder and it's files, and if doesn't exist a
	 */
	private void checkFiles() throws Exception {
		String basePath = documentService.getBaseDocumentsPath();
		File baseFolder = new File(basePath);

		for (File modelFolder : baseFolder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
			String model = modelFolder.getName();
			for (File pkFolder : modelFolder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
				String pk = pkFolder.getName();
				for (File docFile : pkFolder.listFiles((FileFilter) FileFileFilter.INSTANCE)) {
					IPuiDocument document = documentService.getDocumentFromFileName(model, pk, docFile.getName());
					if (document == null) {
						logger.debug("Documents cleaner: delete file for model '" + model + "', PK '" + pk
								+ "' and filename '" + docFile.getName() + "'");
						FileUtils.deleteQuietly(docFile);
					}
				}
				if (pkFolder.listFiles((FileFilter) FileFileFilter.INSTANCE).length == 0) {
					logger.debug("Documents cleaner: delete folder for model '" + model + "' and PK '" + pk + "'");
					FileUtils.deleteDirectory(pkFolder);
				}
			}

			if (modelFolder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY).length == 0) {
				logger.debug("Documents cleaner: delete folder for model '" + model + "'");
				FileUtils.deleteDirectory(modelFolder);
			}
		}
	}

	/**
	 * Check every Document, and if the referenced file doesn't exist, then delete
	 * the Document from the database
	 * 
	 * @throws PuiDaoDeleteException
	 */
	private void checkDatabase() throws PuiServiceGetException, PuiDaoDeleteException {
		SearchRequest req = new SearchRequest();
		req.setRows(100);
		int i = 0;
		List<IPuiDocument> list;
		for (;;) {
			req.setPage(i++);
			list = documentService.searchTable(req).getData();
			if (list.isEmpty()) {
				break;
			}
			for (IPuiDocument document : list) {
				if (!documentService.existsDocumentFile(document)) {
					logger.debug("Documents cleaner: delete document for model '" + document.getModel() + "', PK '"
							+ document.getPk() + "' and filename '" + document.getFilename() + "'");
					documentService.getTableDao().delete(document);
				}
			}
		}
	}

}
