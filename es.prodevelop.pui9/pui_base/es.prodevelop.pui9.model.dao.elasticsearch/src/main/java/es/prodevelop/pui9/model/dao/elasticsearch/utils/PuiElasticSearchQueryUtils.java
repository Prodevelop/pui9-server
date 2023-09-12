package es.prodevelop.pui9.model.dao.elasticsearch.utils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.SumAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldAndFormat;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.filter.rules.AbstractBetweenRule;
import es.prodevelop.pui9.filter.rules.AbstractInRule;
import es.prodevelop.pui9.filter.rules.AbstractNullRule;
import es.prodevelop.pui9.filter.rules.BeginWithRule;
import es.prodevelop.pui9.filter.rules.BetweenRule;
import es.prodevelop.pui9.filter.rules.BoundingBoxRule;
import es.prodevelop.pui9.filter.rules.ContainsRule;
import es.prodevelop.pui9.filter.rules.EndsWithRule;
import es.prodevelop.pui9.filter.rules.EqualsRule;
import es.prodevelop.pui9.filter.rules.GreaterEqualsThanRule;
import es.prodevelop.pui9.filter.rules.GreaterThanRule;
import es.prodevelop.pui9.filter.rules.IsNotNullRule;
import es.prodevelop.pui9.filter.rules.IsNullRule;
import es.prodevelop.pui9.filter.rules.LowerEqualsThanRule;
import es.prodevelop.pui9.filter.rules.LowerThanRule;
import es.prodevelop.pui9.filter.rules.NotBeginWithRule;
import es.prodevelop.pui9.filter.rules.NotBetweenRule;
import es.prodevelop.pui9.filter.rules.NotContainsRule;
import es.prodevelop.pui9.filter.rules.NotEndsWithRule;
import es.prodevelop.pui9.filter.rules.NotEqualsRule;
import es.prodevelop.pui9.filter.rules.NotInRule;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.order.ElasticSearchDistanceOrder;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.order.OrderDirection;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.utils.PuiDateUtil;
import es.prodevelop.pui9.utils.PuiLanguage;

@Component
public class PuiElasticSearchQueryUtils {

	private static final String KEYWORD_LITERAL = "keyword";
	private static final String LEFT_COL = "leftCol";
	private static final String RIGHT_COL = "rightCol";
	private static final String SCRIPT_CODE = "doc[params.leftCol].value {0} doc[params.rightCol].value";
	private static final String SCRIPT_LANG = "painless";

	@Autowired
	private PuiElasticSearchIndexUtils indexUtils;

	private PuiElasticSearchQueryUtils() {
		// do nothing
	}

	@SuppressWarnings("unchecked")
	public <T extends IDto> co.elastic.clients.elasticsearch.core.SearchRequest buildQuery(SearchRequest req) {
		Class<T> dtoClass = (Class<T>) req.getDtoClass();

		if (req.isFromClient() && PuiUserSession.getCurrentSession() != null) {
			req.setZoneId(PuiUserSession.getCurrentSession().getZoneId());
		}
		FilterBuilder filterBuilder = req.buildSearchFilter(dtoClass);
		OrderBuilder orderBuilder = req.createOrderForSearch();

		for (Order order : orderBuilder.getOrders()) {
			String fieldName = DtoRegistry.getFieldNameFromColumnName(dtoClass, order.getColumn());
			if (fieldName != null) {
				order.setColumn(fieldName);
			}
		}

		PuiLanguage language = !ObjectUtils.isEmpty(req.getQueryLang()) ? new PuiLanguage(req.getQueryLang()) : null;
		String index = indexUtils.getIndexForLanguage(dtoClass, language);

		// The Search Builder

		co.elastic.clients.elasticsearch.core.SearchRequest.Builder requestBuilder = new co.elastic.clients.elasticsearch.core.SearchRequest.Builder();
		requestBuilder.index(index);

		if (!ObjectUtils.isEmpty(req.getColumns())) {
			List<String> fields = req.getColumns().stream()
					.map(c -> DtoRegistry.getFieldNameFromColumnName(dtoClass, c)).collect(Collectors.toList());
			List<FieldAndFormat> list = new ArrayList<>();
			fields.forEach(c -> list.add(FieldAndFormat.of(ff -> ff.field(c))));
			requestBuilder.fields(list);
			requestBuilder.source(sc -> sc.filter(sf -> sf.includes(fields)));
		}

		// the filter
		ObjectBuilder<?> filtersBuilder = processFilters(dtoClass, filterBuilder.asFilterGroup());

		// the searched text
		ObjectBuilder<?> quickSearchBuilder = null;
		if (!ObjectUtils.isEmpty(req.getQueryFields())) {
			quickSearchBuilder = processSearchText(dtoClass, req.getQueryFields(), req.getQueryText(), req.getZoneId());
		} else if (!ObjectUtils.isEmpty(req.getQueryFieldText())) {
			quickSearchBuilder = processSearchText(dtoClass, req.getQueryFieldText(), req.getZoneId());
		}

		// the resulting filter
		ObjectBuilder<?> ob;
		if (filtersBuilder == null && quickSearchBuilder == null) {
			ob = null;
		} else if (filtersBuilder != null && quickSearchBuilder == null) {
			ob = filtersBuilder;
		} else if (filtersBuilder == null && quickSearchBuilder != null) {
			ob = quickSearchBuilder;
		} else {
			ob = QueryBuilders.bool().must(((QueryVariant) quickSearchBuilder.build())._toQuery())
					.must(((QueryVariant) filtersBuilder.build())._toQuery());
		}
		if (ob != null) {
			requestBuilder.query(((QueryVariant) ob.build())._toQuery());
		}

		// the order
		List<SortOptions> orders = parseQueryOrder(dtoClass, orderBuilder);
		if (!ObjectUtils.isEmpty(orders)) {
			requestBuilder.sort(orders);
		}

		// distinct values
		if (!ObjectUtils.isEmpty(req.getDistinctOnColumns())) {
			List<String> distinctColumns = req.getDistinctOnColumns();
			String distinctColumn = distinctColumns.get(0);
			if (isTextTerm(dtoClass, distinctColumn)) {
				distinctColumn += "." + KEYWORD_LITERAL;
			}
			final String dc = distinctColumn;
			requestBuilder.collapse(fc -> fc.field(dc).innerHits(ih -> ih.name("distinct_" + dc).size(0)));
		}

		if (req.isDistinctValues()) {
			// TODO
		}

		// the pagination
		Integer page = req.getPage() - 1;
		Integer size = req.getRows();

		Integer from = page * size;
		requestBuilder.from(from);
		requestBuilder.size(size);

		for (String sumColumn : req.getSumColumns()) {
			if (!DtoRegistry.getNumericFields(dtoClass).contains(sumColumn)
					&& !DtoRegistry.getFloatingFields(dtoClass).contains(sumColumn)) {
				continue;
			}

			requestBuilder.aggregations(sumColumn,
					Aggregation.of(a -> a.sum(SumAggregation.of(sa -> sa.field(sumColumn)))));
		}

		return requestBuilder.build();
	}

	/**
	 * Check if the given field is considered as Term for the given DTO class
	 * 
	 * @param dtoClass  The DTO class of the field
	 * @param fieldName The field to check
	 * @return true if it's a text term; false if not
	 */
	private boolean isTextTerm(Class<? extends IDto> dtoClass, String fieldName) {
		Integer length = DtoRegistry.getFieldMaxLength(dtoClass, fieldName);
		return length == null || (length > 0 && length < (32 * 1024));
	}

	/**
	 * Create a FieldSortBuilder with the given order
	 * 
	 * @param dtoClass     The DTO class type for the search
	 * @param orderBuilder The Order of the request
	 * @return The FieldSortBuilder for ElasticSearch
	 */
	private List<SortOptions> parseQueryOrder(Class<? extends IDto> dtoClass, OrderBuilder orderBuilder) {
		if (orderBuilder == null) {
			return Collections.emptyList();
		}

		List<SortOptions> orders = new ArrayList<>();
		for (Order order : orderBuilder.getOrders()) {
			SortOptions sortOption;
			SortOrder sortOrder = order.getDirection().equals(OrderDirection.asc) ? SortOrder.Asc : SortOrder.Desc;

			if (order instanceof ElasticSearchDistanceOrder) {
				ElasticSearchDistanceOrder esdo = (ElasticSearchDistanceOrder) order;
				sortOption = SortOptions.of(so -> so.geoDistance(gd -> gd.field(order.getColumn())
						.location(gl -> gl.latlon(llg -> llg.lon(esdo.getX()).lat(esdo.getY()))).order(sortOrder)));
			} else {
				String column = order.getColumn();
				if (isTextTerm(dtoClass, order.getColumn())) {
					column += "." + KEYWORD_LITERAL;
				}

				final String col = column;
				FieldSort fieldSort = FieldSort.of(fs -> fs.field(col).order(sortOrder));
				sortOption = SortOptions.of(so -> so.field(fieldSort));
			}
			orders.add(sortOption);
		}

		return orders;
	}

	private ObjectBuilder<?> processSearchText(Class<? extends IDto> dtoClass, List<String> fields, String text,
			ZoneId zoneId) {
		Map<String, String> fieldTextMap = new LinkedHashMap<>();
		fields.forEach(field -> fieldTextMap.put(field, text));
		return processSearchText(dtoClass, fieldTextMap, zoneId);
	}

	private ObjectBuilder<?> processSearchText(Class<? extends IDto> dtoClass, Map<String, String> fieldTextMap,
			ZoneId zoneId) {
		FilterGroup group = FilterGroup.orGroup();
		fieldTextMap.entrySet().stream()
				.filter(entry -> !ObjectUtils.isEmpty(entry.getKey()) && !ObjectUtils.isEmpty(entry.getValue()))
				.forEach(entry -> {
					String field = entry.getKey();
					String val = entry.getValue();

					val = val.replace("'", "''").replace("%", "");

					AbstractFilterRule rule = ContainsRule.of(field, val);
					if (rule.isDate(dtoClass)) {
						ZonedDateTime userZonedDateTime = PuiDateUtil.stringToZonedDateTime(val, zoneId);
						if (userZonedDateTime != null) {
							Instant valueStartInstant = userZonedDateTime.withHour(0).withMinute(0).withSecond(0)
									.with(ChronoField.MILLI_OF_SECOND, 0).toInstant();
							Instant valueEndInstant = userZonedDateTime.withHour(23).withMinute(59).withSecond(59)
									.with(ChronoField.MILLI_OF_SECOND, 999).toInstant();
							rule = BetweenRule.of(field, valueStartInstant, valueEndInstant);
						} else {
							rule = null;
						}
					} else if (rule.isBoolean(dtoClass) && (val.equalsIgnoreCase(Boolean.TRUE.toString())
							|| val.equalsIgnoreCase(Boolean.FALSE.toString()))) {
						rule = EqualsRule.of(field, Boolean.valueOf(val));
					}

					if (rule != null) {
						group.addRule(rule);
					}
				});

		return processFilters(dtoClass, group);
	}

	/**
	 * Create a QueryBuilder for the filters of the request
	 * 
	 * @param dtoClass The DTO class type for the search
	 * @param filters  The filters of the request
	 * @return A BoolQueryBuilder of ElasticSearch that represents the user filter
	 */
	public ObjectBuilder<?> processFilters(Class<? extends IDto> dtoClass, FilterGroup filters) {
		if (filters == null) {
			return null;
		}

		List<ObjectBuilder<?>> filterQueryList = new ArrayList<>();

		for (AbstractFilterRule rule : filters.getRules()) {
			ObjectBuilder<?> ob = processRule(dtoClass, rule);
			if (ob != null) {
				filterQueryList.add(ob);
			}
		}

		for (FilterGroup filter : filters.getGroups()) {
			ObjectBuilder<?> ob = processFilters(dtoClass, filter);
			if (ob != null) {
				filterQueryList.add(ob);
			}
		}

		BoolQuery.Builder bqb = QueryBuilders.bool();
		if (filterQueryList.isEmpty()) {
			bqb = null;
		} else if (filterQueryList.size() == 1) {
			bqb.must(((QueryVariant) filterQueryList.get(0).build())._toQuery());
		} else {
			bqb = QueryBuilders.bool();
			for (ObjectBuilder<?> ob : filterQueryList) {
				switch (filters.getGroupOp()) {
				case and:
					bqb.must(((QueryVariant) ob.build())._toQuery());
					break;
				case or:
					bqb.should(((QueryVariant) ob.build())._toQuery());
					bqb.minimumShouldMatch("1");
					break;
				}
			}
		}

		return bqb;
	}

	/**
	 * Create a QueryBuilder for the given Rule of a Filter
	 * 
	 * @param dtoClass The DTO class type for the search
	 * @param rule     The Rule of the Filter
	 * @return A QueryBuilder of ElasticSearch that represents the Rule
	 */
	private ObjectBuilder<?> processRule(Class<? extends IDto> dtoClass, AbstractFilterRule rule) {
		if (rule == null) {
			return null;
		}

		String field = rule.getField();
		if (field.equals(IDto.LANG_COLUMN_NAME)) {
			return null;
		}

		// ensure to select the view field name
		if (!DtoRegistry.getAllFields(dtoClass).contains(field)) {
			// if the fields list doesn't contain the given field, check if it was a column
			// and get the corresponding field name
			field = DtoRegistry.getFieldNameFromColumnName(dtoClass, field);
			if (!DtoRegistry.getAllFields(dtoClass).contains(field)) {
				// if it still doesn't exist, return null
				return null;
			}
		}

		String termValue = valueAsTermString(rule.getData());
		Instant instantValue = rule.valueAsInstant();
		Number numberValue = rule.valueAsNumber();
		Boolean boolValue = rule.valueAsBoolean();

		if (ObjectUtils.isEmpty(termValue) && instantValue == null && numberValue == null && boolValue == null) {
			if (rule instanceof EqualsRule) {
				rule = IsNullRule.of(rule.getField());
			} else if (rule instanceof NotEqualsRule) {
				rule = IsNotNullRule.of(rule.getField());
			}
		}

		ObjectBuilder<?> ob = null;

		if (rule instanceof EqualsRule || rule instanceof NotEqualsRule) {
			if (rule.isDataIsColumn()) {
				Map<String, JsonData> map = new LinkedHashMap<>();
				map.put(LEFT_COL, JsonData.of(rule.getField() + "." + KEYWORD_LITERAL));
				map.put(RIGHT_COL, JsonData.of(rule.getData() + "." + KEYWORD_LITERAL));
				String operator = "";
				if (rule instanceof EqualsRule) {
					operator = "==";
				} else {
					operator = "!=";
				}
				String script = MessageFormat.format(SCRIPT_CODE, operator);
				ob = QueryBuilders.script()
						.script(s -> s.inline(is -> is.lang(SCRIPT_LANG).source(script).params(map)));
			} else {
				if (rule.isString(dtoClass) && !ObjectUtils.isEmpty(termValue)) {
					if (isTextTerm(dtoClass, field)) {
						ob = QueryBuilders.regexp().field(field + "." + KEYWORD_LITERAL).value(termValue);
					} else {
						// never search by this kind of fields because it's a huge field
						ob = QueryBuilders.simpleQueryString().query("fake_text_never_searched");
					}
				} else if (rule.isNumber(dtoClass) && numberValue != null) {
					ob = QueryBuilders.term().field(field).value(numberValue.longValue());
				} else if (rule.isFloatingNumber(dtoClass) && numberValue != null) {
					ob = QueryBuilders.term().field(field).value(numberValue.doubleValue());
				} else if (rule.isDate(dtoClass) && instantValue != null) {
					ob = QueryBuilders.range().gte(JsonData.of(instantValue)).lte(JsonData.of(instantValue));
				} else if (rule.isBoolean(dtoClass) && boolValue != null) {
					ob = QueryBuilders.term().field(field).value(boolValue);
				}
				if (ob != null && rule instanceof NotEqualsRule) {
					ob = QueryBuilders.bool().mustNot(((QueryVariant) ob.build())._toQuery());
				}
			}
		} else if (rule instanceof BeginWithRule || rule instanceof NotBeginWithRule) {
			if (rule.isString(dtoClass) && !ObjectUtils.isEmpty(termValue)) {
				if (isTextTerm(dtoClass, field)) {
					ob = QueryBuilders.regexp().field(field + "." + KEYWORD_LITERAL).value(termValue + ".*");
				} else {
					ob = QueryBuilders.regexp().field(field).value(termValue + ".*");
				}
				if (ob != null && rule instanceof NotBeginWithRule) {
					ob = QueryBuilders.bool().mustNot(((QueryVariant) ob.build())._toQuery());
				}
			}
		} else if (rule instanceof EndsWithRule || rule instanceof NotEndsWithRule) {
			if (rule.isString(dtoClass) && !ObjectUtils.isEmpty(termValue)) {
				if (isTextTerm(dtoClass, field)) {
					ob = QueryBuilders.regexp().field(field + "." + KEYWORD_LITERAL).value(".*" + termValue);
				} else {
					ob = QueryBuilders.regexp().field(field).value(".*" + termValue);
				}
				if (ob != null && rule instanceof NotEndsWithRule) {
					ob = QueryBuilders.bool().mustNot(((QueryVariant) ob.build())._toQuery());
				}
			}
		} else if (rule instanceof ContainsRule || rule instanceof NotContainsRule) {
			if (rule.isString(dtoClass) && !ObjectUtils.isEmpty(termValue)) {
				if (isTextTerm(dtoClass, field)) {
					ob = QueryBuilders.regexp().field(field + "." + KEYWORD_LITERAL).value(".*" + termValue + ".*");
				} else {
					ob = QueryBuilders.regexp().field(field).value(".*" + termValue + ".*");
				}
				if (ob != null && rule instanceof NotContainsRule) {
					ob = QueryBuilders.bool().mustNot(((QueryVariant) ob.build())._toQuery());
				}
			} else if (rule.isNumber(dtoClass) && numberValue != null) {
				ob = QueryBuilders.term().field(field).value(numberValue.longValue());
				if (ob != null && rule instanceof NotContainsRule) {
					ob = QueryBuilders.bool().mustNot(((QueryVariant) ob.build())._toQuery());
				}
			} else if (rule.isFloatingNumber(dtoClass) && numberValue != null) {
				ob = QueryBuilders.term().field(field).value(numberValue.doubleValue());
				if (ob != null && rule instanceof NotContainsRule) {
					ob = QueryBuilders.bool().mustNot(((QueryVariant) ob.build())._toQuery());
				}
			}
		} else if (rule instanceof LowerThanRule || rule instanceof LowerEqualsThanRule
				|| rule instanceof GreaterThanRule || rule instanceof GreaterEqualsThanRule) {
			if (rule.isDataIsColumn()) {
				Map<String, JsonData> map = new LinkedHashMap<>();
				map.put(LEFT_COL, JsonData.of(rule.getField()));
				map.put(RIGHT_COL, JsonData.of(rule.getData()));
				String operator = "";
				if (rule instanceof LowerThanRule) {
					operator = "<";
				} else if (rule instanceof LowerEqualsThanRule) {
					operator = "<=";
				} else if (rule instanceof GreaterThanRule) {
					operator = ">";
				} else if (rule instanceof GreaterEqualsThanRule) {
					operator = ">=";
				}
				String script = MessageFormat.format(SCRIPT_CODE, operator);
				ob = QueryBuilders.script()
						.script(s -> s.inline(is -> is.lang(SCRIPT_LANG).source(script).params(map)));
			} else {
				if (rule instanceof LowerThanRule) {
					if (rule.isNumber(dtoClass) && numberValue != null) {
						ob = QueryBuilders.range().field(field).lt(JsonData.of(numberValue));
					} else if (rule.isDate(dtoClass) && instantValue != null) {
						ob = QueryBuilders.range().field(field).lt(JsonData.of(instantValue));
					}
				} else if (rule instanceof LowerEqualsThanRule) {
					if (rule.isNumber(dtoClass) && numberValue != null) {
						ob = QueryBuilders.range().field(field).lte(JsonData.of(numberValue));
					} else if (rule.isDate(dtoClass) && instantValue != null) {
						ob = QueryBuilders.range().field(field).lte(JsonData.of(instantValue));
					}
				} else if (rule instanceof GreaterThanRule) {
					if (rule.isNumber(dtoClass) && numberValue != null) {
						ob = QueryBuilders.range().field(field).gt(JsonData.of(numberValue));
					} else if (rule.isDate(dtoClass) && instantValue != null) {
						ob = QueryBuilders.range().field(field).gt(JsonData.of(instantValue));
					}
				} else if (rule instanceof GreaterEqualsThanRule) {
					if (rule.isNumber(dtoClass) && numberValue != null) {
						ob = QueryBuilders.range().field(field).gte(JsonData.of(numberValue));
					} else if (rule.isDate(dtoClass) && instantValue != null) {
						ob = QueryBuilders.range().field(field).gte(JsonData.of(instantValue));
					}
				}
			}
		} else if (rule instanceof AbstractBetweenRule) {
			AbstractBetweenRule r = (AbstractBetweenRule) rule;
			if (!r.isDataIsColumn()) {
				Object lower = null;
				Object upper = null;
				if (rule.isNumber(dtoClass) || rule.isDate(dtoClass)) {
					lower = r.getLower();
					upper = r.getUpper();
				}
				if (lower != null && upper != null) {
					ob = QueryBuilders.range().field(field).gte(JsonData.of(lower)).lte(JsonData.of(upper));
				}
				if (ob != null && rule instanceof NotBetweenRule) {
					ob = QueryBuilders.bool().mustNot(((QueryVariant) ob.build())._toQuery());
				}
			}
		} else if (rule instanceof AbstractInRule) {
			AbstractInRule r = (AbstractInRule) rule;
			if (isTextTerm(dtoClass, field)) {
				field += "." + KEYWORD_LITERAL;
			}
			ob = QueryBuilders.terms().field(field).terms(tqf -> tqf.value(r.getCollection().stream().map(v -> {
				if (v instanceof String) {
					return FieldValue.of((String) v);
				} else if (v instanceof Integer) {
					return FieldValue.of((Integer) v);
				} else if (v instanceof BigDecimal) {
					return FieldValue.of(((BigDecimal) v).doubleValue());
				} else {
					return null;
				}
			}).collect(Collectors.toList())));
			if (ob != null && rule instanceof NotInRule) {
				ob = QueryBuilders.bool().mustNot(((QueryVariant) ob.build())._toQuery());
			}
		} else if (rule instanceof AbstractNullRule) {
			ob = QueryBuilders.exists().field(field);
			if (rule instanceof IsNullRule) {
				ob = QueryBuilders.bool().mustNot(((QueryVariant) ob.build())._toQuery());
			}
		} else if (rule instanceof BoundingBoxRule) {
			BoundingBoxRule bbr = (BoundingBoxRule) rule;
			ob = QueryBuilders.geoBoundingBox().field(bbr.getField())
					.boundingBox(gb -> gb.trbl(
							trbl -> trbl.bottomLeft(bl -> bl.latlon(ll -> ll.lon(bbr.getXmin()).lat(bbr.getYmin())))
									.topRight(tp -> tp.latlon(ll -> ll.lon(bbr.getXmax()).lat(bbr.getYmax())))));
		}

		return ob;
	}

	/**
	 * Convert the value into a term
	 * 
	 * @param value The original value
	 * @return The modified value as term string
	 */
	private String valueAsTermString(Object value) {
		if (value == null) {
			return null;
		}

		String text;
		if (value instanceof String) {
			text = (String) value;
			if (ObjectUtils.isEmpty(text)) {
				return null;
			}

			text = text.toLowerCase().trim().replaceAll("[ ]+", " ");

			StringBuilder sb = new StringBuilder();
			for (char c : text.toCharArray()) {
				if (Character.isSpaceChar(c)) {
					sb.append(".*");
				} else {
					sb.append("[");
					sb.append(convertChar(c));
					sb.append("]");
				}
			}

			text = sb.toString();
		} else {
			text = value.toString();
			text = ".*" + text + ".*";
		}

		return text;
	}

	/**
	 * Convert the given character into the multiple existing ways it has
	 * (uppercase, lowercase, with accents...)
	 */
	private String convertChar(char c) {
		StringBuilder sb = new StringBuilder();

		switch (c) {
		case 'a':
			sb.append('A');
			sb.append('Á');
			sb.append('À');
			sb.append('Ä');
			sb.append('Â');
			sb.append('Ã');
			sb.append('a');
			sb.append('á');
			sb.append('à');
			sb.append('ä');
			sb.append('â');
			sb.append('ã');
			break;
		case 'e':
			sb.append('E');
			sb.append('É');
			sb.append('È');
			sb.append('Ë');
			sb.append('Ê');
			sb.append('e');
			sb.append('é');
			sb.append('è');
			sb.append('ë');
			sb.append('ê');
			break;
		case 'i':
			sb.append('I');
			sb.append('Í');
			sb.append('Ì');
			sb.append('Ï');
			sb.append('Î');
			sb.append('i');
			sb.append('í');
			sb.append('ì');
			sb.append('ï');
			sb.append('î');
			break;
		case 'o':
			sb.append('O');
			sb.append('Ó');
			sb.append('Ò');
			sb.append('Ö');
			sb.append('Ô');
			sb.append('Õ');
			sb.append('o');
			sb.append('ó');
			sb.append('ò');
			sb.append('ö');
			sb.append('ô');
			sb.append('õ');
			break;
		case 'u':
			sb.append('U');
			sb.append('Ú');
			sb.append('Ù');
			sb.append('Ü');
			sb.append('Û');
			sb.append('u');
			sb.append('ú');
			sb.append('ù');
			sb.append('ü');
			sb.append('û');
			break;
		default:
			if (Character.isDigit(c)) {
				sb.append(c);
			} else if (Character.isLetter(c)) {
				sb.append(Character.toUpperCase(c));
				sb.append(c);
			} else {
				sb.append(c);
			}
			break;
		}

		return sb.toString();
	}

}
