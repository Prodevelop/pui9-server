package es.prodevelop.pui9.filter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.annotation.Nullable;

import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.utils.IPuiObject;
import es.prodevelop.pui9.utils.PuiDateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

/**
 * This class represents a Rule in a Filter. A simple Rule is composed by the
 * field agains it's operating, the operation type and the value to compare with
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Schema(description = "Rules description")
public abstract class AbstractFilterRule implements IPuiObject {

	private static final long serialVersionUID = 1L;

	@Schema(description = "Field", requiredMode = RequiredMode.REQUIRED)
	private String field;

	@Schema(description = "Operation", requiredMode = RequiredMode.REQUIRED)
	private FilterRuleOperation op;

	@Schema(description = "Value", requiredMode = RequiredMode.NOT_REQUIRED)
	private Object data;

	@Schema(description = "If the data is a column name to compare with", requiredMode = RequiredMode.NOT_REQUIRED)
	private Boolean dataIsColumn = false;

	@Schema(description = "If case sensitive and considering accents", requiredMode = RequiredMode.NOT_REQUIRED)
	private Boolean caseSensitiveAndAccents = false;

	@Schema(hidden = true)
	private transient ZoneId zoneId = ZoneId.systemDefault();

	protected AbstractFilterRule(String field, FilterRuleOperation op) {
		this.field = field;
		this.op = op;
		this.data = null;
		this.dataIsColumn = false;
		this.caseSensitiveAndAccents = false;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractFilterRule> T withData(Object data) {
		this.data = data;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractFilterRule> T withDataIsColumn() {
		this.dataIsColumn = true;
		this.caseSensitiveAndAccents = true;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractFilterRule> T withCaseSensitiveAndAccents() {
		this.caseSensitiveAndAccents = true;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractFilterRule> T withZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
		return (T) this;
	}

	public String getField() {
		return field;
	}

	public FilterRuleOperation getOp() {
		return op;
	}

	public abstract String getSqlOp();

	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T) data;
	}

	public Boolean isDataIsColumn() {
		return dataIsColumn;
	}

	public Boolean isCaseSensitiveAndAccents() {
		return caseSensitiveAndAccents;
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	/**
	 * Returns the value as Instant
	 * 
	 * @return The value as Instant
	 */
	@Nullable
	public Instant valueAsInstant() {
		return valueAsInstant(data);
	}

	protected Instant valueAsInstant(Object value) {
		if (value instanceof Instant) {
			return (Instant) value;
		} else if (value instanceof ZonedDateTime) {
			return ((ZonedDateTime) value).toInstant();
		} else if (value instanceof String) {
			return PuiDateUtil.stringToInstant((String) value);
		} else {
			return null;
		}
	}

	/**
	 * Returns the value as Number to be used with text fields
	 * 
	 * @return The value as Number
	 */
	@Nullable
	public Number valueAsNumber() {
		return valueAsNumber(data);
	}

	protected Number valueAsNumber(Object value) {
		if (value instanceof Number) {
			return (Number) value;
		} else if (value instanceof String) {
			String val = (String) value;
			if (val.startsWith("\"")) {
				val = val.substring(1);
			}
			if (val.endsWith("\"")) {
				val = val.substring(0, val.length() - 1);
			}

			val = val.replace(",", ".");

			try {
				// is Integer?
				return Integer.parseInt(val);
			} catch (Exception e1) {
				try {
					// is Long?
					return Long.parseLong(val);
				} catch (Exception e2) {
					try {
						// is Double?
						return Double.parseDouble(val);
					} catch (Exception e3) {
						try {
							// is BigDecimal?
							return new BigDecimal(val);
						} catch (Exception e4) {
							return null;
						}
					}
				}
			}
		} else {
			return null;
		}
	}

	/**
	 * Returns the value as Boolean
	 * 
	 * @return The value as Boolean
	 */
	@Nullable
	public Boolean valueAsBoolean() {
		if (data instanceof Boolean) {
			return (Boolean) data;
		} else if (data instanceof String) {
			String val = (String) data;
			return val.equalsIgnoreCase(Boolean.TRUE.toString()) || val.equalsIgnoreCase(Boolean.FALSE.toString())
					? Boolean.valueOf(val)
					: null;
		} else {
			return null;
		}
	}

	/**
	 * Check if the given field is a text field
	 */
	public boolean isString(Class<? extends IDto> dtoClass) {
		return DtoRegistry.getStringFields(dtoClass).contains(field);
	}

	/**
	 * Check if the given field is an Clob or very large field in the database
	 */
	public boolean isLargeStringField(Class<? extends IDto> dtoClass) {
		Integer length = DtoRegistry.getFieldMaxLength(dtoClass, field);
		return length == null || length == -1 || length >= (32 * 1024);
	}

	/**
	 * Check if the given field is a numeric field
	 */
	public boolean isNumber(Class<? extends IDto> dtoClass) {
		return DtoRegistry.getNumericFields(dtoClass).contains(field);
	}

	/**
	 * Check if the given field is a floatinc numeric field
	 */
	public boolean isFloatingNumber(Class<? extends IDto> dtoClass) {
		return DtoRegistry.getFloatingFields(dtoClass).contains(field);
	}

	/**
	 * Check if the given field is a date field
	 */
	public boolean isDate(Class<? extends IDto> dtoClass) {
		return DtoRegistry.getDateTimeFields(dtoClass).contains(field);
	}

	/**
	 * Check if the given field is a Boolean field
	 */
	public boolean isBoolean(Class<? extends IDto> dtoClass) {
		return DtoRegistry.getBooleanFields(dtoClass).contains(field);
	}

	@Override
	public String toString() {
		return field + " " + op + " " + (dataIsColumn ? "[" : "") + data + (dataIsColumn ? "]" : "");
	}

}
