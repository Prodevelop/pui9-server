package es.prodevelop.pui9.model.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.sql.DataSource;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.codegen.pui9.db.CodegenModelsDatabaseUtils;
import es.prodevelop.codegen.pui9.model.ClientConfiguration;
import es.prodevelop.codegen.pui9.model.DatabaseConnection;
import es.prodevelop.codegen.pui9.model.PuiConfiguration;
import es.prodevelop.codegen.pui9.model.ServerConfiguration;
import es.prodevelop.codegen.pui9.model.Table;
import es.prodevelop.codegen.pui9.model.View;
import es.prodevelop.pui9.classpath.PuiClassLoaderUtils;
import es.prodevelop.pui9.classpath.PuiClasspathFinderRegistry;
import es.prodevelop.pui9.db.helpers.IDatabaseHelper;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.generator.layers.AbstractControllerLayerGenerator;
import es.prodevelop.pui9.model.generator.layers.AbstractDaoLayerGenerator;
import es.prodevelop.pui9.model.generator.layers.AbstractServiceLayerGenerator;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * This class allows to generate the DAO and DTO classes of a View. It is very
 * useful when you need the DTO and DAO of a new View in Runtime. For instance,
 * to use it in a "Select" component, or in the Docgen PUI component. It
 * generates the classes and load them within the Classpath, and also registers
 * all of them within the DaoRegistry and DtoRegistry registers
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DynamicClassGenerator {

	public static final String GENERATED_FOLDER = "pui_generated";
	public static final String GENERATED_PACKAGE_NAME = "es.prodevelop.pui9.generated";
	public static final String FILENAME_WILDARD = "%filename%";

	public static final String READ_FUNCTIONALITY = "READ_PUI_GENERATED_ENTITY";
	public static final String WRITE_FUNCTIONALITY = "WRITE_PUI_GENERATED_ENTITY";
	public static final String GEN_FUNCTIONALITY = "GEN_PUI_GENERATED_ENTITY";

	public static final String PUI_VERSION_FOLDER = "1.x.y";
	private static final String TEMPLATE_FOLDER = "/versions/" + PUI_VERSION_FOLDER + "/templates";

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired(required = false)
	@Qualifier("dataSource")
	private DataSource dataSource;

	@Autowired
	private AbstractDaoLayerGenerator daoLayerGenerator;

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired(required = false)
	private IDatabaseHelper databaseHelper;

	@Autowired(required = false)
	private AbstractServiceLayerGenerator serviceLayerGenerator;

	@Autowired(required = false)
	private AbstractControllerLayerGenerator controllerLayerGenerator;

	private Configuration freemarkerConfig;

	private Set<String> generatedTablesCache = new HashSet<>();
	private Set<String> generatedViewsCache = new HashSet<>();

	private Reflections reflections;

	private DynamicClassGenerator() {
	}

	private void init() {
		if (freemarkerConfig == null && reflections == null) {
			freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32);
			freemarkerConfig.setClassLoaderForTemplateLoading(PuiClassLoaderUtils.getClassLoader(), TEMPLATE_FOLDER);
			freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_32));
			freemarkerConfig.setWhitespaceStripping(false);

			reflections = new Reflections(ConfigurationBuilder.build()
					.addClassLoaders(PuiClassLoaderUtils.getClassLoader()).forPackages("es.prodevelop"));
		}
	}

	/**
	 * Execute the code generation with the given configuration. This method is
	 * executed in a synchronized way, in order to avoid colisions in the code
	 * generation
	 * 
	 * @param config The configuration
	 * @return true if all worked fine; false if not
	 * @throws Exception
	 */
	public synchronized void executeCodeGeneration(String tableName, String viewName) throws Exception {
		init();
		String table = tableName;
		String view = viewName;
		if (!ObjectUtils.isEmpty(table)) {
			if (daoRegistry.existsDaoForEntity(table)) {
				logger.info("*** Table " + table + " already exists");
				table = null;
			}
			if (generatedTablesCache.contains(table)) {
				logger.info("*** Table " + table + " was already generated");
				table = null;
			}
			if (table != null) {
				generatedTablesCache.add(table);
			}
		}
		if (!ObjectUtils.isEmpty(view)) {
			if (daoRegistry.existsDaoForEntity(view)) {
				logger.info("*** View " + view + " already exists");
				view = null;
			}
			if (generatedViewsCache.contains(view)) {
				logger.info("*** View " + view + " was already generated");
				view = null;
			}
			if (view != null) {
				generatedViewsCache.add(view);
			}
		}

		if (table == null && view == null) {
			logger.info("*** Nothing to generate for: table (" + (!ObjectUtils.isEmpty(tableName) ? tableName : "-")
					+ ") / view (" + (!ObjectUtils.isEmpty(viewName) ? viewName : "-") + ")");
			return;
		}

		long start = System.currentTimeMillis();

		boolean jarIncludedInClasspath = false;
		List<TemplateFileInfo> templateList = new ArrayList<>();
		try {
			logger.debug("*** Preparing models to generate code for: table ("
					+ (!ObjectUtils.isEmpty(tableName) ? tableName : "-") + ") / view ("
					+ (!ObjectUtils.isEmpty(viewName) ? viewName : "-") + ")");

			PuiConfiguration model = createGeneratorModel(table, view);

			List<TemplateFileInfo> javaTemplateList = new ArrayList<>();
			List<TemplateFileInfo> daoTemplateList = new ArrayList<>();
			List<TemplateFileInfo> serviceTemplateList = new ArrayList<>();
			List<TemplateFileInfo> controllerTemplateList = new ArrayList<>();

			if (model.getServer().isGenerate()) {
				if (model.getSelectedTable() != null) {
					daoTemplateList.addAll(generateTableTemplates(model));
				}
				if (model.getSelectedView() != null) {
					daoTemplateList.addAll(generateViewTemplates(model));
				}
				if (!daoTemplateList.isEmpty()) {
					javaTemplateList.addAll(daoTemplateList);

					if (model.getServer().isGenerateService()) {
						serviceTemplateList.addAll(generateServiceTemplates(model));
						javaTemplateList.addAll(serviceTemplateList);
					}
					if (model.getServer().isGenerateController()) {
						controllerTemplateList.addAll(generateControllerTemplates(model));
						javaTemplateList.addAll(controllerTemplateList);
					}
				}
			}
			templateList.addAll(javaTemplateList);

			if (ObjectUtils.isEmpty(templateList)) {
				logger.info("*** Nothing to generate for: table (" + (!ObjectUtils.isEmpty(tableName) ? tableName : "-")
						+ ") / view (" + (!ObjectUtils.isEmpty(viewName) ? viewName : "-") + ")");
				return;
			}

			logger.debug("*** Start generating code for: table (" + (!ObjectUtils.isEmpty(tableName) ? tableName : "-")
					+ ") / view (" + (!ObjectUtils.isEmpty(viewName) ? viewName : "-") + ")");

			String modelName = model.getSelectedTable() != null ? model.getSelectedTable().getJavaName()
					: model.getSelectedView().getJavaName();
			modelName = modelName.toLowerCase();

			createTempFiles(templateList);

			boolean serverCreated = false;
			if (model.getServer().isGenerate()) {
				try {
					compileClasses(javaTemplateList);
					serverCreated = true;
				} catch (Exception e) {
					throw e;
				}
			}

			File jarFile = buildJar(templateList.get(0).getTempBinFolder(), modelName);
			if (jarFile != null) {
				loadJar(jarFile);
			}

			if (serverCreated) {
				loadClasses(daoTemplateList, serviceTemplateList, controllerTemplateList);
			}

			jarIncludedInClasspath = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			jarIncludedInClasspath = false;
			throw e;
		} finally {
			if (!jarIncludedInClasspath) {
				generatedTablesCache.remove(table);
				generatedViewsCache.remove(view);
			}
			if (templateList != null) {
				for (TemplateFileInfo tfi : templateList) {
					if (tfi.getTempSrcFile() != null) {
						Files.deleteIfExists(tfi.getTempSrcFile().toPath());
					}
					if (tfi.getTempBinFile() != null) {
						Files.deleteIfExists(tfi.getTempBinFile().toPath());
					}
				}
			}

			long end = System.currentTimeMillis();
			long total = end - start;
			logger.debug("*** Finish generating code for: table (" + (!ObjectUtils.isEmpty(tableName) ? tableName : "-")
					+ ") / view (" + (!ObjectUtils.isEmpty(viewName) ? viewName : "-") + "). Took " + total + "ms");
		}
	}

	/**
	 * Generate the table templates
	 * 
	 * @param model The model generator
	 * @return The list of templates for the table
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private List<TemplateFileInfo> generateTableTemplates(PuiConfiguration model) throws Exception {
		List<TemplateFileInfo> tableTemplateList = Collections.emptyList();
		if (model.getSelectedTable() != null) {
			tableTemplateList = daoLayerGenerator.getTemplateList(false);

			generateTemplates(tableTemplateList, model, model.getSelectedTable().getJavaName());
		}
		return tableTemplateList;
	}

	/**
	 * Generate the view templates
	 * 
	 * @param model The model generator
	 * @return The list of templates for the view
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private List<TemplateFileInfo> generateViewTemplates(PuiConfiguration model) throws Exception {
		List<TemplateFileInfo> viewTemplateList = Collections.emptyList();
		if (model.getSelectedView() != null) {
			viewTemplateList = daoLayerGenerator.getTemplateList(true);

			generateTemplates(viewTemplateList, model, model.getSelectedView().getJavaName());
		}
		return viewTemplateList;
	}

	/**
	 * Generate the service templates
	 * 
	 * @param model The model generator
	 * @return The list of templates for the service
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private List<TemplateFileInfo> generateServiceTemplates(PuiConfiguration model) throws Exception {
		List<TemplateFileInfo> serviceTemplateList = Collections.emptyList();
		if (serviceLayerGenerator != null) {
			serviceTemplateList = serviceLayerGenerator.getTemplateList(false);

			generateTemplates(serviceTemplateList, model,
					model.getSelectedTable() != null ? model.getSelectedTable().getJavaName()
							: model.getSelectedView().getJavaName());
		}
		return serviceTemplateList;
	}

	/**
	 * Generate the controller templates
	 * 
	 * @param model The model generator
	 * @return The list of templates for the controller
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private List<TemplateFileInfo> generateControllerTemplates(PuiConfiguration model) throws Exception {
		List<TemplateFileInfo> controllerTemplateList = Collections.emptyList();
		if (controllerLayerGenerator != null) {
			controllerTemplateList = controllerLayerGenerator.getTemplateList(false);

			generateTemplates(controllerTemplateList, model,
					model.getSelectedTable() != null ? model.getSelectedTable().getJavaName()
							: model.getSelectedView().getJavaName());
		}
		return controllerTemplateList;
	}

	/**
	 * Generate the contents of the given template file info
	 * 
	 * @param templateList The template list to be generated
	 * @param model        The data model for the freemarker template
	 * @param modelName    The name of the entity to be generated
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private void generateTemplates(List<TemplateFileInfo> templateList, PuiConfiguration model, String modelName)
			throws Exception {
		for (TemplateFileInfo tfi : templateList) {
			StringWriter outputWriter = new StringWriter();
			freemarkerConfig.getTemplate(tfi.getTemplateName());
			freemarkerConfig.setSharedVariable("config", model);
			Template tpl = freemarkerConfig.getTemplate(tfi.getTemplateName());
			tpl.setOutputEncoding(StandardCharsets.UTF_8.name());
			tpl.process(model, outputWriter);
			outputWriter.flush();
			outputWriter.close();

			String contents = outputWriter.toString();
			if (tfi.getGeneratedFileName().contains(FILENAME_WILDARD)) {
				tfi.setFileName(tfi.getGeneratedFileName().replace(FILENAME_WILDARD, modelName));
			}
			tfi.setContents(contents);
		}
	}

	/**
	 * Creates the Generator Model that will be used to generate the file contents
	 * by Freemarker
	 * 
	 * @param config The class generator configuration
	 * @return The code generator model
	 * @throws SQLException If any exception is throws while creating the generator
	 *                      model
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private PuiConfiguration createGeneratorModel(String tableName, String viewName) throws SQLException {
		Connection connection = getConnection();

		PuiConfiguration model = new PuiConfiguration();
		{
			CodegenModelsDatabaseUtils.singleton(connection);
			model.setDatabase(new DatabaseConnection());
			model.getDatabase().setConfiguration(model);
			model.getDatabase().setType(databaseHelper.getDatabaseType());
		}
		{
			model.setServer(new ServerConfiguration());
			model.getServer().setConfiguration(model);
			model.getServer().setDtoProject(GENERATED_PACKAGE_NAME);
			model.getServer().setDaoProject(GENERATED_PACKAGE_NAME);
			model.getServer().setBoProject(GENERATED_PACKAGE_NAME);
			model.getServer().setWebProject(GENERATED_PACKAGE_NAME);
			model.getServer().setBasePackage(GENERATED_PACKAGE_NAME);
			try {
				Class abstractCommonControllerClass = Class
						.forName("es.prodevelop.pui9.controller.AbstractCommonController");
				Optional<Class> optAbstractControllerClass = reflections.getSubTypesOf(abstractCommonControllerClass)
						.stream().filter(st -> ((Class) st).getSimpleName().equals("AbstractController")).findFirst();
				if (!optAbstractControllerClass.isPresent()) {
					throw new ClassNotFoundException();
				}
				model.getServer().setSuperController(optAbstractControllerClass.get().getName());
			} catch (ClassNotFoundException e) {
				model.getServer().setSuperController("es.prodevelop.pui9.controller.AbstractCommonController");
			}
			model.getServer().setGenerate(true);
			model.getServer().setGenerateService(true);
			model.getServer().setGenerateController(true);
			try {
				Class.forName("es.prodevelop.pui9.geo.dto.interfaces.IGeoDto");
				model.getServer().setGeoProject(true);
			} catch (ClassNotFoundException e) {
				model.getServer().setGeoProject(false);
			}
			model.getServer().setUseAdvancedFunctionalities(false);
		}
		{
			model.setClient(new ClientConfiguration());
			model.getClient().setConfiguration(model);
			model.getClient().setGenerate(false);
		}

		if (!ObjectUtils.isEmpty(tableName)) {
			Table table = new Table();
			table.setConfiguration(model);
			table.setDbName(tableName);
			boolean exists = CodegenModelsDatabaseUtils.singleton().loadTable(table);
			if (exists) {
				model.setSelectedTable(table);
				model.getServer().setReadFunctionality(READ_FUNCTIONALITY);
				model.getServer().setWriteFunctionality(WRITE_FUNCTIONALITY);
			}
		}
		if (!ObjectUtils.isEmpty(viewName)) {
			View view = new View();
			view.setConfiguration(model);
			view.setDbName(viewName);
			boolean exists = CodegenModelsDatabaseUtils.singleton().loadView(view);
			if (exists) {
				model.setSelectedView(view);
			}
		}

		if (model.getSelectedTable() != null) {
			model.setModelName(model.getSelectedTable().getLowercaseName());
		} else if (model.getSelectedView() != null) {
			model.setModelName(model.getSelectedView().getLowercaseName());
		}
		model.getServer().computeJavaAttributes();

		releaseConnection(connection);

		return model;
	}

	/**
	 * Create the files content. The target folder is the temp folder of the user
	 * who launched the application (tomcat or jboss)
	 * 
	 * @param templateList The template list from where the files will be created
	 * @throws Exception If any exception is thrown in while creating the files
	 *                   content
	 */
	private void createTempFiles(List<TemplateFileInfo> templateList) throws Exception {
		File tempFolder = new File(FileUtils.getTempDirectory(), GENERATED_FOLDER + System.currentTimeMillis());

		File tempSrcFolder = new File(tempFolder, "src");
		tempSrcFolder.mkdirs();
		File tempBinFolder = new File(tempFolder, "bin");
		tempBinFolder.mkdirs();

		for (TemplateFileInfo tfi : templateList) {
			tfi.setTempBinFolder(tempBinFolder);
			tfi.setTempSrcFile(new File(tempSrcFolder, tfi.getFileName()));
			boolean created = tfi.getTempSrcFile().createNewFile();
			if (!created) {
				continue;
			}

			try (FileWriter writer = new FileWriter(tfi.getTempSrcFile())) {
				writer.write(tfi.getContents());
			}
		}
	}

	/**
	 * Compile all the given Java classes. The target folder is the same where this
	 * class JAR is placed. The classpath of the application is used for compiling
	 * the classes. See {@link PuiClasspathFinderRegistry} class from PUI
	 * 
	 * @param templateList The list of templates
	 * @throws Exception If any exception was thrown while compiling the classes
	 */
	private void compileClasses(List<TemplateFileInfo> templateList) throws Exception {
		File binFolder = templateList.get(0).getTempBinFolder();

		List<File> files = new ArrayList<>();

		for (TemplateFileInfo tfi : templateList) {
			files.add(tfi.getTempSrcFile());
			String subfolders = tfi.getPackageName().replace(".", "/");
			String name = subfolders + File.separator + tfi.getFileName().replace(JavaFileObject.Kind.SOURCE.extension,
					JavaFileObject.Kind.CLASS.extension);
			tfi.setTempBinFile(new File(binFolder, name));
		}

		List<File> classpath = PuiClasspathFinderRegistry.getInstance().getClasspath();
		if (classpath.isEmpty()) {
			throw new Exception("No classpath available");
		}

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(binFolder));
		fileManager.setLocation(StandardLocation.CLASS_PATH, classpath);

		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);
		boolean result = compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
		fileManager.close();
		if (!result) {
			throw new Exception("Generated code do not compile");
		}
	}

	/**
	 * Builds the JAR file with the generated files
	 * 
	 * @param tempBinFolder The temp folder where the files were created
	 * @param config        The generator configuration
	 * @param entityName    The entity name
	 * @return The generated JAR file
	 * @throws Exception If any exception is thrown while building the JAR
	 */
	private File buildJar(File tempBinFolder, String entityName) throws Exception {
		File jarFile = new File(tempBinFolder, GENERATED_PACKAGE_NAME + "." + entityName + ".jar");

		ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(jarFile);

		File jarFolder = new File(tempBinFolder, GENERATED_PACKAGE_NAME.split("\\.")[0]);
		addFileToZip(zaos, jarFolder.getPath(), "");
		FileUtils.deleteDirectory(jarFolder);

		zaos.close();

		return jarFile;
	}

	/**
	 * Add the folder contents to the given zip archive output stream
	 * 
	 * @param zipOutputStream The zip file out stream
	 * @param path            The path file to be added
	 * @param base            The base path for the file to be added
	 * @throws Exception If any exception is thrown while adding a file to the zip
	 */
	private void addFileToZip(ZipArchiveOutputStream zipOutputStream, String path, String base) throws Exception {
		final File f = new File(path);
		final String entryName = base + f.getName();
		final ZipArchiveEntry zipEntry = new ZipArchiveEntry(f, entryName);

		zipOutputStream.putArchiveEntry(zipEntry);

		if (f.isFile()) {
			try (FileInputStream fInputStream = new FileInputStream(f)) {
				IOUtils.copy(fInputStream, zipOutputStream);
				zipOutputStream.closeArchiveEntry();
			}
		} else {
			zipOutputStream.closeArchiveEntry();
			final File[] children = f.listFiles();

			if (children != null) {
				for (File child : children) {
					addFileToZip(zipOutputStream, child.getAbsolutePath(), entryName + "/");
				}
			}
		}
	}

	/**
	 * Load the generated Jar File into the classpath. See
	 * {@link PuiClassLoaderUtils} class
	 * 
	 * @param jarFile The JAR file to be added
	 * @throws Exception If any Exception is thrown while loading the JAR file
	 */
	private void loadJar(File jarFile) throws Exception {
		URL jarUrl = jarFile.toURI().toURL();
		PuiClassLoaderUtils.addURL(jarUrl);
	}

	/**
	 * Register the Java Classes within the {@link DaoRegistry DaoRegistry} and the
	 * {@link DtoRegistry} registers but using Spring, in order to create the Beans
	 * properly
	 * 
	 * @param daoTemplateList        The list of DAO templates to load
	 * @param serviceTemplateList    The list of Service templates to load
	 * @param controllerTemplateList The list of Controller templates to load
	 * @throws Exception If any exception is thrown while loading the classes into
	 *                   Spring
	 */
	private void loadClasses(List<TemplateFileInfo> daoTemplateList, List<TemplateFileInfo> serviceTemplateList,
			List<TemplateFileInfo> controllerTemplateList) throws Exception {
		daoLayerGenerator.registerClasses(daoTemplateList);
		if (serviceLayerGenerator != null) {
			serviceLayerGenerator.registerClasses(serviceTemplateList);
		}
		if (controllerLayerGenerator != null) {
			controllerLayerGenerator.registerClasses(controllerTemplateList);
		}
	}

	private Connection getConnection() {
		checkDataSource();
		return DataSourceUtils.getConnection(dataSource);
	}

	private void releaseConnection(Connection connection) {
		checkDataSource();
		DataSourceUtils.releaseConnection(connection, dataSource);
	}

	private void checkDataSource() {
		if (dataSource == null) {
			throw new DataSourceLookupFailureException("No database configured for this application");
		}
	}

}
