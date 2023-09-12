package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class LowerEqualsThanTodayRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static LowerEqualsThanTodayRule of(String field, Integer data) {
		return new LowerEqualsThanTodayRule(field).withData(data);
	}

	private LowerEqualsThanTodayRule() {
		this(null);
	}

	private LowerEqualsThanTodayRule(String field) {
		super(field, FilterRuleOperation.let);
	}

	@Override
	public String getSqlOp() {
		return "";
	}

}
