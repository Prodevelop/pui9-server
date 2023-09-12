package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class LowerThanTodayRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static LowerThanTodayRule of(String field, Integer data) {
		return new LowerThanTodayRule(field).withData(data);
	}

	private LowerThanTodayRule() {
		this(null);
	}

	private LowerThanTodayRule(String field) {
		super(field, FilterRuleOperation.ltt);
	}

	@Override
	public String getSqlOp() {
		return "";
	}

}
