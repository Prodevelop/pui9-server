package es.prodevelop.pui9.dashboards.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.dashboards.dto.EChartsLineBarOptions;
import es.prodevelop.pui9.dashboards.dto.EChartsLineBarSeries;
import es.prodevelop.pui9.dashboards.dto.EChartsPieOptions;
import es.prodevelop.pui9.dashboards.dto.EChartsPieSeries;
import es.prodevelop.pui9.dashboards.dto.EChartsPieSeriesData;
import es.prodevelop.pui9.dashboards.dto.EChartsTypes;
import es.prodevelop.pui9.dashboards.model.dao.interfaces.IPuiWidgetDao;
import es.prodevelop.pui9.dashboards.model.dao.interfaces.IPuiWidgetTypeDao;
import es.prodevelop.pui9.dashboards.model.dto.PuiWidgetTypePk;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidget;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidgetPk;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiWidgetType;
import es.prodevelop.pui9.dashboards.model.views.dao.interfaces.IVPuiWidgetDao;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiWidget;
import es.prodevelop.pui9.dashboards.service.interfaces.IPuiWidgetService;
import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.service.AbstractService;

@PuiGenerated
@Service
public class PuiWidgetService
		extends AbstractService<IPuiWidgetPk, IPuiWidget, IVPuiWidget, IPuiWidgetDao, IVPuiWidgetDao>
		implements IPuiWidgetService {

	@Autowired
	private IPuiWidgetTypeDao puiWidgetTypeDao;

	@Override
	protected void afterGet(IPuiWidget dto) throws PuiServiceException {
		try {
			IPuiWidgetType puiWidgetType = puiWidgetTypeDao.findOne(new PuiWidgetTypePk(dto.getTypeid()));
			dto.setType(puiWidgetType.getType());
			dto.setComponent(puiWidgetType.getComponent());
		} catch (PuiDaoFindException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public EChartsPieOptions getEChartsPieOptions(String entity, String columnName, String columnValue,
			String tooltipValue, FilterGroup filter) {
		EChartsPieSeries series = new EChartsPieSeries();
		EChartsPieOptions options = new EChartsPieOptions();
		options.getSeries().add(series);

		IDao<?> dao = getDaoFromEntityName(entity);
		if (dao == null) {
			logger.debug("ViewDao for entityName " + entity + " not exist");
			return options;
		}

		List<String> tooltipValues = new ArrayList<>();
		if (tooltipValue != null) {
			tooltipValues.add(tooltipValue);
			if (tooltipValue.contains(",")) {
				tooltipValues = Arrays.asList(tooltipValue.split(","));
			}
		}

		List<EChartsDataKeyValue> echartsDataList = getEChartsDataFromDao(dao, columnName, columnValue, filter);
		int i = 0;
		for (EChartsDataKeyValue dto : echartsDataList) {
			try {
				String key = (tooltipValues.size() > i) ? tooltipValues.get(i) : dto.getKey();
				double value = Double.parseDouble(dto.getValue());
				series.getData().add(new EChartsPieSeriesData(key, value));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				// do nothing
			}
			i++;
		}

		return options;
	}

	@Override
	public EChartsLineBarOptions getEChartsLineBarOptions(String entity, String columnName, String columnValue,
			String tooltipValue, EChartsTypes type, FilterGroup filter) {
		List<String> categories = new ArrayList<>();
		List<String> seriesData = new ArrayList<>();

		List<EChartsLineBarSeries> series = new ArrayList<>();

		EChartsLineBarOptions options = new EChartsLineBarOptions();
		options.setCategories(categories);
		options.setSeries(series);

		IDao<?> dao = getDaoFromEntityName(entity);
		if (dao == null) {
			logger.debug("ViewDao for entityName " + entity + " not exist");
			return options;
		}

		if (!columnValue.contains(",")) {
			String name = tooltipValue != null ? tooltipValue : "items";
			series.add(new EChartsLineBarSeries(name, type, seriesData));

			List<EChartsDataKeyValue> echartsDataList = getEChartsDataFromDao(dao, columnName, columnValue, filter);

			for (EChartsDataKeyValue dto : echartsDataList) {
				categories.add(dto.getKey());
				seriesData.add(dto.getValue());
			}
		} else {
			List<String> columnValues = Arrays.asList(columnValue.split(","));
			List<String> tooltipValues = tooltipValue != null && tooltipValue.contains(",")
					? Arrays.asList(tooltipValue.split(","))
					: null;

			if (tooltipValues != null && columnValues.size() == tooltipValues.size()) {
				for (String tValue : tooltipValues) {
					series.add(new EChartsLineBarSeries(tValue, type, new ArrayList<>()));
				}
			} else {
				for (String cValue : columnValues) {
					series.add(new EChartsLineBarSeries(cValue, type, new ArrayList<>()));
				}
			}

			List<EChartsDataKeyListValue> echartsDataList = getEChartsDataFromDao(dao, columnName, columnValues, filter,
					tooltipValues);

			for (EChartsDataKeyListValue dto : echartsDataList) {
				categories.add(dto.getKey());
				for (EChartsDataKeyValue keyValue : dto.getValues()) {
					for (EChartsLineBarSeries serie : series) {
						if (keyValue.getKey().equals(serie.getName())) {
							serie.getData().add(keyValue.getValue());
						}
					}
				}
			}
		}

		return options;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> getVuetifyDatatableValues(String entity, FilterGroup filter) {
		IDao<?> dao = getDaoFromEntityName(entity);
		if (dao == null) {
			logger.debug("ViewDao for entityName " + entity + " not exist");
			return Collections.emptyList();
		}

		try {
			return filter == null ? (List<Object>) dao.findAll()
					: (List<Object>) dao.findWhere(FilterBuilder.newFilter(filter));
		} catch (PuiDaoFindException e) {
			logger.error(e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	@Override
	public Long getVuetifyDatatableCount(String entity, FilterGroup filter) {
		IDao<?> dao = getDaoFromEntityName(entity);
		if (dao == null) {
			logger.debug("ViewDao for entityName " + entity + " not exist");
			return null;
		}

		try {
			return filter == null ? dao.count() : dao.count(FilterBuilder.newFilter(filter));
		} catch (PuiDaoCountException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	private IDao<?> getDaoFromEntityName(String entity) {
		Class<? extends IDao> daoClass = getDaoRegistry().getDaoFromEntityName(entity, true);
		return daoClass == null ? null : (IDao<?>) PuiApplicationContext.getInstance().getBean(daoClass);
	}

	@SuppressWarnings("unchecked")
	private List<EChartsDataKeyValue> getEChartsDataFromDao(IDao<?> dao, String columnName, String columnValue,
			FilterGroup filter) {
		List<EChartsDataKeyValue> list = new ArrayList<>();

		List<Object> elements;
		try {
			elements = (filter == null) ? (List<Object>) dao.findAll()
					: (List<Object>) dao.findWhere(FilterBuilder.newFilter(filter));
		} catch (PuiDaoFindException e) {
			logger.error(e.getMessage(), e);
			elements = Collections.emptyList();
		}

		Field columnNameField = DtoRegistry
				.getJavaFieldFromColumnName(getDaoRegistry().getDtoFromDao(dao.getDaoClass(), false), columnName);
		Field columnValueField = DtoRegistry
				.getJavaFieldFromColumnName(getDaoRegistry().getDtoFromDao(dao.getDaoClass(), false), columnValue);

		for (Object dto : elements) {
			Object objName;
			try {
				objName = columnNameField.get(dto);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				objName = null;
			}

			Object objValue = null;
			try {
				objValue = columnValueField.get(dto);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage(), e);
				continue;
			}

			String name = getName(objName);
			String value = objValue != null ? objValue.toString() : null;

			if (objValue instanceof BigDecimal) {
				value = ((BigDecimal) objValue).toPlainString();
			}

			list.add(new EChartsDataKeyValue(name, value));
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	private List<EChartsDataKeyListValue> getEChartsDataFromDao(IDao<?> dao, String columnName,
			List<String> columnValues, FilterGroup filter, List<String> tooltipValues) {
		List<EChartsDataKeyListValue> list = new ArrayList<>();

		List<Object> elements;
		try {
			elements = (filter == null) ? (List<Object>) dao.findAll()
					: (List<Object>) dao.findWhere(FilterBuilder.newFilter(filter));
		} catch (PuiDaoFindException e) {
			elements = Collections.emptyList();
		}

		Field columnNameField = DtoRegistry
				.getJavaFieldFromColumnName(getDaoRegistry().getDtoFromDao(dao.getDaoClass(), false), columnName);
		Map<String, Field> valueFieldsMap = new HashMap<>();
		Map<String, String> columnTooltipMap = new HashMap<>();

		for (int i = 0; i < columnValues.size(); i++) {
			String cv = columnValues.get(i);
			Field columnValueField = DtoRegistry
					.getJavaFieldFromColumnName(getDaoRegistry().getDtoFromDao(dao.getDaoClass(), false), cv);
			valueFieldsMap.put(cv, columnValueField);
			if (tooltipValues != null && tooltipValues.size() == columnValues.size()) {
				columnTooltipMap.put(cv, tooltipValues.get(i));
			}
		}

		for (Object dto : elements) {
			Object objName;
			try {
				objName = columnNameField.get(dto);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				objName = null;
			}

			String name = getName(objName);

			List<EChartsDataKeyValue> values = new ArrayList<>();
			for (Entry<String, Field> valueFieldsEntry : valueFieldsMap.entrySet()) {
				Object objValue = null;
				try {
					objValue = valueFieldsEntry.getValue().get(dto);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.error(e.getMessage(), e);
					continue;
				}

				String key = columnTooltipMap.get(valueFieldsEntry.getKey());
				if (key == null) {
					key = valueFieldsEntry.getKey();
				}

				if (objValue instanceof BigDecimal) {
					values.add(new EChartsDataKeyValue(key, ((BigDecimal) objValue).toPlainString()));
				} else {
					values.add(new EChartsDataKeyValue(key, objValue != null ? objValue.toString() : null));
				}
			}

			list.add(new EChartsDataKeyListValue(name, values));
		}

		return list;
	}

	private String getName(Object objName) {
		String name = "";

		if (objName instanceof String) {
			name = String.valueOf(objName);
		} else if (objName instanceof Instant) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault());
			name = formatter.format((Instant) objName);
		}

		return name;
	}

	private class EChartsDataKeyValue {

		private String key;
		private String value;

		public EChartsDataKeyValue(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

	}

	private class EChartsDataKeyListValue {

		private String key;
		private List<EChartsDataKeyValue> values;

		public EChartsDataKeyListValue(String key, List<EChartsDataKeyValue> values) {
			this.key = key;
			this.values = values;
		}

		public String getKey() {
			return key;
		}

		public List<EChartsDataKeyValue> getValues() {
			return values;
		}

	}

}