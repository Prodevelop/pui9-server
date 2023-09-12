package es.prodevelop.pui9.search;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonSyntaxException;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.filter.rules.BetweenRule;
import es.prodevelop.pui9.filter.rules.GreaterEqualsThanRule;
import es.prodevelop.pui9.filter.rules.GreaterThanRule;
import es.prodevelop.pui9.filter.rules.LowerEqualsThanRule;
import es.prodevelop.pui9.filter.rules.LowerThanRule;
import es.prodevelop.pui9.filter.rules.NotBetweenRule;
import es.prodevelop.pui9.list.adapters.IListAdapter;
import es.prodevelop.pui9.list.adapters.ListAdapterRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.utils.IPuiObject;
import es.prodevelop.pui9.utils.PuiDateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

/**
 * This object is used with the Searching request operation to configure the
 * operation. The main attributes to set are:
 * <ul>
 * <li><b>modelName</b>: The name of the model we want to search</li>
 * <li><b>page</b>: The current page to start the search (starting with 1)</li>
 * <li><b>rows</b>: The number of rows to return for each page</li>
 * <li><b>queryLang</b>: The language to use</li>
 * <li><b>queryText</b>: The text to search against the visible fields
 * {@link #queryFields}</li>
 * <li><b>queryFields</b>: the list of fields where the {@link #queryText} will
 * be searched</li>
 * <li><b>queryFieldText</b>: if the search text is per column (a hashmap where
 * the Key is the column name and the Value is the text to search for each
 * column)</li>
 * <li><b>queryFlexible</b>: for Elastic Search, indicating that the searching
 * text should be treated with flexibility, allowing spelling errors</li>
 * <li><b>filters</b>: the Filter of the search</li>
 * <li><b>orderColumn</b>: the column used to order the results</li>
 * <li><b>orderDirection</b>: the direction of the order</li>
 * </ul>
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class SearchRequest implements IPuiObject {

	private static final long serialVersionUID = 1L;

	public static final Integer NUM_MAX_ROWS = 10000;
	public static final Integer DEFAULT_PAGE = 1;
	public static final Integer DEFAULT_ROWS = 20;

	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "")
	private String model = "";
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "1")
	private Integer page = DEFAULT_PAGE;
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "20")
	private Integer rows = DEFAULT_ROWS;
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "es")
	private String queryLang = "";
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "es")
	private String queryText = "";
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "[]")
	private List<String> queryFields = new ArrayList<>();
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "[]")
	private Map<String, String> queryFieldText = new LinkedHashMap<>();
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "false")
	private Boolean queryFlexible = false;
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "{}")
	private FilterGroup filter = null;
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "[]")
	private List<Order> order;
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "[]")
	private List<String> columns = new ArrayList<>();
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "[]")
	private List<String> sumColumns = new ArrayList<>();
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "false")
	private Boolean distinctValues = false;
	@Schema(requiredMode = RequiredMode.NOT_REQUIRED, example = "")
	private List<String> distinctOnColumns = null;

	@Schema(hidden = true)
	private transient FilterGroup dbFilters = IListAdapter.EMPTY_FILTER;
	@Schema(hidden = true)
	private transient Class<? extends IDto> dtoClass;
	@Schema(hidden = true)
	private transient Boolean performCount = true;
	@Schema(hidden = true)
	private transient Boolean fromClient = false;
	@Schema(hidden = true)
	private transient ZoneId zoneId = PuiDateUtil.utcZone;

	/**
	 * Get the model this request is going to be performed
	 * 
	 * @return The name of the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Set the name of the model this request is going to be performed
	 * 
	 * @param model The name of the model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Get the page number of the pagination. Starting with {@link #DEFAULT_PAGE}
	 * 
	 * @return The page number of the pagination
	 */
	public Integer getPage() {
		if (page != null) {
			if (page > 0) {
				return page;
			} else {
				return DEFAULT_PAGE;
			}
		} else {
			return DEFAULT_PAGE;
		}
	}

	/**
	 * Set the page of the pagination
	 * 
	 * @param page The page of the pagination
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * Get the number of rows of the pagination. Default value is
	 * {@link #DEFAULT_PAGE} and maximum value is {@link #NUM_MAX_ROWS}
	 * 
	 * @return The number of rows of the pagination
	 */
	public Integer getRows() {
		if (rows != null) {
			if (rows > 0) {
				return rows;
			} else {
				return DEFAULT_ROWS;
			}
		} else {
			return NUM_MAX_ROWS;
		}
	}

	/**
	 * Set the number of rows of the pagination
	 * 
	 * @param rows The number of rows of the pagination
	 */
	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * Get the language of the query (for translated models)
	 * 
	 * @return The language
	 */
	public String getQueryLang() {
		return queryLang;
	}

	/**
	 * Set the language of the query
	 * 
	 * @param queryLang The language
	 */
	public void setQueryLang(String queryLang) {
		this.queryLang = queryLang;
	}

	/**
	 * Get the query text for search
	 * 
	 * @return The searched text
	 */
	public String getQueryText() {
		return queryText;
	}

	/**
	 * Set the text to search
	 * 
	 * @param queryText The searched text
	 */
	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	/**
	 * Get the field names where the query will apply
	 * 
	 * @return
	 */
	public List<String> getQueryFields() {
		return queryFields != null ? queryFields : Collections.emptyList();
	}

	public void addQueryField(String queryField) {
		if (this.queryFields == null) {
			this.queryFields = new ArrayList<>();
		}
		this.queryFields.add(queryField);
	}

	public void setQueryFields(List<String> queryFields) {
		this.queryFields = queryFields;
	}

	public Map<String, String> getQueryFieldText() {
		return queryFieldText != null ? queryFieldText : Collections.emptyMap();
	}

	public void addQueryFieldText(String field, String text) {
		if (this.queryFieldText == null) {
			this.queryFieldText = new LinkedHashMap<>();
		}
		this.queryFieldText.put(field, text);
	}

	public void setQueryFieldText(Map<String, String> queryFieldText) {
		this.queryFieldText = queryFieldText;
	}

	public Boolean isQueryFlexible() {
		return queryFlexible;
	}

	public void setQueryFlexible(Boolean queryFlexible) {
		this.queryFlexible = queryFlexible;
	}

	public void setFilter(FilterGroup filter) {
		this.filter = filter;
	}

	public List<Order> getOrder() {
		if (order == null) {
			order = Collections.emptyList();
		}
		return order;
	}

	public void addOrder(Order order) {
		if (this.order == null) {
			this.order = new ArrayList<>();
		}
		this.order.add(order);
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

	public List<String> getColumns() {
		if (columns == null) {
			columns = Collections.emptyList();
		}
		return columns;
	}

	public void addColumn(String column) {
		if (columns == null) {
			columns = new ArrayList<>();
		}
		columns.add(column);
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public List<String> getSumColumns() {
		if (sumColumns == null) {
			sumColumns = Collections.emptyList();
		}
		return sumColumns;
	}

	public void addSumColumn(String sumColumn) {
		if (sumColumns == null) {
			sumColumns = new ArrayList<>();
		}
		sumColumns.add(sumColumn);
	}

	public void setSumColumns(List<String> sumColumns) {
		this.sumColumns = sumColumns;
	}

	@Schema(hidden = true)
	public void setDbFilters(FilterGroup dbFilters) {
		this.dbFilters = dbFilters;
	}

	public Boolean isDistinctValues() {
		return distinctValues;
	}

	public void setDistinctValues(Boolean distinctValues) {
		this.distinctValues = distinctValues;
	}

	public List<String> getDistinctOnColumns() {
		return distinctOnColumns;
	}

	public void setDistinctOnColumns(String... distinctOnColumns) {
		this.distinctOnColumns = Arrays.asList(distinctOnColumns);
	}

	public Boolean isFromClient() {
		return fromClient;
	}

	public void setFromClient(Boolean fromClient) {
		this.fromClient = fromClient;
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public Class<? extends IDto> getDtoClass() {
		return dtoClass;
	}

	public void setDtoClass(Class<? extends IDto> dtoClass) {
		this.dtoClass = dtoClass;
	}

	public Boolean isPerformCount() {
		return performCount;
	}

	public void setPerformCount(Boolean performCount) {
		this.performCount = performCount;
	}

	/**
	 * Build the {@link FilterBuilder} object from the Filter of the search and the
	 * filters in the database of the model
	 * 
	 * @return The filter as FilterBuilder representation
	 */
	public FilterBuilder buildSearchFilter(Class<? extends IDto> dtoClass) {
		purgueNullRules(this.filter);

		// filters from request
		modifyDateRules(dtoClass, this.filter);

		// filters from dabatase: substitute every filter parameter with the
		// correct value
		FilterGroup dbFilter = null;
		if (this.dbFilters != null) {
			String dbFiltersJson = "";
			if (this.dbFilters.equals(IListAdapter.EMPTY_FILTER)) {
				dbFiltersJson = IListAdapter.SEARCH_PARAMETER;
			} else {
				dbFiltersJson = this.dbFilters.toJson();
			}

			if (dbFiltersJson.contains(IListAdapter.SEARCH_PARAMETER)) {
				IListAdapter<? extends IDto> listAdapter = ListAdapterRegistry.getSingleton().getAdapter(dtoClass);
				String fixedParams = listAdapter != null ? listAdapter.getFixedFilterParameters(this) : null;
				if (!StringUtils.isEmpty(fixedParams)) {
					for (String split : fixedParams.split("\\" + IListAdapter.SEPARATOR)) {
						dbFiltersJson = dbFiltersJson.replaceFirst("\\" + IListAdapter.SEARCH_PARAMETER, split);
					}
				}
			}

			if (!dbFiltersJson.equals(IListAdapter.SEARCH_PARAMETER)) {
				try {
					dbFilter = FilterGroup.fromJson(dbFiltersJson);
					purgueBadDatabaseRules(dbFilter);
					purgueNullRules(dbFilter);
					setDbFilters(dbFilter);
				} catch (JsonSyntaxException e) {
					// do nothing
				}
			}
		}

		// combine filters from request and from database
		FilterGroup filters;
		if (this.filter != null && dbFilter == null) {
			filters = this.filter;
		} else if (dbFilter != null) {
			if (this.filter == null) {
				filters = dbFilter;
			} else {
				filters = FilterGroup.andGroup();
				filters.getGroups().add(this.filter);
				filters.getGroups().add(dbFilter);
			}
		} else {
			filters = null;
		}

		return filters != null ? FilterBuilder.newFilter(filters) : FilterBuilder.newAndFilter();
	}

	/**
	 * Build the {@link OrderBuilder} object from the order and direction of the
	 * request
	 * 
	 * @return The order as {@link OrderBuilder} representation
	 */
	public OrderBuilder createOrderForSearch() {
		OrderBuilder orderBuilder = OrderBuilder.newOrder();

		if (!getOrder().isEmpty()) {
			for (Order ord : getOrder()) {
				if (dtoClass != null && !DtoRegistry.getAllColumnNames(dtoClass).contains(ord.getColumn())) {
					ord.setColumn(DtoRegistry.getColumnNameFromFieldName(dtoClass, ord.getColumn()));
				}
				orderBuilder.addOrder(ord);
			}
		}

		return orderBuilder;
	}

	/**
	 * Iterate the given filter to remove the bad defined rules: those that contains
	 * the character {@link IListAdapter#SEARCH_PARAMETER} after processing the
	 * associated Adapter
	 */
	private void purgueBadDatabaseRules(FilterGroup filter) {
		if (filter == null) {
			return;
		}

		filter.getRules().removeIf(
				rule -> rule.getData() != null && rule.getData().toString().contains(IListAdapter.SEARCH_PARAMETER));
		filter.getGroups().forEach(this::purgueBadDatabaseRules);
	}

	private void purgueNullRules(FilterGroup filter) {
		if (filter == null) {
			return;
		}

		filter.getRules().removeIf(Objects::isNull);
		filter.getGroups().forEach(this::purgueNullRules);
	}

	private void modifyDateRules(Class<? extends IDto> dtoClass, FilterGroup filter) {
		if (filter == null) {
			return;
		}

		for (int i = 0; i < filter.getRules().size(); i++) {
			AbstractFilterRule nextRule = filter.getRules().get(i);
			if (!isDate(dtoClass, nextRule.getField())) {
				continue;
			}

			AbstractFilterRule newRule = modifyDateFilters(nextRule);
			if (!Objects.equals(nextRule, newRule)) {
				filter.getRules().set(i, newRule);
			}
		}
		filter.getGroups().forEach(group -> modifyDateRules(dtoClass, group));
	}

	private AbstractFilterRule modifyDateFilters(AbstractFilterRule rule) {
		if (rule.getOp() == null || !(rule.getData() instanceof String)) {
			return rule;
		}

		String value = rule.getData().toString().trim();
		if (!PuiDateUtil.stringHasHours(value)) {
			value += " 00";
		}
		if (!PuiDateUtil.stringHasMinutes(value)) {
			value += ":00";
		}
		if (!PuiDateUtil.stringHasSeconds(value)) {
			value += ":00";
		}

		Integer days = null;
		try {
			days = Integer.parseInt(rule.getData().toString());
		} catch (Exception e) {
			days = null;
		}

		ZonedDateTime userZonedDateTime;
		if (days == null) {
			userZonedDateTime = PuiDateUtil.stringToZonedDateTime(value, zoneId);
		} else {
			userZonedDateTime = ZonedDateTime.now(zoneId).plus(days, ChronoUnit.DAYS);
		}

		Instant valueStartInstant = userZonedDateTime.withHour(0).withMinute(0).withSecond(0)
				.with(ChronoField.MILLI_OF_SECOND, 0).toInstant();
		Instant valueEndInstant = userZonedDateTime.withHour(23).withMinute(59).withSecond(59)
				.with(ChronoField.MILLI_OF_SECOND, 999).toInstant();

		AbstractFilterRule newRule = rule;
		switch (rule.getOp()) {
		case eq:
		case eqt:
			newRule = BetweenRule.of(rule.getField(), valueStartInstant, valueEndInstant);
			break;
		case ne:
		case net:
			newRule = NotBetweenRule.of(rule.getField(), valueStartInstant, valueEndInstant);
			break;
		case ltt:
			newRule = LowerThanRule.of(rule.getField(), valueStartInstant);
			break;
		case let:
			newRule = LowerEqualsThanRule.of(rule.getField(), valueEndInstant);
			break;
		case le:
		case gt:
			rule.withData(valueEndInstant);
			break;
		case gtt:
			newRule = GreaterThanRule.of(rule.getField(), valueEndInstant);
			break;
		case lt:
		case ge:
			rule.withData(valueStartInstant);
			break;
		case get:
			newRule = GreaterEqualsThanRule.of(rule.getField(), valueStartInstant);
			break;
		default:
			break;
		}

		return newRule;
	}

	private boolean isDate(Class<? extends IDto> dtoClass, String field) {
		if (DtoRegistry.getFieldNameFromColumnName(dtoClass, field) != null) {
			field = DtoRegistry.getFieldNameFromColumnName(dtoClass, field);
		}
		return DtoRegistry.getDateTimeFields(dtoClass).contains(field);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("model: " + (!ObjectUtils.isEmpty(model) ? model : ""));
		sb.append(", page: " + (page != null ? page : ""));
		sb.append(", rows: " + (rows != null ? rows : ""));
		sb.append(", queryText: " + (!ObjectUtils.isEmpty(queryText) ? queryText : ""));
		sb.append(", queryFields: " + (!ObjectUtils.isEmpty(queryFields) ? String.join(",", queryFields) : ""));
		return sb.toString();
	}

}
