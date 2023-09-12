package es.prodevelop.pui9.db.helpers;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.filter.rules.AbstractBetweenRule;
import es.prodevelop.pui9.filter.rules.AbstractInRule;
import es.prodevelop.pui9.filter.rules.BeginWithRule;
import es.prodevelop.pui9.filter.rules.BetweenRule;
import es.prodevelop.pui9.filter.rules.BoundingBoxRule;
import es.prodevelop.pui9.filter.rules.ContainsRule;
import es.prodevelop.pui9.filter.rules.EndsWithRule;
import es.prodevelop.pui9.filter.rules.EqualsRule;
import es.prodevelop.pui9.filter.rules.GreaterEqualsThanRule;
import es.prodevelop.pui9.filter.rules.GreaterThanRule;
import es.prodevelop.pui9.filter.rules.InRule;
import es.prodevelop.pui9.filter.rules.IntersectsByPointRule;
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
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * This is an abstract implementation shared for all the database types.
 * Nevertheless, there are some specific methods that are specific for each
 * database vendor
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractDatabaseHelper implements IDatabaseHelper {

	private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";

	protected static final String DATE_FORMAT = "{DATE_FORMAT}";
	protected static final String COLUMNNAME = "_columnname_";
	protected static final String TIMEZONE = "_timezone_";
	protected static final String OP = "_op_";
	protected static final String VALUE = "_value_";
	protected static final String BEGINNING = "_beginning_";
	protected static final String END = "_end_";
	protected static final String AND = " AND ";

	@Autowired
	private DaoRegistry daoRegistry;

	private DSLContext dslContext;

	@Override
	public final DSLContext getDSLContext() {
		if (dslContext == null) {
			dslContext = initializeDSLContext();
		}
		return dslContext;
	}

	protected abstract DSLContext initializeDSLContext();

	public String processSearchText(Class<? extends IDto> dtoClass, List<String> fields, String text, ZoneId zoneId) {
		Map<String, String> fieldTextMap = new LinkedHashMap<>();
		fields.forEach(field -> fieldTextMap.put(field, text));
		return processSearchText(dtoClass, fieldTextMap, zoneId);
	}

	public String processSearchText(Class<? extends IDto> dtoClass, Map<String, String> fieldTextMap, ZoneId zoneId) {
		FilterGroup group = FilterGroup.orGroup();

		for (Iterator<Entry<String, String>> it = fieldTextMap.entrySet().iterator(); it.hasNext();) {
			Entry<String, String> next = it.next();
			String field = next.getKey();
			String text = next.getValue();
			if (ObjectUtils.isEmpty(field) || ObjectUtils.isEmpty(text)) {
				continue;
			}

			// escape single quotes and remove wilcard character
			text = text.replace("'", "''").replace("%", "");

			AbstractFilterRule rule = ContainsRule.of(field, text);
			if (rule.isDate(dtoClass)) {
				rule.withZoneId(zoneId);
			} else if (rule.isBoolean(dtoClass) && (text.equalsIgnoreCase(Boolean.TRUE.toString())
					|| text.equalsIgnoreCase(Boolean.FALSE.toString()))) {
				rule = EqualsRule.of(field, Boolean.valueOf(text));
			}

			group.addRule(rule);
		}

		return processFilters(dtoClass, group, true);
	}

	@Override
	public String processFilters(Class<? extends IDto> dtoClass, FilterGroup filters, boolean addAlias) {
		if (filters == null) {
			return null;
		}

		List<String> ruleList = new ArrayList<>();
		List<String> filterList = new ArrayList<>();

		// rules
		for (AbstractFilterRule rule : filters.getRules()) {
			String r = processRule(dtoClass, rule, addAlias);
			if (r != null) {
				ruleList.add(r);
			}
		}

		// groups
		for (FilterGroup filter : filters.getGroups()) {
			String f = processFilters(dtoClass, filter, addAlias);
			if (f != null) {
				filterList.add(f);
			}
		}

		StringBuilder sb = new StringBuilder();

		// rules
		if (!ruleList.isEmpty()) {
			sb.append("(");
		}
		for (Iterator<String> it = ruleList.iterator(); it.hasNext();) {
			sb.append(it.next());
			if (it.hasNext()) {
				sb.append(" " + filters.getGroupOp() + " ");
			}
		}
		if (!ruleList.isEmpty()) {
			sb.append(")");
		}

		// groups
		if (!filterList.isEmpty()) {
			if (!ruleList.isEmpty()) {
				sb.append(" " + filters.getGroupOp());
			}
			sb.append(" (");
		}
		for (Iterator<String> it = filterList.iterator(); it.hasNext();) {
			sb.append(it.next());
			if (it.hasNext()) {
				sb.append(" " + filters.getGroupOp() + " ");
			}
		}
		if (!filterList.isEmpty()) {
			sb.append(")");
		}

		if (ObjectUtils.isEmpty(sb.toString())) {
			return null;
		} else {
			return sb.toString();
		}
	}

	private String processRule(Class<? extends IDto> dtoClass, AbstractFilterRule rule, boolean addAlias) {
		if (rule == null) {
			return null;
		}

		Field field = DtoRegistry.getJavaFieldFromAllFields(dtoClass, rule.getField());
		if (field == null) {
			return null;
		}

		boolean hasLangSupport = ITableDto.class.isAssignableFrom(dtoClass)
				&& daoRegistry.hasLanguageSupport(daoRegistry.getDaoFromDto(dtoClass));
		String sqlColumn = rule.getField();

		if (!DtoRegistry.getColumnNames(dtoClass).contains(sqlColumn)) {
			// if the columns list doesn't contain the given column, check if it was a field
			// and get the corresponding column name
			sqlColumn = DtoRegistry.getColumnNameFromFieldName(dtoClass, sqlColumn);
		}

		if (hasLangSupport && rule.getField().equalsIgnoreCase(IDto.LANG_COLUMN_NAME)) {
			sqlColumn = (addAlias ? IDao.TABLE_LANG_PREFIX + "." : "") + sqlColumn;
		} else {
			sqlColumn = (addAlias ? IDao.TABLE_PREFIX + "." : "") + sqlColumn;
		}

		String sqlTextValue = valueAsSqlString(rule.getData());
		Instant instantValue = rule.valueAsInstant();
		Number numberValue = rule.valueAsNumber();
		Boolean boolValue = rule.valueAsBoolean();

		boolean isLargeStringField = rule.isLargeStringField(dtoClass);
		if (sqlTextValue == null && instantValue == null && numberValue == null && boolValue == null) {
			if (rule instanceof EqualsRule) {
				rule = IsNullRule.of(rule.getField());
			} else if (rule instanceof NotEqualsRule) {
				rule = IsNotNullRule.of(rule.getField());
			}
		}

		StringBuilder sqlBuilder = new StringBuilder();

		if (rule instanceof EqualsRule || rule instanceof NotEqualsRule) {
			if (rule.isDataIsColumn()) {
				sqlBuilder.append(
						getSqlTextOperation(sqlColumn, true, false, rule.getSqlOp(), sqlTextValue, false, false, true));
			} else {
				if (rule.isString(dtoClass)) {
					sqlBuilder.append(getSqlTextOperation(sqlColumn, rule.isCaseSensitiveAndAccents(),
							isLargeStringField, rule.getSqlOp(), sqlTextValue, false, false, false));
				} else if (rule.isNumber(dtoClass) || rule.isFloatingNumber(dtoClass) && numberValue != null) {
					sqlBuilder.append(sqlColumn + rule.getSqlOp() + numberValue);
				} else if (rule.isDate(dtoClass) && instantValue != null) {
					sqlBuilder.append(sqlColumn + rule.getSqlOp() + getSqlDateOperation(instantValue));
				} else if (rule.isBoolean(dtoClass) && boolValue != null) {
					sqlBuilder.append(sqlColumn + rule.getSqlOp() + boolValue);
				}
			}
		} else if (rule instanceof BeginWithRule || rule instanceof NotBeginWithRule) {
			if (rule.isString(dtoClass)) {
				sqlBuilder.append(getSqlTextOperation(sqlColumn, rule.isCaseSensitiveAndAccents(), isLargeStringField,
						rule.getSqlOp(), sqlTextValue, false, true, rule.isDataIsColumn()));
			}
		} else if (rule instanceof EndsWithRule || rule instanceof NotEndsWithRule) {
			if (rule.isString(dtoClass)) {
				sqlBuilder.append(getSqlTextOperation(sqlColumn, rule.isCaseSensitiveAndAccents(), isLargeStringField,
						rule.getSqlOp(), sqlTextValue, true, false, rule.isDataIsColumn()));
			}
		} else if (rule instanceof ContainsRule || rule instanceof NotContainsRule) {
			if (rule.isString(dtoClass)) {
				sqlBuilder.append(getSqlTextOperation(sqlColumn, rule.isCaseSensitiveAndAccents(), isLargeStringField,
						rule.getSqlOp(), sqlTextValue, true, true, rule.isDataIsColumn()));
			} else if (rule.isNumber(dtoClass) || rule.isFloatingNumber(dtoClass) || rule.isBoolean(dtoClass)) {
				// in case the rule was build for text searching
				sqlBuilder.append(getSqlCastOperationAsString(sqlColumn, rule.getSqlOp(), sqlTextValue, true, true));
			} else if (rule.isDate(dtoClass)) {
				// in case the rule was build for text searching
				sqlTextValue = sqlTextValue != null ? sqlTextValue.replace("/", "-").replace(".", "-") : "";
				sqlBuilder.append(
						getSqlDateOperationAsString(sqlColumn, rule.getSqlOp(), sqlTextValue, rule.getZoneId()));
			}
		} else if (rule instanceof LowerThanRule || rule instanceof LowerEqualsThanRule
				|| rule instanceof GreaterThanRule || rule instanceof GreaterEqualsThanRule) {
			if (rule.isDataIsColumn()) {
				sqlBuilder.append(sqlColumn + rule.getSqlOp() + sqlTextValue);
			} else {
				if (rule.isNumber(dtoClass) || rule.isFloatingNumber(dtoClass) && numberValue != null) {
					sqlBuilder.append(sqlColumn + rule.getSqlOp() + numberValue);
				} else if (rule.isDate(dtoClass) && instantValue != null) {
					sqlBuilder.append(sqlColumn + rule.getSqlOp() + getSqlDateOperation(instantValue));
				} else if (rule.isString(dtoClass)) {
					sqlBuilder.append(getSqlTextOperation(sqlColumn, true, false, rule.getSqlOp(), sqlTextValue, false,
							false, false));
				}
			}
		} else if (rule instanceof BetweenRule || rule instanceof NotBetweenRule) {
			AbstractBetweenRule r = (AbstractBetweenRule) rule;
			if (!r.isDataIsColumn()) {
				Object lower = null;
				Object upper = null;
				if (rule.isNumber(dtoClass) || rule.isFloatingNumber(dtoClass)) {
					lower = r.getLower();
					upper = r.getUpper();
				} else if (rule.isDate(dtoClass) && r.getLower() instanceof Instant
						&& r.getUpper() instanceof Instant) {
					lower = getSqlDateOperation((Instant) r.getLower());
					upper = getSqlDateOperation((Instant) r.getUpper());
				}
				if (lower != null && upper != null) {
					sqlBuilder.append(sqlColumn + r.getSqlOp() + lower + AND + upper);
				}
			} else {
				Object value = null;
				String left = r.getLower();
				if (!DtoRegistry.getAllColumnNames(dtoClass).contains(left)) {
					left = DtoRegistry.getColumnNameFromFieldName(dtoClass, left);
				}
				String right = r.getUpper();
				if (!DtoRegistry.getAllColumnNames(dtoClass).contains(right)) {
					right = DtoRegistry.getColumnNameFromFieldName(dtoClass, right);
				}

				if (rule.isNumber(dtoClass) || rule.isFloatingNumber(dtoClass)) {
					value = r.getValue();
				} else if (rule.isDate(dtoClass) && r.getValue() instanceof Instant) {
					value = getSqlDateOperation((Instant) r.getValue());
				}
				if (value != null && !ObjectUtils.isEmpty(left) && !ObjectUtils.isEmpty(right)) {
					sqlBuilder.append(value + r.getSqlOp() + left + AND + right);
				}
			}
		} else if (rule instanceof InRule || rule instanceof NotInRule) {
			AbstractInRule r = (AbstractInRule) rule;
			sqlBuilder.append(sqlColumn + r.getSqlOp() + " (");
			for (Iterator<Object> it = r.getCollection().iterator(); it.hasNext();) {
				Object next = it.next();
				if (rule.isString(dtoClass)) {
					String strVal = (String) next;
					if (strVal.startsWith("'") && strVal.endsWith("'")) {
						sqlBuilder.append(strVal);
					} else {
						sqlBuilder.append("'" + strVal + "'");
					}
				} else if (rule.isNumber(dtoClass) || rule.isFloatingNumber(dtoClass)) {
					sqlBuilder.append(next);
				}

				if (it.hasNext()) {
					sqlBuilder.append(",");
				}
			}
			sqlBuilder.append(")");
		} else if (rule instanceof IsNullRule || rule instanceof IsNotNullRule) {
			sqlBuilder.append(sqlColumn + rule.getSqlOp());
		} else if (rule instanceof BoundingBoxRule) {
			BoundingBoxRule r = (BoundingBoxRule) rule;
			sqlBuilder.append(
					getBoundingBoxSql(r.getField(), r.getSrid(), r.getXmin(), r.getYmin(), r.getXmax(), r.getYmax()));
		} else if (rule instanceof IntersectsByPointRule) {
			IntersectsByPointRule r = (IntersectsByPointRule) rule;
			sqlBuilder.append(getIntersectsByPoint(r.getField(), r.getSrid(), r.getX(), r.getY()));
		}

		String sql = sqlBuilder.toString();
		return ObjectUtils.isEmpty(sql) ? null : sql;
	}

	/**
	 * Returns the value as String to be used with text fields
	 * 
	 * @param value The value
	 * @return The value as String
	 */
	private String valueAsSqlString(Object value) {
		if (value == null) {
			return "";
		}

		String text;
		if (value instanceof String) {
			text = (String) value;
			if (ObjectUtils.isEmpty(text)) {
				return "";
			}

			if (text.contains("'")) {
				text = text.replace("'", "''");
			}
		} else {
			text = value.toString();
		}

		return text;
	}

	protected abstract String getBoundingBoxSql(String column, Integer srid, Double xmin, Double ymin, Double xmax,
			Double ymax);

	protected abstract String getIntersectsByPoint(String column, Integer srid, Double x, Double y);

	/**
	 * Get the Sql for searching a number as string (convert the Number into a
	 * String)
	 */
	private String getSqlCastOperationAsString(String column, String operation, String value, boolean beginning,
			boolean end) {
		return getSqlCastToString().replace(COLUMNNAME, column) + operation + (beginning ? "'%" : "'") + value
				+ (end ? "%'" : "'");
	}

	/**
	 * Get the vendor specific Sql for converting a Number into a String
	 */
	protected abstract String getSqlCastToString();

	/**
	 * Get the Sql for searching a date as string (convert the Date into a String)
	 */
	private String getSqlDateOperationAsString(String column, String operation, String value, ZoneId zoneId) {
		return getSqlConvertDateIntoString(zoneId).replace(COLUMNNAME, column).replace(TIMEZONE, zoneId.getId())
				+ operation + "'%" + value + "%'";
	}

	/**
	 * Get the vendor specific Sql for converting a Date into a String
	 */
	protected abstract String getSqlConvertDateIntoString(ZoneId zoneId);

	/**
	 * Get the Sql for converting an String into a Date
	 */
	private String getSqlDateOperation(Instant instant) {
		if (instant == null) {
			return "''";
		}
		boolean hasMillis = instant.get(ChronoField.MILLI_OF_SECOND) != 0;
		return getSqlConvertStringIntoDate(hasMillis).replace(VALUE, PuiDateUtil.temporalAccessorToString(instant,
				DateTimeFormatter
						.ofPattern(hasMillis ? PuiDateUtil.DEFAULT_FORMAT_WITH_MILLIS : PuiDateUtil.DEFAULT_FORMAT)
						.withZone(ZoneId.systemDefault())));
	}

	/**
	 * Get the vendor specific Sql for converting a String into Date
	 */
	protected abstract String getSqlConvertStringIntoDate(boolean withMillis);

	/**
	 * Get the SQL for searching Text, removing the accents and making the lowercase
	 * operation
	 */
	private String getSqlTextOperation(String column, boolean caseSensitiveAndAccents, boolean isLargeStringField,
			String operation, String value, boolean beginning, boolean end, boolean dataIsColumn) {
		return getSqlTextOperation(caseSensitiveAndAccents, isLargeStringField, dataIsColumn)
				.replace(COLUMNNAME, column).replace(OP, operation)
				.replace(VALUE, caseSensitiveAndAccents ? value : value.toLowerCase())
				.replace(BEGINNING, beginning ? "%" : "").replace(END, end ? "%" : "");
	}

	/**
	 * Get the vendor specific Sql for converting a String by removing the accents
	 * and converting to lowercase
	 */
	protected abstract String getSqlTextOperation(boolean caseSensitiveAndAccents, boolean isLargeStringField,
			boolean dataIsColumn);

	/**
	 * Adapt the format of the DateFormat to the one of the user has assigned
	 * 
	 * @param baseDateFormat The base DateFormat to be used
	 * @return The new DateFormat to be used
	 */
	protected String adaptDateFormatToUser(String baseDateFormat) {
		String dateFormat = DEFAULT_DATE_FORMAT;
		if (PuiUserSession.getCurrentSession() != null) {
			dateFormat = PuiUserSession.getCurrentSession().getDateformat();
		}
		return baseDateFormat.replace(DATE_FORMAT, dateFormat).replace("/", "-").replace(".", "-");
	}

}
