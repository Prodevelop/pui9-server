package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class GreaterThanTodayRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static GreaterThanTodayRule of(String field, Integer data) {
		return new GreaterThanTodayRule(field).withData(data);
	}

	private GreaterThanTodayRule() {
		this(null);
	}

	private GreaterThanTodayRule(String field) {
		super(field, FilterRuleOperation.gtt);
	}

	@Override
	public String getSqlOp() {
		return "";
	}

}
