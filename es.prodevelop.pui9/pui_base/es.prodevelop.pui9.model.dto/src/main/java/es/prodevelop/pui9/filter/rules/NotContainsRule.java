package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class NotContainsRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static NotContainsRule of(String field, String data) {
		return new NotContainsRule(field).withData(data);
	}

	private NotContainsRule() {
		this(null);
	}

	private NotContainsRule(String field) {
		super(field, FilterRuleOperation.nc);
	}

	@Override
	public String getSqlOp() {
		return " NOT LIKE ";
	}

}
