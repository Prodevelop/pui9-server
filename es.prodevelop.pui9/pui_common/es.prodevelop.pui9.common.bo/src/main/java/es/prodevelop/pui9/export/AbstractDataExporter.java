package es.prodevelop.pui9.export;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.list.adapters.IListAdapter;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.search.ExportColumnDefinition;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.services.exceptions.PuiServiceExportException;
import es.prodevelop.pui9.utils.PuiDateUtil;

public abstract class AbstractDataExporter implements IDataExporter {

	@Autowired
	protected DaoRegistry daoRegistry;

	@Autowired
	private IPuiModelService modelService;

	protected AbstractDataExporter() {
		DataExporterRegistry.getSingleton().registerExporter(this);
	}

	@Override
	public final FileDownload export(ExportRequest req) throws PuiServiceExportException {
		req.setPerformCount(false);
		req.setExportTitle(!ObjectUtils.isEmpty(req.getExportTitle()) ? req.getExportTitle() : req.getModel());

		fillColumns(req);
		fillDatabaseFilters(req);

		InputStream is = doExport(req);
		return createFileDownload(req.getExportTitle(), is);
	}

	/**
	 * Fill the request with the available database fixed filters
	 * 
	 * @param req
	 */
	private void fillDatabaseFilters(ExportRequest req) {
		try {
			IPuiModel puiModel = modelService.guessModel(req);
			if (puiModel != null && !ObjectUtils.isEmpty(puiModel.getFilter())) {
				if (puiModel.getFilter().equals(IListAdapter.SEARCH_PARAMETER)) {
					req.setDbFilters(IListAdapter.EMPTY_FILTER);
				} else {
					try {
						req.setDbFilters(FilterGroup.fromJson(puiModel.getFilter()));
					} catch (JsonSyntaxException e) {
						// do nothing
					}
				}
			} else {
				req.setDbFilters(null);
			}
		} catch (PuiServiceGetException e) {
			// do nothing
		}
	}

	/**
	 * Do the generation of the export data
	 * 
	 * @param req The request of the export
	 * @return The generated input stream
	 * @throws PuiServiceExportException If any error occurs during the export
	 */
	protected abstract InputStream doExport(ExportRequest req) throws PuiServiceExportException;

	protected void generateDetail(ExportRequest req, Consumer<List<List<Pair<String, Object>>>> consumer) {
		Class<IDao<? extends IDto>> daoClass = daoRegistry.getDaoFromDto(req.getDtoClass());
		IDao<? extends IDto> dao = PuiApplicationContext.getInstance().getBean(daoClass);

		req.setRows(1000);

		AtomicLong total = new AtomicLong(0);
		dao.executePaginagedOperation(req, null, list -> {
			total.addAndGet(list.size());
			List<List<Pair<String, Object>>> data = convertData(req.getExportColumns(), req.getDtoClass(), list);
			consumer.accept(data);
		});
		req.setTotal(total.get());
	}

	/**
	 * In case the columns to be exported are not providen, fill the columns list
	 * with all the columns of the table
	 * 
	 * @param req
	 */
	private void fillColumns(ExportRequest req) {
		if (ObjectUtils.isEmpty(req.getExportColumns())) {
			List<ExportColumnDefinition> columns = new ArrayList<>();
			AtomicInteger index = new AtomicInteger(0);
			DtoRegistry.getColumnNames(req.getDtoClass()).forEach(col -> {
				ExportColumnDefinition ecd = ExportColumnDefinition.of(col, col, index.getAndIncrement(), null);
				if (DtoRegistry.getDateTimeFields(req.getDtoClass()).contains(col)) {
					ecd.setDateformat(PuiUserSession.getCurrentSession() != null
							? PuiUserSession.getCurrentSession().getDateformat() + " HH:mm:ss"
							: "yyyy-MM-dd HH:mm:ss");
				}
				columns.add(ecd);
			});
			req.setExportColumns(columns);
		}

		req.getExportColumns().removeIf(ex -> DtoRegistry.getAllColumnVisibility(req.getDtoClass())
				.getOrDefault(ex.getName(), ColumnVisibility.visible).equals(ColumnVisibility.completelyhidden));

		Collections.sort(req.getExportColumns());
	}

	protected List<List<Pair<String, Object>>> convertData(List<ExportColumnDefinition> exportColumns,
			Class<? extends IDto> dtoClass, List<?> data) {
		List<List<Pair<String, Object>>> list = new ArrayList<>();

		data.forEach(d -> {
			List<Pair<String, Object>> newData = new ArrayList<>();
			exportColumns.forEach(ec -> {
				Object value;
				if (ec.getName().contains(".")) {
					// is json value
					value = getJsonValue(dtoClass, d, ec.getName());
				} else {
					value = getBasicValue(dtoClass, d, ec.getName());
				}

				newData.add(Pair.of(ec.getName(), value));
			});
			list.add(newData);
		});

		return list;
	}

	private Object getJsonValue(Class<? extends IDto> dtoClass, Object data, String jsonFieldName) {
		String[] fieldParts = jsonFieldName.split("\\.");
		if (fieldParts == null) {
			return null;
		}

		String fieldName = fieldParts[0];
		Object value = getBasicValue(dtoClass, data, fieldName);

		JsonObject jo = GsonSingleton.getSingleton().getGson().fromJson((String) value, JsonObject.class);
		for (int i = 1; i < fieldParts.length; i++) {
			if (jo.get(fieldParts[i]) == null || jo.get(fieldParts[i]).isJsonNull()) {
				value = null;
				break;
			} else if (jo.get(fieldParts[i]).isJsonArray()) {
				value = jo.getAsJsonArray(fieldParts[i]);
				break;
			} else if (jo.get(fieldParts[i]).isJsonPrimitive()) {
				value = jo.getAsJsonPrimitive(fieldParts[i]).getAsString();
				break;
			} else if (jo.get(fieldParts[i]).isJsonObject()) {
				jo = jo.getAsJsonObject(fieldParts[i]);
			}
		}
		return value;
	}

	private Object getBasicValue(Class<? extends IDto> dtoClass, Object data, String fieldName) {
		Field field = DtoRegistry.getJavaFieldFromColumnName(dtoClass, fieldName);
		if (field == null) {
			field = DtoRegistry.getJavaFieldFromFieldName(dtoClass, fieldName);
		}

		Object value;
		try {
			value = field.get(data);
		} catch (Exception e) {
			value = null;
		}

		return value;
	}

	private FileDownload createFileDownload(String title, InputStream is) {
		return new FileDownload(is, title.replace(" ", "_") + "." + getExportType().extension);
	}

	protected ZonedDateTime getCurrentTimeAtUserTimeZone() {
		return PuiDateUtil.getInstantAtZoneId(Instant.now(),
				PuiUserSession.getCurrentSession() != null ? PuiUserSession.getCurrentSession().getZoneId()
						: ZoneId.systemDefault());
	}

	protected String getString(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof BigDecimal) {
			return String.valueOf(((BigDecimal) value).doubleValue());
		}
		return value.toString();
	}

	protected String getInstantAsString(Object value, String dateformat) {
		ZoneId zoneId = PuiUserSession.getCurrentSession() != null ? PuiUserSession.getCurrentSession().getZoneId()
				: ZoneId.systemDefault();
		ZonedDateTime zdt = PuiDateUtil.getInstantAtZoneId((Instant) value, zoneId);
		if (dateformat == null) {
			dateformat = PuiUserSession.getCurrentSession() != null
					? PuiUserSession.getCurrentSession().getDateformat() + " HH:mm:ss"
					: "yyyy-MM-dd HH:mm:ss";
		}

		TemporalAccessor ta;
		if (dateformat.toUpperCase().contains("HH")) {
			ta = zdt;
		} else {
			ta = zdt == null ? null : zdt.toLocalDate();
		}
		return PuiDateUtil.temporalAccessorToString(ta, DateTimeFormatter.ofPattern(dateformat).withZone(zoneId));
	}

	protected BigDecimal getBigDecimal(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		} else {
			return new BigDecimal(value.toString());
		}
	}

	protected String convertBigDecimalToString(BigDecimal value, char decimalCharacter) {
		String val = getString(value);
		if (val != null) {
			val = val.replace('.', decimalCharacter);
		}
		return val;
	}

}
