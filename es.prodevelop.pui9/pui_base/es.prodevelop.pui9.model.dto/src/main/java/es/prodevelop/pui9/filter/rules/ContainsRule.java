package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class ContainsRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static ContainsRule of(String field, String data) {
		return new ContainsRule(field).withData(data);
	}

	private ContainsRule() {
		this(null);
	}

	private ContainsRule(String field) {
		super(field, FilterRuleOperation.cn);
	}

	@Override
	public String getSqlOp() {
		return " LIKE ";
	}

}
