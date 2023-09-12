package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class EqualsThanTodayRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static EqualsThanTodayRule of(String field, Integer data) {
		return new EqualsThanTodayRule(field).withData(data);
	}

	private EqualsThanTodayRule() {
		this(null);
	}

	private EqualsThanTodayRule(String field) {
		super(field, FilterRuleOperation.eqt);
	}

	@Override
	public String getSqlOp() {
		return "";
	}

}
