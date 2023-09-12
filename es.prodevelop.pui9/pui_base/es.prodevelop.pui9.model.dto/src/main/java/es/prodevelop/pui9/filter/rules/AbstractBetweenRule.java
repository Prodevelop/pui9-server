package es.prodevelop.pui9.filter.rules;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public abstract class AbstractBetweenRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	protected AbstractBetweenRule(String field, FilterRuleOperation op) {
		super(field, op);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	protected <T extends AbstractBetweenRule> T withValues(Object lower, Object upper) {
		List<Object> values = new ArrayList<>();
		if ((lower instanceof Number && upper instanceof Number)
				|| (lower instanceof Instant && upper instanceof Instant)) {
			values.add(lower);
			values.add(upper);
		} else if (lower instanceof LocalDate && upper instanceof LocalDate) {
			values.add(Instant.from(((LocalDate) lower).atStartOfDay().atZone(ZoneId.systemDefault())));
			values.add(Instant.from(((LocalDate) upper).atTime(LocalTime.MAX).with(ChronoField.MILLI_OF_SECOND, 999)
					.atZone(ZoneId.systemDefault())));
		}

		return (T) withData(values);
	}

	@SuppressWarnings("unchecked")
	protected <T extends AbstractBetweenRule> T withColumnsAndValue(Object value, String leftColumn,
			String rightColumn) {
		withDataIsColumn();
		List<Object> values = new ArrayList<>();
		values.add(value);
		values.add(leftColumn);
		values.add(rightColumn);
		return (T) withData(values);
	}

	@Override
	@SuppressWarnings("unchecked")
	public AbstractBetweenRule withData(Object data) {
		if (!(data instanceof List<?>)) {
			return null;
		}

		List<?> list = (List<?>) data;
		if (list.size() == 2) {
			// column between values
			Object first = list.get(0);
			Object second = list.get(1);
			if (first instanceof Number || first instanceof Instant) {
				return super.withData(data);
			} else if (first instanceof String) {
				// are Instants?
				Object lower = valueAsInstant(first);
				Object upper = valueAsInstant(second);
				if (lower == null || upper == null) {
					// are numbers?
					lower = valueAsNumber(first);
					upper = valueAsNumber(second);
				}
				if (lower == null || upper == null) {
					return null;
				}
				List<Object> values = new ArrayList<>();
				values.add(lower);
				values.add(upper);
				return super.withData(values);
			} else {
				return null;
			}
		} else if (list.size() == 3) {
			// value between columns
			Object val = list.get(0);
			if (val instanceof Number || val instanceof Instant) {
				return super.withData(data);
			} else if (val instanceof String) {
				// is Instant?
				Object value = valueAsInstant(val);
				if (value == null) {
					// is number
					value = valueAsNumber(val);
				}
				if (value == null) {
					return null;
				}
				List<Object> values = new ArrayList<>();
				values.add(value);
				values.add(list.get(1));
				values.add(list.get(2));
				return super.withData(values);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getField() {
		return !isDataIsColumn() ? super.getField() : (String) ((List<Object>) getData()).get(1);
	}

	@SuppressWarnings("unchecked")
	public <T> T getLower() {
		return !isDataIsColumn() ? ((List<T>) getData()).get(0) : ((List<T>) getData()).get(1);
	}

	@SuppressWarnings("unchecked")
	public <T> T getUpper() {
		return !isDataIsColumn() ? ((List<T>) getData()).get(1) : ((List<T>) getData()).get(2);
	}

	@SuppressWarnings("unchecked")
	public Object getValue() {
		return !isDataIsColumn() ? super.getData() : (Object) ((List<Object>) super.getData()).get(0);
	}

	@Override
	public String toString() {
		return !isDataIsColumn() ? super.toString()
				: (getValue() + " " + getOp() + " [" + getLower() + " and " + getUpper() + "]");
	}

}
