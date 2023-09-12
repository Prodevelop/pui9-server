package es.prodevelop.pui9.common.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiImportexportDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiImportexportService;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.service.AbstractService;

@PuiGenerated
@Service
public class PuiImportexportService
		extends AbstractService<IPuiImportexportPk, IPuiImportexport, INullView, IPuiImportexportDao, INullViewDao>
		implements IPuiImportexportService {

	@Autowired
	private IPuiVariableService variableService;

	@Override
	protected void beforeDelete(IPuiImportexport dto) throws PuiServiceException {
		String importFolder = getImportFolder(dto.getModel(), dto.getId());
		try {
			FileUtils.deleteDirectory(new File(importFolder));
		} catch (IOException e) {
			// do nothing
		}
	}

	@Override
	public String getBaseDocumentsPath() {
		String basePath = variableService.getVariable(PuiVariableValues.IMPORTEXPORT_PATH.name());
		if (!basePath.endsWith(File.separator)) {
			basePath += File.separator;
		}

		String tagStart = "[$][{]";
		String tagEnd = "[}]";
		String tagRegex = tagStart + "([^{]*)" + tagEnd;

		List<String> allMatches = new ArrayList<>();
		Matcher m = Pattern.compile(tagRegex).matcher(basePath);
		while (m.find()) {
			allMatches.add(m.group());
		}

		String path = basePath;
		for (String match : allMatches) {
			String prop = match.replaceAll(tagStart, "").replaceAll(tagEnd, "");
			String propVal = System.getProperty(prop);
			if (propVal != null) {
				path = path.replace(match, propVal);
			}
		}

		return path;
	}

	@Override
	public String getImportFolder(String model, Integer id) {
		String importFolder = getBaseDocumentsPath();
		if (!importFolder.endsWith(File.separator)) {
			importFolder += File.separator;
		}
		importFolder += model + File.separator + id + File.separator;
		File folders = new File(importFolder);
		if (!folders.exists() && !folders.mkdirs()) {
			throw new InvalidPathException(folders.toString(), "Could not create destination folder");
		}

		return importFolder;
	}

}
