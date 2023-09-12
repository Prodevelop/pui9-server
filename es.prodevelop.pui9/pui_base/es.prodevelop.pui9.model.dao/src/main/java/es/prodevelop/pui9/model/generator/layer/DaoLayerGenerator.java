package es.prodevelop.pui9.model.generator.layer;

import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.model.generator.DynamicClassGenerator;
import es.prodevelop.pui9.model.generator.TemplateFileInfo;
import es.prodevelop.pui9.model.generator.layers.AbstractDaoLayerGenerator;

/**
 * This class represents the DAO Layer Generator for JDBC approach, to generate
 * all the files for the PUI Server.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DaoLayerGenerator extends AbstractDaoLayerGenerator {

	public static final String subpackageTableDaoName = ".model.dao";
	public static final String subpackageTableDaoInterfacesName = subpackageTableDaoName + ".interfaces";
	public static final String subpackageViewDaoName = ".model.views.dao";
	public static final String subpackageViewDaoInterfacesName = subpackageViewDaoName + ".interfaces";

	@Override
	protected List<TemplateFileInfo> getTableDaoTemplates() {
		TemplateFileInfo idao = new TemplateFileInfo("dao/ITableDao.ftl",
				"I" + DynamicClassGenerator.FILENAME_WILDARD + "Dao" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.GENERATED_PACKAGE_NAME + subpackageTableDaoInterfacesName);
		TemplateFileInfo dao = new TemplateFileInfo("dao/TableDao.ftl",
				DynamicClassGenerator.FILENAME_WILDARD + "Dao" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.GENERATED_PACKAGE_NAME + subpackageTableDaoName);

		List<TemplateFileInfo> list = new ArrayList<>();
		list.add(idao);
		list.add(dao);

		return list;
	}

	@Override
	protected List<TemplateFileInfo> getViewDaoTemplates() {
		TemplateFileInfo idao = new TemplateFileInfo("dao/IViewDao.ftl",
				"I" + DynamicClassGenerator.FILENAME_WILDARD + "Dao" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.GENERATED_PACKAGE_NAME + subpackageViewDaoInterfacesName);
		TemplateFileInfo dao = new TemplateFileInfo("dao/ViewDao.ftl",
				DynamicClassGenerator.FILENAME_WILDARD + "Dao" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.GENERATED_PACKAGE_NAME + subpackageViewDaoName);

		List<TemplateFileInfo> list = new ArrayList<>();
		list.add(idao);
		list.add(dao);

		return list;
	}

}
