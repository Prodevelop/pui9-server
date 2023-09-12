package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class GreaterEqualsThanTodayRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static GreaterEqualsThanTodayRule of(String field, Integer data) {
		return new GreaterEqualsThanTodayRule(field).withData(data);
	}

	private GreaterEqualsThanTodayRule() {
		this(null);
	}

	private GreaterEqualsThanTodayRule(String field) {
		super(field, FilterRuleOperation.get);
	}

	@Override
	public String getSqlOp() {
		return "";
	}

}
