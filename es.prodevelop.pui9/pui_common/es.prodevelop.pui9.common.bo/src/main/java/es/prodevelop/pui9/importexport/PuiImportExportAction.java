package es.prodevelop.pui9.importexport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IllegalFormatConversionException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportInvalidColumnException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportInvalidModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportNoModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportPkNotIncludedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportWithErrorsException;
import es.prodevelop.pui9.common.model.dto.PuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.service.interfaces.IPuiImportexportService;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.csv.CsvReader;
import es.prodevelop.pui9.csv.CsvWriter;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoFactory;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.search.ExportColumnDefinition;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.service.interfaces.IService;
import es.prodevelop.pui9.service.registry.ServiceRegistry;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiDateUtil;
import es.prodevelop.pui9.utils.PuiLanguage;

@Component
public class PuiImportExportAction {

	protected static final String CSV_FILE_EXTENSION = ".csv";
	protected static final String JSON_FILE_EXTENSION = ".json";

	@Autowired
	protected DaoRegistry daoRegistry;

	@Autowired
	private ServiceRegistry serviceRegistry;

	@Autowired
	private IPuiModelService modelService;

	@Autowired
	private IPuiImportexportService importExportService;

	@Autowired
	private ImportUtil importUtil;

	@Autowired
	private ExportUtil exportUtil;

	private List<String> modelsCache;

	@PostConstruct
	private void postConstruct() {
		if (modelsCache != null) {
			return;
		}

		modelsCache = new ArrayList<>();

		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ReloadImportExportActionModels", true, 1, 1,
				TimeUnit.HOURS, () -> reloadModels(true));
	}

	/**
	 * Reload the models cache with those that has the flag actionImportExport set
	 * to true in the configuration of the models at pui_model table
	 * 
	 * @param force If set to true, the cache is forced to be reloaded
	 */
	public void reloadModels(boolean force) {
		if (force || ObjectUtils.isEmpty(modelsCache)) {
			if (force) {
				modelService.reloadModels(true);
			}
			synchronized (modelsCache) {
				modelsCache.clear();
				modelsCache.addAll(modelService.getOriginalPuiModelConfigurations().entrySet().stream()
						.filter(entry -> entry.getValue().getDefaultConfiguration().isActionImportExport())
						.map(Entry::getKey).collect(Collectors.toList()));
			}
		}
	}

	public ImportUtil getImportUtil() {
		return importUtil;
	}

	public ExportUtil getExportUtil() {
		return exportUtil;
	}

	/**
	 * Check if the given model is configured to be exported and imported
	 * 
	 * @param model The model to be checked
	 * @throws PuiCommonImportExportInvalidModelException If the model is not
	 *                                                    configured for
	 *                                                    import/export
	 */
	private void checkModelAvailable(String model) throws PuiCommonImportExportInvalidModelException {
		reloadModels(false);

		synchronized (modelsCache) {
			if (!modelsCache.contains(model)) {
				throw new PuiCommonImportExportInvalidModelException(model);
			}
		}
	}

	/**
	 * Get all the columns from the table DTO classes associated with the model
	 * 
	 * @param model The model to get its exportable columns
	 * @return The list of columns to be exported
	 * @throws PuiCommonImportExportInvalidModelException If model is not available
	 *                                                    for import/export
	 */
	private List<ExportColumnDefinition> getExportableColumns(String model)
			throws PuiCommonImportExportInvalidModelException {
		checkModelAvailable(model);
		List<ExportColumnDefinition> columns = new ArrayList<>();
		iterateAllTableDtoInterfaces(model, superDtoIface -> {
			AtomicInteger i = new AtomicInteger(0);
			DtoRegistry.getAllColumnNames(superDtoIface).forEach(colName -> {
				columns.removeIf(col -> col.getName().equals(colName));
				String fieldName = DtoRegistry.getFieldNameFromColumnName(superDtoIface, colName);
				columns.add(i.getAndIncrement(), ExportColumnDefinition.of(colName, fieldName, -1, null));
			});
		});

		IntStream.range(0, columns.size()).boxed().forEach(i -> columns.get(i).setOrder(i));

		return columns;
	}

	/**
	 * Check all the exported columns for the given model. All the pk columns should
	 * be included, and all the included columns should exists in the model
	 * 
	 * @param model         The model to check its columns
	 * @param exportColumns The columns to be checked
	 * @throws PuiCommonImportExportPkNotIncludedException If any PK column of the
	 *                                                     model is not included
	 * @throws PuiCommonImportExportInvalidColumnException If any included column is
	 *                                                     invalid due to does not
	 *                                                     really belong to the
	 *                                                     model
	 * @throws PuiCommonImportExportInvalidModelException  If the given model is not
	 *                                                     enabled for import/export
	 */
	private void checkColumns(String model, List<String> exportColumns)
			throws PuiCommonImportExportPkNotIncludedException, PuiCommonImportExportInvalidColumnException,
			PuiCommonImportExportInvalidModelException {
		Class<? extends ITableDto> dtoClass = getTableDtoClass(model);
		List<String> columnNames = getExportableColumns(model).stream().map(ExportColumnDefinition::getName)
				.collect(Collectors.toList());
		List<String> pkColumnNames = DtoRegistry.getPkFields(dtoClass).stream()
				.map(pkField -> DtoRegistry.getColumnNameFromFieldName(dtoClass, pkField)).collect(Collectors.toList());

		// pk is included?
		if (pkColumnNames.size() != exportColumns.stream().filter(pkColumnNames::contains).count()) {
			throw new PuiCommonImportExportPkNotIncludedException(pkColumnNames.toArray(new String[0]));
		}

		// any invalid column?
		Optional<String> ecd = exportColumns.stream().filter(col -> !columnNames.contains(col)).findAny();
		if (ecd.isPresent()) {
			throw new PuiCommonImportExportInvalidColumnException(ecd.get());
		}
	}

	/**
	 * Iterate all table DTO interfaces and execute the given consumer over each
	 * implementation class interface
	 * 
	 * @param model    The model to retrieve its interfaces
	 * @param function The consumer to execute for the implementation (and not
	 *                 abstract) class of each interface
	 */
	@SuppressWarnings("unchecked")
	private void iterateAllTableDtoInterfaces(String model, Consumer<Class<? extends ITableDto>> function) {
		Class<? extends ITableDto> dtoClass = getTableDtoClass(model);
		for (Class<?> iface : ClassUtils.getAllInterfaces(dtoClass)) {
			if (!ITableDto.class.isAssignableFrom(iface)) {
				continue;
			}

			Class<? extends ITableDto> superDtoClass = DtoRegistry
					.getDtoImplementation((Class<? extends ITableDto>) iface);
			if (Modifier.isAbstract(superDtoClass.getModifiers())) {
				continue;
			}

			function.accept(superDtoClass);
		}
	}

	/**
	 * Get the table DTO class associated with the model, if exists
	 * 
	 * @param model The model to retrieve the table DTO class
	 * @return The table DTO class associated with the model
	 */
	private Class<? extends ITableDto> getTableDtoClass(String model) {
		Class<? extends ITableDto> dtoClass = daoRegistry.getTableDtoFromModelId(model, false, false);
		if (dtoClass == null) {
			SearchRequest req = new SearchRequest();
			req.setModel(model);
			IPuiModel puiModel;
			try {
				puiModel = modelService.guessModel(req);
			} catch (PuiServiceGetException e) {
				return null;
			}
			if (puiModel == null) {
				return null;
			}

			dtoClass = daoRegistry
					.getTableDtoFromViewDto(daoRegistry.getDtoFromEntityName(puiModel.getEntity(), false, false));
		}

		return dtoClass;
	}

	/**
	 * Converts an Object into an String
	 * 
	 * @param value The Object to be converted
	 * @return The corresponding String
	 */
	private String asString(Object value) {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}

		return value.toString();
	}

	/**
	 * Convert an Object into a BigDecimal
	 * 
	 * @param value The Object to be converted
	 * @return The corresponding BigDecimal
	 * @throws IllegalFormatConversionException If the Object could not be converted
	 *                                          into a BigDecimal
	 */
	private BigDecimal asBigDecimal(Object value) throws IllegalFormatConversionException {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}

		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		} else {
			try {
				return new BigDecimal(value.toString().replace(',', '.'));
			} catch (Exception e) {
				throw new IllegalFormatConversionException('a', BigDecimal.class);
			}
		}
	}

	@Component
	public class ExportUtil {

		public List<ExportColumnDefinition> getExportableColumns(String model)
				throws PuiCommonImportExportInvalidModelException {
			return PuiImportExportAction.this.getExportableColumns(model);
		}

		public FileDownload export(ExportRequest req)
				throws PuiCommonImportExportNoModelException, PuiCommonImportExportPkNotIncludedException,
				PuiCommonImportExportInvalidColumnException, PuiCommonImportExportInvalidModelException {
			if (ObjectUtils.isEmpty(req.getModel())) {
				throw new PuiCommonImportExportNoModelException();
			}

			checkModelAvailable(req.getModel());
			Collections.sort(req.getExportColumns());
			fillColumns(req);
			checkColumns(req.getModel(),
					req.getExportColumns().stream().map(ExportColumnDefinition::getName).collect(Collectors.toList()));
			modifyRequestOrder(req);

			List<ExportColumnDefinition> bakColumns = req.getExportColumns();

			req.setPage(SearchRequest.DEFAULT_PAGE);
			req.setRows(1000);

			if (ObjectUtils.isEmpty(req.getQueryLang())) {
				req.setQueryLang(PuiUserSession.getSessionLanguage().getIsocode());
			}

			// backup the columns and order or the original request
			req.setExportColumns(null);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CsvWriter writer = new CsvWriter(baos, StandardCharsets.UTF_8);

			generateTableHeader(writer, bakColumns);

			// retrieve registries from view
			findFromView(req, list -> {
				// retrieve registries from table
				FilterBuilder pkFilterBuilder = mountFilterWithPks(req.getModel(), list);
				SearchRequest searchReq = new SearchRequest();
				searchReq.setModel(req.getModel());
				searchReq.setFilter(pkFilterBuilder.asFilterGroup());
				searchReq.setOrder(req.getOrder());
				searchReq.setPage(SearchRequest.DEFAULT_PAGE);
				searchReq.setRows(req.getRows());
				searchReq.setPerformCount(false);
				List<ITableDto> recordsFromTable = findFromTable(searchReq);

				List<List<Pair<String, Object>>> data = convertData(bakColumns, recordsFromTable);
				generateTableContent(writer, req.getModel(), data);
			});

			writer.close();

			InputStream is = new ByteArrayInputStream(baos.toByteArray());
			try {
				baos.close();
			} catch (IOException e) {
				// do nothing
			}

			return new FileDownload(is, req.getModel() + CSV_FILE_EXTENSION);
		}

		private void fillColumns(ExportRequest req) {
			if (ObjectUtils.isEmpty(req.getExportColumns())) {
				req.setExportColumns(new ArrayList<>());
			}

			Class<? extends ITableDto> tableDto = getTableDtoClass(req.getModel());

			DtoRegistry.getPkFields(tableDto).stream()
					.map(pkField -> DtoRegistry.getColumnNameFromFieldName(tableDto, pkField))
					.filter(pk -> req.getExportColumns().stream().map(ExportColumnDefinition::getName)
							.noneMatch(name -> Objects.equals(name, pk)))
					.forEach(pk -> req.getExportColumns().add(0, ExportColumnDefinition.of(pk, pk, -1, null)));

			// include mandatory columns
			iterateAllTableDtoInterfaces(req.getModel(),
					superDtoIface -> DtoRegistry.getNotNullFields(superDtoIface).stream()
							.map(notNull -> DtoRegistry.getColumnNameFromFieldName(superDtoIface, notNull))
							.filter(notNull -> req.getExportColumns().stream().map(ExportColumnDefinition::getName)
									.noneMatch(name -> Objects.equals(name, notNull)))
							.forEach(notNull -> req.getExportColumns()
									.add(ExportColumnDefinition.of(notNull, notNull, -1, null))));
		}

		private void modifyRequestOrder(ExportRequest req) {
			if (ObjectUtils.isEmpty(req.getOrder())) {
				List<String> pkFieldNames = DtoRegistry.getPkFields(getTableDtoClass(req.getModel()));
				List<Order> order = new ArrayList<>();
				pkFieldNames.forEach(pk -> order.add(Order.newOrderAsc(pk)));
				req.setOrder(order);
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private List<ITableDto> findFromTable(SearchRequest req) {
			try {
				Class<? extends IService> serviceClass = serviceRegistry.getServiceFromModelId(req.getModel());
				IService service = PuiApplicationContext.getInstance().getBean(serviceClass);
				return service.searchTable(req).getData();
			} catch (PuiServiceGetException e) {
				return Collections.emptyList();
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void findFromView(ExportRequest req, Consumer<List<IViewDto>> consumer) {
			Class<? extends IService> serviceClass = serviceRegistry.getServiceFromModelId(req.getModel());
			IService service = PuiApplicationContext.getInstance().getBean(serviceClass);
			service.getViewDao().executePaginagedOperation(req, null, consumer);
		}

		private FilterBuilder mountFilterWithPks(String model, List<IViewDto> recordsFromView) {
			FilterBuilder filterBuilder = FilterBuilder.newOrFilter();
			if (ObjectUtils.isEmpty(recordsFromView)) {
				return filterBuilder;
			}

			Class<? extends ITableDto> dtoClass = getTableDtoClass(model);
			List<String> pkFieldNames = DtoRegistry.getPkFields(dtoClass);

			if (pkFieldNames.size() == 1) {
				// the PK is simple
				String pkFieldName = pkFieldNames.iterator().next();
				Field pkField = DtoRegistry.getJavaFieldFromFieldName(recordsFromView.get(0).getClass(), pkFieldName);
				boolean isNumeric = DtoRegistry.getNumericFields(dtoClass).contains(pkFieldName);
				boolean isString = DtoRegistry.getStringFields(dtoClass).contains(pkFieldName);
				if (isNumeric) {
					List<Number> pks = new ArrayList<>();
					recordsFromView.forEach(rec -> {
						try {
							pks.add((Number) pkField.get(rec));
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// continue
						}
					});
					filterBuilder.addIn(pkFieldName, pks);
				} else if (isString) {
					List<String> pks = new ArrayList<>();
					recordsFromView.forEach(rec -> {
						try {
							pks.add((String) pkField.get(rec));
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// continue
						}
					});
					filterBuilder.addIn(pkFieldName, pks);
				}
			} else {
				// the PK is composed
				recordsFromView.forEach(rec -> {
					FilterBuilder fbRecord = FilterBuilder.newAndFilter();
					pkFieldNames.forEach(pkName -> {
						Field pkField = DtoRegistry.getJavaFieldFromFieldName(rec.getClass(), pkName);
						try {
							if (DtoRegistry.getStringFields(dtoClass).contains(pkName)) {
								fbRecord.addEqualsExact(pkName, (String) pkField.get(rec));
							} else {
								fbRecord.addEquals(pkName, pkField.get(rec));
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// continue
						}
					});
					filterBuilder.addGroup(fbRecord);
				});
			}

			return filterBuilder;
		}

		private List<List<Pair<String, Object>>> convertData(List<ExportColumnDefinition> exportColumns,
				List<ITableDto> data) {
			List<List<Pair<String, Object>>> list = new ArrayList<>();

			data.forEach(d -> {
				List<Pair<String, Object>> newData = new ArrayList<>();
				exportColumns.forEach(ec -> {
					String fieldName = DtoRegistry.getFieldNameFromColumnName(d.getClass(), ec.getName());
					Object value;
					try {
						value = PropertyUtils.getProperty(d, fieldName);
					} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
						value = null;
					}
					newData.add(Pair.of(ec.getName(), value));
				});
				list.add(newData);
			});

			return list;
		}

		private void generateTableHeader(CsvWriter writer, List<ExportColumnDefinition> exportColumns) {
			// first row is the name of the column
			exportColumns.forEach(ec -> {
				try {
					writer.write(ec.getName());
				} catch (IOException e) {
					// do nothing
				}
			});
			try {
				writer.endRecord();
			} catch (IOException e) {
				// do nothing
			}

			// second row is the title of the column
			exportColumns.forEach(ec -> {
				try {
					writer.write(ec.getTitle());
				} catch (IOException e) {
					// do nothing
				}
			});
			try {
				writer.endRecord();
			} catch (IOException e) {
				// do nothing
			}
		}

		private void generateTableContent(CsvWriter writer, String model, List<List<Pair<String, Object>>> data) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PuiUserSession.getCurrentSession() != null
					? PuiUserSession.getCurrentSession().getDateformat() + " HH:mm:ss"
					: "yyyy-MM-dd HH:mm:ss");

			data.forEach(rec -> {
				rec.forEach(pair -> {
					AtomicReference<Class<? extends IDto>> superInterface = new AtomicReference<>();
					AtomicReference<String> fieldname = new AtomicReference<>();
					iterateAllTableDtoInterfaces(model, superIface -> {
						String fieldName = DtoRegistry.getFieldNameFromColumnName(superIface, pair.getKey());
						if (fieldName == null) {
							return;
						}

						fieldname.set(fieldName);
						superInterface.set(superIface);
					});

					String value = null;
					if (DtoRegistry.getDateTimeFields(superInterface.get()).contains(fieldname.get())) {
						value = instantAsString((Instant) pair.getValue(), dtf);
					} else if (DtoRegistry.getFloatingFields(superInterface.get()).contains(fieldname.get())) {
						value = asString(asBigDecimal(pair.getValue()));
					} else {
						value = asString(pair.getValue());
					}

					try {
						writer.write(value, true);
					} catch (IOException e) {
						// do nothing
					}
				});

				try {
					writer.endRecord();
				} catch (IOException e) {
					// do nothing
				}
			});
		}

		/**
		 * Convert an Instant as String
		 * 
		 * @param value The Instant to be converted
		 * @param dtf   The format of the converted String
		 * @return The converted String
		 */
		private String instantAsString(Instant value, DateTimeFormatter dtf) {
			ZonedDateTime zdt = PuiDateUtil.getInstantAtZoneId(value,
					PuiUserSession.getCurrentSession() != null ? PuiUserSession.getCurrentSession().getZoneId()
							: ZoneId.systemDefault());
			return PuiDateUtil.temporalAccessorToString(zdt, dtf);
		}

	}

	@Component
	public class ImportUtil {

		private static final String PK_SEPARATOR = "#";
		private static final String NEW_RECORD_PREFIX = "NEW_";

		@Transactional(rollbackFor = PuiException.class)
		public ImportData prepareImport(String model, InputStream is)
				throws PuiCommonImportExportPkNotIncludedException, PuiCommonImportExportInvalidColumnException,
				PuiCommonImportExportInvalidModelException, PuiServiceInsertException {
			checkModelAvailable(model);

			byte[] bytes;
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				IOUtils.copy(is, os);
				bytes = os.toByteArray();
			} catch (IOException e) {
				return null;
			}

			CsvReader reader = new CsvReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8, true);

			List<String> columnNames = readColumnNames(reader);
			List<String> columnTitles = readColumnTitles(reader);
			checkColumns(model, columnNames);
			List<String> pkFields = DtoRegistry.getPkFields(getTableDtoClass(model));

			ImportData importData = new ImportData(PuiUserSession.getCurrentSession().getUsr(), model,
					PuiUserSession.getSessionLanguage().getIsocode(), pkFields, columnNames, columnTitles);
			readRecords(reader, importData);

			FilterBuilder filterBuilder = mountFilterWithPks(importData);
			List<ITableDto> dtos = findFromTable(model, filterBuilder, importData.getLanguage());
			compareRecords(importData, dtos);
			saveToDatabase(importData);
			copyToFileSystem(new ByteArrayInputStream(bytes), importData);

			return importData;
		}

		@Transactional(rollbackFor = PuiException.class)
		public void performImport(IPuiImportexportPk pk) throws PuiServiceException {
			IPuiImportexport importExport = importExportService.getByPk(pk);
			if (importExport.getExecuted().equals(PuiConstants.TRUE_INT)) {
				throw new PuiServiceException(new Exception("Ya se había ejecutado"));
			}

			String importFolder = importExportService.getImportFolder(importExport.getModel(), importExport.getId());
			String fileName = getFileName(importExport.getDatetime(), importExport.getUsr());

			String jsonFileName = importFolder + fileName + JSON_FILE_EXTENSION;

			String json;
			try {
				json = FileUtils.readFileToString(new File(jsonFileName), StandardCharsets.UTF_8);
			} catch (IOException e) {
				return;
			}

			ImportData importData = GsonSingleton.getSingleton().getGson().fromJson(json, ImportData.class);
			checkImportData(importData);
			executeImport(pk, importData);
		}

		@Transactional(rollbackFor = PuiException.class)
		public void cancelImport(IPuiImportexportPk pk) throws PuiServiceException {
			IPuiImportexport importExport;
			try {
				importExport = importExportService.getByPk(pk);
			} catch (PuiServiceGetException e) {
				return;
			}

			String importFolder = importExportService.getImportFolder(importExport.getModel(), importExport.getId());
			FileUtils.deleteQuietly(new File(importFolder));

			importExportService.delete(pk);
		}

		private List<String> readColumnNames(CsvReader reader) {
			List<String> columnNames = new ArrayList<>();

			// read the headers in the first row
			try {
				reader.readHeaders();
				columnNames.addAll(Arrays.asList(reader.getHeaders()));
			} catch (IOException e) {
				// do nothing
			}

			return columnNames;
		}

		private List<String> readColumnTitles(CsvReader reader) {
			List<String> columnTitles = new ArrayList<>();

			// read the second row, that contains the title of the columns
			try {
				reader.readRecord();
				columnTitles.addAll(Arrays.asList(reader.getValues()));
			} catch (IOException e) {
				// do nothing
			}

			return columnTitles;
		}

		private void readRecords(CsvReader reader, ImportData importData) {
			try {
				AtomicReference<String> language = new AtomicReference<>();
				int i = 1;
				Class<? extends ITableDto> dtoClass = getTableDtoClass(importData.getModel());
				while (reader.readRecord()) {
					ImportDataRecord rec = new ImportDataRecord();
					importData.getColumns().forEach(col -> {
						String value;
						try {
							value = reader.get(col);
						} catch (IOException e) {
							return;
						}

						AtomicReference<Class<? extends IDto>> superInterface = new AtomicReference<>();
						boolean hasError = false;

						iterateAllTableDtoInterfaces(importData.getModel(), superIface -> {
							if (!DtoRegistry.getAllColumnNames(superIface).contains(col)) {
								return;
							}
							superInterface.set(superIface);
						});

						Object val = null;
						if (DtoRegistry.getDateTimeFields(superInterface.get()).contains(col)) {
							val = stringAsInstant(value);
						} else if (DtoRegistry.getFloatingFields(superInterface.get()).contains(col)) {
							val = asBigDecimal(value);
						} else if (DtoRegistry.getNumericFields(superInterface.get()).contains(col)) {
							if (DtoRegistry.getJavaFieldFromColumnName(superInterface.get(), col).getType()
									.equals(Integer.class)) {
								val = stringAsInteger(value);
							} else if (DtoRegistry.getJavaFieldFromColumnName(superInterface.get(), col).getType()
									.equals(Long.class)) {
								val = stringAsLong(value);
							}
						} else {
							val = asString(value);
						}

						rec.addAttribute(col, new ImportDataAttribute(val, hasError));
						if (hasError) {
							rec.setStatus(ImportDataRecordStatus.ERROR);
						}

						if (IDto.LANG_COLUMN_NAME.equals(col) && language.get() == null) {
							language.set(val.toString());
						}
					});

					StringBuilder pkBuilder = new StringBuilder();
					for (Iterator<String> pkIt = importData.getPks().iterator(); pkIt.hasNext();) {
						String colName = DtoRegistry.getColumnNameFromFieldName(dtoClass, pkIt.next());
						ImportDataAttribute ida = rec.getAttributes().get(colName);
						if (ida.getValue() != null) {
							pkBuilder.append(ida.getValue());
							if (pkIt.hasNext()) {
								pkBuilder.append(PK_SEPARATOR);
							}
						} else {
							pkBuilder = new StringBuilder(NEW_RECORD_PREFIX + (i++));
							break;
						}
					}

					if (pkBuilder.indexOf(NEW_RECORD_PREFIX) >= 0) {
						if (rec.getStatus().equals(ImportDataRecordStatus.ERROR)) {
							rec.setStatus(ImportDataRecordStatus.NEW_ERROR);
						} else {
							rec.setStatus(ImportDataRecordStatus.NEW);
						}
					}

					importData.addRecord(pkBuilder.toString(), rec);
				}

				if (language.get() != null) {
					importData.setLanguage(language.get());
				}

				reader.close();
			} catch (IOException e) {
				// do nothing
			}
		}

		private FilterBuilder mountFilterWithPks(ImportData importData) {
			Class<? extends ITableDto> dtoClass = getTableDtoClass(importData.getModel());
			FilterBuilder filterBuilder = FilterBuilder.newOrFilter();
			if (ObjectUtils.isEmpty(importData.getRecords())) {
				return filterBuilder;
			}

			if (importData.getPks().size() == 1) {
				// the PK is simple
				String pkFieldName = importData.getPks().get(0);
				boolean isNumeric = DtoRegistry.getNumericFields(dtoClass).contains(pkFieldName);
				boolean isString = DtoRegistry.getStringFields(dtoClass).contains(pkFieldName);
				String pkColName = DtoRegistry.getColumnNameFromFieldName(dtoClass, pkFieldName);
				if (isNumeric) {
					List<Number> pks = new ArrayList<>();
					importData.getRecords().keySet().stream().filter(pk -> !pk.startsWith(NEW_RECORD_PREFIX))
							.forEach(pk -> pks.add(Integer.parseInt(pk)));
					filterBuilder.addIn(pkColName, pks);
				} else if (isString) {
					List<String> pks = new ArrayList<>();
					importData.getRecords().keySet().forEach(pks::add);
					filterBuilder.addIn(pkColName, pks);
				}
			} else {
				// the PK is composed
				importData.getRecords().keySet().stream().filter(pk -> !pk.startsWith(NEW_RECORD_PREFIX))
						.forEach(pkRec -> {
							FilterBuilder fbRecord = FilterBuilder.newAndFilter();
							String[] pkValues = pkRec.split(PK_SEPARATOR);
							for (int i = 0; i < pkValues.length; i++) {
								String pkColName = DtoRegistry.getColumnNameFromFieldName(dtoClass,
										importData.getPks().get(i));
								fbRecord.addEqualsExact(pkColName, pkValues[i]);
							}
							filterBuilder.addGroup(fbRecord);
						});
			}

			return filterBuilder;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private List<ITableDto> findFromTable(String model, FilterBuilder filterBuilder, String language) {
			try {
				Class<? extends ITableDao> tableDaoClass = daoRegistry.getTableDaoFromModelId(model);
				ITableDao tableDao = PuiApplicationContext.getInstance().getBean(tableDaoClass);
				return tableDao.findWhere(filterBuilder, new PuiLanguage(language));
			} catch (PuiDaoFindException e) {
				return Collections.emptyList();
			}
		}

		private void compareRecords(ImportData importData, List<ITableDto> dtos) {
			Class<? extends ITableDto> dtoClass = getTableDtoClass(importData.getModel());

			for (ITableDto dto : dtos) {
				StringBuilder pkBuilder = new StringBuilder();
				for (String pkField : importData.getPks()) {
					if (pkBuilder.length() > 0) {
						pkBuilder.append(PK_SEPARATOR);
					}
					try {
						pkBuilder.append(DtoRegistry.getJavaFieldFromFieldName(dtoClass, pkField).get(dto));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						pkBuilder = null;
						break;
					}
				}

				if (pkBuilder == null) {
					continue;
				}

				ImportDataRecord idr = importData.getRecords().get(pkBuilder.toString());
				if (idr == null) {
					continue;
				}

				boolean recordHasChanged = false;
				AtomicBoolean recordHasErrror = new AtomicBoolean(idr.getStatus().equals(ImportDataRecordStatus.ERROR));
				for (String colName : idr.getAttributes().keySet()) {
					ImportDataAttribute ida = idr.getAttributes().get(colName);

					AtomicReference<Class<? extends IDto>> superInterface = new AtomicReference<>();
					iterateAllTableDtoInterfaces(importData.getModel(), superIface -> {
						if (!DtoRegistry.getAllColumnNames(superIface).contains(colName)) {
							return;
						}

						superInterface.set(superIface);
					});

					String fieldName = DtoRegistry.getFieldNameFromColumnName(dtoClass, colName);

					Object dtoAttrValue;
					Field field = DtoRegistry.getJavaFieldFromFieldName(superInterface.get(), fieldName);
					if (field == null) {
						field = DtoRegistry.getJavaFieldFromFieldName(superInterface.get(), fieldName);
					}

					try {
						dtoAttrValue = PropertyUtils.getProperty(dto, fieldName);
						if (DtoRegistry.getStringFields(superInterface.get()).contains(fieldName)
								&& ObjectUtils.isEmpty(dtoAttrValue)) {
							dtoAttrValue = null;
						}
					} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException
							| NoSuchMethodException e) {
						ida.setStatus(ImportDataAttributeStatus.ERROR);
						recordHasErrror.set(true);
						break;
					}

					if (ida.getStatus().equals(ImportDataAttributeStatus.ERROR)) {
						ida.setOldValue(dtoAttrValue);
						continue;
					}

					if (dtoAttrValue == null && ida.getValue() == null) {
						ida.setStatus(ImportDataAttributeStatus.UNMODIFIED);
					} else if (dtoAttrValue == null && ida.getValue() != null) {
						if (field.getType().equals(ida.getValue().getClass())) {
							ida.setStatus(ImportDataAttributeStatus.MODIFIED);
							ida.setOldValue(dtoAttrValue);
							recordHasChanged = true;
						} else {
							ida.setStatus(ImportDataAttributeStatus.ERROR);
							recordHasErrror.set(true);
							break;
						}
					} else if (dtoAttrValue != null && ida.getValue() == null) {
						if (DtoRegistry.getNotNullFields(superInterface.get()).contains(fieldName)) {
							ida.setStatus(ImportDataAttributeStatus.ERROR);
							recordHasErrror.set(true);
							break;
						} else {
							ida.setStatus(ImportDataAttributeStatus.MODIFIED);
							ida.setOldValue(dtoAttrValue);
							recordHasChanged = true;
						}
					} else if (dtoAttrValue != null && ida.getValue() != null) {
						if (Objects.equals(dtoAttrValue, ida.getValue())) {
							ida.setStatus(ImportDataAttributeStatus.UNMODIFIED);
						} else if (dtoAttrValue.getClass().equals(ida.getValue().getClass())) {
							ida.setStatus(ImportDataAttributeStatus.MODIFIED);
							ida.setOldValue(dtoAttrValue);
							recordHasChanged = true;
						} else {
							ida.setStatus(ImportDataAttributeStatus.ERROR);
							recordHasErrror.set(true);
							break;
						}
					}
				}

				if (recordHasErrror.get()) {
					idr.setStatus(ImportDataRecordStatus.ERROR);
				} else if (recordHasChanged) {
					idr.setStatus(ImportDataRecordStatus.MODIFIED);
				} else {
					idr.setStatus(ImportDataRecordStatus.UNMODIFIED);
				}
			}

			importData.getRecords().values().forEach(rec ->

			{
				switch (rec.getStatus()) {
				case UNMODIFIED:
					importData.addUnmodifiedRecord();
					break;
				case MODIFIED:
					importData.addModifiedRecord();
					break;
				case NEW:
					importData.addNewRecord();
					break;
				case NEW_ERROR:
					importData.addNewWithErrorsRecord();
					break;
				case ERROR:
					importData.addErrorRecord();
					break;
				}
			});
		}

		private void copyToFileSystem(InputStream is, ImportData importData) {
			String csv;
			try {
				csv = IOUtils.toString(is, StandardCharsets.UTF_8);
			} catch (IOException e) {
				// do nothing
				return;
			}
			String json = GsonSingleton.getSingleton().getGson().toJson(importData);

			String importFolder = importExportService.getImportFolder(importData.getModel(), importData.getId());
			String fileName = getFileName(importData.getImportTime(), importData.getUser());

			String csvFileName = importFolder + fileName + CSV_FILE_EXTENSION;
			String jsonFileName = importFolder + fileName + JSON_FILE_EXTENSION;
			try {
				FileUtils.write(new File(csvFileName), csv, StandardCharsets.UTF_8);
				FileUtils.write(new File(jsonFileName), json, StandardCharsets.UTF_8);
			} catch (IOException e) {
				// do nothing
			}
		}

		private void saveToDatabase(ImportData importData) throws PuiServiceInsertException {
			String fileName = getFileName(importData.getImportTime(), importData.getUser());

			IPuiImportexport importExport = new PuiImportexport();
			importExport.setModel(importData.getModel());
			importExport.setUsr(importData.getUser());
			importExport.setDatetime(importData.getImportTime());
			importExport.setFilenamecsv(fileName + CSV_FILE_EXTENSION);
			importExport.setFilenamejson(fileName + JSON_FILE_EXTENSION);
			importExport.setExecuted(PuiConstants.FALSE_INT);

			importExport = importExportService.insert(importExport);
			importData.setId(importExport.getId());
		}

		private void checkImportData(ImportData importData) throws PuiCommonImportExportWithErrorsException {
			if (importData.getErrorRecords() > 0) {
				throw new PuiCommonImportExportWithErrorsException();
			}

			for (ImportDataRecord rec : importData.getRecords().values()) {
				if (rec.getStatus().equals(ImportDataRecordStatus.ERROR)) {
					throw new PuiCommonImportExportWithErrorsException();
				}
				if (rec.getStatus().equals(ImportDataRecordStatus.MODIFIED)
						|| rec.getStatus().equals(ImportDataRecordStatus.NEW)) {
					rec.getAttributes().forEach(
							(field, attr) -> iterateAllTableDtoInterfaces(importData.getModel(), superInterface -> {
								if (!DtoRegistry.getAllFields(superInterface).contains(field)) {
									return;
								}
								if (DtoRegistry.getDateTimeFields(superInterface).contains(field)
										&& attr.getValue() instanceof String) {
									attr.setValue(stringAsInstant((String) attr.getValue()));
								}
								if (DtoRegistry.getFloatingFields(superInterface).contains(field)
										&& attr.getValue() instanceof Integer) {
									attr.setValue(new BigDecimal((Integer) attr.getValue()));
								}
							}));
				}
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void executeImport(IPuiImportexportPk pk, ImportData importData) throws PuiServiceException {
			List<ITableDto> updateRecords = new ArrayList<>();
			List<ITableDto> insertRecords = new ArrayList<>();
			for (ImportDataRecord rec : importData.getRecords().values()) {
				if (rec.getStatus().equals(ImportDataRecordStatus.MODIFIED)) {
					updateRecords.add(populateNewRecord(importData.getModel(), importData.getLanguage(), rec));
				} else if (rec.getStatus().equals(ImportDataRecordStatus.NEW)) {
					insertRecords.add(populateNewRecord(importData.getModel(), importData.getLanguage(), rec));
				}
			}

			Class<? extends IService> serviceClass = serviceRegistry.getServiceFromModelId(importData.getModel());
			IService service = PuiApplicationContext.getInstance().getBean(serviceClass);
			service.bulkUpdate(updateRecords);
			service.bulkInsert(insertRecords);

			importExportService.patch(pk,
					Collections.singletonMap(IPuiImportexport.EXECUTED_FIELD, PuiConstants.TRUE_INT));
		}

		private ITableDto populateNewRecord(String model, String language, ImportDataRecord rec) {
			Map<String, Object> mapValues = new LinkedHashMap<>();
			rec.getAttributes().forEach((attribute, value) -> mapValues.put(attribute, value.getValue()));

			if (daoRegistry.hasLanguageSupport(daoRegistry.getTableDaoFromModelId(model))) {
				mapValues.put(IDto.LANG_COLUMN_NAME, language);
			}

			Class<? extends ITableDto> dtoClass = daoRegistry.getTableDtoFromModelId(model, false);
			return DtoFactory.createInstanceFromInterface(dtoClass, mapValues);
		}

		private String getFileName(Instant importTime, String user) {
			String importTimeStr = PuiDateUtil.temporalAccessorToString(importTime,
					DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
			return importTimeStr + "_" + user;
		}

		/**
		 * Convert an String into an Integer
		 * 
		 * @param value The String to be converted
		 * @return The corresponding Integer
		 * @throws IllegalFormatConversionException If the String could not be converted
		 *                                          into an Integer
		 */
		private Integer stringAsInteger(String value) throws IllegalFormatConversionException {
			if (ObjectUtils.isEmpty(value)) {
				return null;
			}

			try {
				return Integer.valueOf(value);
			} catch (Exception e) {
				throw new IllegalFormatConversionException('a', Integer.class);
			}
		}

		/**
		 * Convert an String into a Long
		 * 
		 * @param value The String to be converted
		 * @return The corresponding Long
		 * @throws IllegalFormatConversionException If the String could not be converted
		 *                                          into a Long
		 */
		private Long stringAsLong(String value) throws IllegalFormatConversionException {
			if (ObjectUtils.isEmpty(value)) {
				return null;
			}

			try {
				return Long.valueOf(value);
			} catch (Exception e) {
				throw new IllegalFormatConversionException('a', Long.class);
			}
		}

		/**
		 * Convert an String into an Instant
		 * 
		 * @param value The String to be converted
		 * @return The corresponding Instant
		 * @throws IllegalFormatConversionException If the String could not be converted
		 *                                          into an Instant
		 */
		private Instant stringAsInstant(String value) throws IllegalFormatConversionException {
			if (ObjectUtils.isEmpty(value)) {
				return null;
			}

			try {
				return Instant.parse(value);
			} catch (Exception e1) {
				try {
					LocalDateTime ldt = PuiDateUtil.stringToLocalDateTime(value,
							PuiUserSession.getCurrentSession() != null ? PuiUserSession.getCurrentSession().getZoneId()
									: ZoneId.systemDefault());
					if (ldt == null) {
						throw new IllegalFormatConversionException('a', Instant.class);
					}
					ZonedDateTime zdt = ZonedDateTime.of(ldt,
							PuiUserSession.getCurrentSession() != null ? PuiUserSession.getCurrentSession().getZoneId()
									: ZoneId.systemDefault());
					return zdt.toInstant();
				} catch (Exception e2) {
					throw new IllegalFormatConversionException('a', Instant.class);
				}
			}
		}

	}

}
