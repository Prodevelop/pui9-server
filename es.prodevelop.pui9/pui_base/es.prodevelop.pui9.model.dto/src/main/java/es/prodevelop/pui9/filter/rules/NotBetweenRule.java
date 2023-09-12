package es.prodevelop.pui9.filter.rules;

import javax.annotation.Nullable;

import es.prodevelop.pui9.filter.FilterRuleOperation;

public class NotBetweenRule extends AbstractBetweenRule {

	private static final long serialVersionUID = 1L;

	@Nullable
	public static NotBetweenRule of(String field, Object lower, Object upper) {
		return new NotBetweenRule(field).withValues(lower, upper);
	}

	public static NotBetweenRule of(Object value, String leftColumn, String rightColumn) {
		return new NotBetweenRule(leftColumn).withColumnsAndValue(value, leftColumn, rightColumn);
	}

	private NotBetweenRule() {
		this(null);
	}

	private NotBetweenRule(String field) {
		super(field, FilterRuleOperation.nbt);
	}

	@Override
	public String getSqlOp() {
		return " NOT BETWEEN ";
	}

}
