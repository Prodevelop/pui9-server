package es.prodevelop.pui9.importexport;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dto.PuiImportexportPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiImportexportService;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.threads.PuiMultiInstanceProcessBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiConstants;

/**
 * This component has a Timer to periodically check ImportExport table and
 * Files. If there exists any File that is not pointed by any ImportExport, the
 * File will be automatically deleted. Furthermore, if there exist any
 * ImportExport that points to a 'phantom' File, the ImportExport will be
 * deleted too. Also, those configurations that are not still executed and
 * passed more than 30 days, will be deleted too
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class CleanImportExport {

	private static final long MAX_DAYS_WITHOUT_PROCESS = 30;

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private IPuiImportexportService importExportService;

	@Autowired
	private PuiMultiInstanceProcessBackgroundExecutors multiInstanceProcessBackExec;

	@PostConstruct
	private void postConstruct() {
		Long initDelay = PuiMultiInstanceProcessBackgroundExecutors.getNextExecutionDelayAsMinutes(3, 0);

		multiInstanceProcessBackExec.registerNewExecutor("CleanImportExport", initDelay, TimeUnit.DAYS.toMinutes(1),
				TimeUnit.MINUTES, () -> {
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
		return docPathExists() && docPathHasFiles();
	}

	private boolean docPathExists() {
		String basePath = importExportService.getBaseDocumentsPath();
		File baseFolder = new File(basePath);
		return baseFolder.exists();
	}

	private boolean docPathHasFiles() {
		String basePath = importExportService.getBaseDocumentsPath();
		File baseFolder = new File(basePath);
		String[] files = baseFolder.list();
		return files != null && files.length > 0;
	}

	/**
	 * Check every Folder and it's files, and if doesn't exist a
	 */
	private void checkFiles() throws Exception {
		String basePath = importExportService.getBaseDocumentsPath();
		File baseFolder = new File(basePath);

		for (File modelFolder : baseFolder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
			String model = modelFolder.getName();
			for (File pkFolder : modelFolder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
				IPuiImportexportPk pk = new PuiImportexportPk(Integer.valueOf(pkFolder.getName()));
				if (!importExportService.exists(pk)) {
					logger.debug(
							"ImportExport cleaner: delete file for model '" + model + "' and ID '" + pk.getId() + "'");
					FileUtils.deleteDirectory(pkFolder);
				}
				if (pkFolder.listFiles().length == 0) {
					FileUtils.deleteDirectory(pkFolder);
				}
			}

			if (modelFolder.list().length == 0) {
				logger.debug("ImportExport cleaner: delete folder from filesystem for model '" + model + "'");
				FileUtils.deleteDirectory(modelFolder);
			}
		}
	}

	/**
	 * Check every Document, and if the referenced file doesn't exist, then delete
	 * the Document from the database
	 */
	private void checkDatabase() throws PuiServiceGetException, PuiServiceDeleteException {
		for (IPuiImportexport importExport : importExportService.getAll()) {
			String importFolder = importExportService.getImportFolder(importExport.getModel(), importExport.getId());
			File importFolderFile = new File(importFolder);
			if ((!importFolderFile.exists() || importFolderFile.listFiles().length == 0)
					&& importExport.getExecuted().equals(PuiConstants.FALSE_INT)) {
				logger.debug("ImportExport cleaner: delete importExport for model '" + importExport.getModel()
						+ "', ID '" + importExport.getId() + "', importTime '" + importExport.getDatetime()
						+ "' and user '" + importExport.getUsr() + "'");
				importExportService.delete(importExport);
				try {
					FileUtils.deleteDirectory(importFolderFile);
				} catch (IOException e) {
					// do nothing
				}
			}
		}

		for (IPuiImportexport importExport : importExportService.getAllWhere(
				FilterBuilder.newAndFilter().addEquals(IPuiImportexport.EXECUTED_COLUMN, PuiConstants.FALSE_INT))) {
			long days = ChronoUnit.DAYS.between(Instant.now(), importExport.getDatetime());
			days = Math.abs(days);
			if (days < MAX_DAYS_WITHOUT_PROCESS) {
				continue;
			}

			logger.debug("ImportExport cleaner: delete importExport for model '" + importExport.getModel() + "', ID '"
					+ importExport.getId() + "', importTime '" + importExport.getDatetime() + "' and user '"
					+ importExport.getUsr() + "'");
			importExportService.delete(importExport);
		}
	}

}
