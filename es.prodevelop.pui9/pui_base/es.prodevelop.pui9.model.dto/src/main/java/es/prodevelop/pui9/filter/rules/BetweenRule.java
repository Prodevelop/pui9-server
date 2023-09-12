package es.prodevelop.pui9.filter.rules;

import javax.annotation.Nullable;

import es.prodevelop.pui9.filter.FilterRuleOperation;

public class BetweenRule extends AbstractBetweenRule {

	private static final long serialVersionUID = 1L;

	@Nullable
	public static BetweenRule of(String field, Object lower, Object upper) {
		return new BetweenRule(field).withValues(lower, upper);
	}

	public static BetweenRule of(Object value, String leftColumn, String rightColumn) {
		return new BetweenRule(leftColumn).withColumnsAndValue(value, leftColumn, rightColumn);
	}

	private BetweenRule() {
		this(null);
	}

	private BetweenRule(String field) {
		super(field, FilterRuleOperation.bt);
	}

	@Override
	public String getSqlOp() {
		return " BETWEEN ";
	}

}
