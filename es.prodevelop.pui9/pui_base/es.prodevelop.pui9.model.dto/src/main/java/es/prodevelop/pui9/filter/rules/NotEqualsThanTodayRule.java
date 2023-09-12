package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class NotEqualsThanTodayRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static NotEqualsThanTodayRule of(String field, Integer data) {
		return new NotEqualsThanTodayRule(field).withData(data);
	}

	private NotEqualsThanTodayRule() {
		this(null);
	}

	private NotEqualsThanTodayRule(String field) {
		super(field, FilterRuleOperation.net);
	}

	@Override
	public String getSqlOp() {
		return "";
	}

}
