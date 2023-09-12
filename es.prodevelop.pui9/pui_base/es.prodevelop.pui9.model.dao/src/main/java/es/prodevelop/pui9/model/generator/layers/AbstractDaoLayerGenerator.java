package es.prodevelop.pui9.model.generator.layers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.classpath.PuiClassLoaderUtils;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.generator.DynamicClassGenerator;
import es.prodevelop.pui9.model.generator.TemplateFileInfo;

/**
 * This class represents the DAO Layer Generator, to generate all the files for
 * the PUI Server. This is the abstract class. Concrete classes for JDBC
 * approach is available
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractDaoLayerGenerator extends AbstractLayerGenerator {

	public static final String subpackageTableName = ".model.dto";
	public static final String subpackageTableInterfacesName = subpackageTableName + ".interfaces";
	public static final String subpackageViewName = ".model.views.dto";
	public static final String subpackageViewInterfacesName = subpackageViewName + ".interfaces";

	@Override
	public List<TemplateFileInfo> getTemplateList(boolean isView) {
		List<TemplateFileInfo> tableTemplateList = new ArrayList<>();

		if (!isView) {
			tableTemplateList.addAll(getTableDtoTemplates());
			tableTemplateList.addAll(getTableDaoTemplates());
		} else {
			tableTemplateList.addAll(getViewDtoTemplates());
			tableTemplateList.addAll(getViewDaoTemplates());
		}

		return tableTemplateList;
	}

	protected List<TemplateFileInfo> getTableDtoTemplates() {
		TemplateFileInfo idtopk = new TemplateFileInfo("dto/ITableDtoPk.ftl",
				"I" + DynamicClassGenerator.FILENAME_WILDARD + "Pk" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.GENERATED_PACKAGE_NAME + subpackageTableInterfacesName);
		TemplateFileInfo idto = new TemplateFileInfo("dto/ITableDto.ftl",
				"I" + DynamicClassGenerator.FILENAME_WILDARD + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.GENERATED_PACKAGE_NAME + subpackageTableInterfacesName);
		TemplateFileInfo dtopk = new TemplateFileInfo("dto/TableDtoPk.ftl",
				DynamicClassGenerator.FILENAME_WILDARD + "Pk" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.GENERATED_PACKAGE_NAME + subpackageTableName);
		TemplateFileInfo dto = new TemplateFileInfo("dto/TableDto.ftl",
				DynamicClassGenerator.FILENAME_WILDARD + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.GENERATED_PACKAGE_NAME + subpackageTableName);

		List<TemplateFileInfo> list = new ArrayList<>();
		list.add(idtopk);
		list.add(idto);
		list.add(dtopk);
		list.add(dto);

		return list;
	}

	protected List<TemplateFileInfo> getViewDtoTemplates() {
		TemplateFileInfo idto = new TemplateFileInfo("dto/IViewDto.ftl",
				"I" + DynamicClassGenerator.FILENAME_WILDARD + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.GENERATED_PACKAGE_NAME + subpackageViewInterfacesName);
		TemplateFileInfo dto = new TemplateFileInfo("dto/ViewDto.ftl",
				DynamicClassGenerator.FILENAME_WILDARD + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.GENERATED_PACKAGE_NAME + subpackageViewName);

		List<TemplateFileInfo> list = new ArrayList<>();
		list.add(idto);
		list.add(dto);

		return list;
	}

	protected abstract List<TemplateFileInfo> getTableDaoTemplates();

	protected abstract List<TemplateFileInfo> getViewDaoTemplates();

	@Override
	protected List<Class<?>> getClassesToRegister(List<TemplateFileInfo> templateList) throws Exception {
		List<Class<?>> classes = new ArrayList<>();

		for (TemplateFileInfo tfi : templateList) {
			String className = (tfi.getPackageName() + "." + tfi.getFileName())
					.replace(JavaFileObject.Kind.SOURCE.extension, "");
			Class<?> clazz = PuiClassLoaderUtils.getClassLoader().loadClass(className);
			if (IDao.class.isAssignableFrom(clazz) && classPassFilter(clazz)) {
				classes.add(clazz);
			}
		}

		return classes;
	}

	@Override
	protected Object registerBean(Class<?> clazz) {
		appContext.setClassLoader(PuiClassLoaderUtils.getClassLoader());
		appContext.getBeanFactory().setBeanClassLoader(PuiClassLoaderUtils.getClassLoader());

		registerBeanDefinition(clazz);

		DtoRegistry.getDtoImplementation(null, true);
		return factory.getBean(StringUtils.uncapitalize(clazz.getSimpleName()));
	}

	/**
	 * Filter the class to be registered
	 * 
	 * @param clazz The class to be registered
	 * @return true if the class passes the filter; false if not
	 */
	private boolean classPassFilter(Class<?> clazz) {
		return !clazz.isInterface();
	}

	/**
	 * Register the new bean
	 * 
	 * @param clazz The class to be registered
	 */
	private void registerBeanDefinition(Class<?> clazz) {
		String className = clazz.getName().replace(".", "/") + JavaFileObject.Kind.CLASS.extension;
		URL url = PuiClassLoaderUtils.getClassLoader().getResource(className);
		UrlResource ur = new UrlResource(url);

		RootBeanDefinition rbd = new RootBeanDefinition(clazz);
		rbd.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		rbd.setSource(ur);
		rbd.setResource(ur);

		definitionRegistry.registerBeanDefinition(StringUtils.uncapitalize(clazz.getSimpleName()), rbd);
	}

}
