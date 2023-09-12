package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class EndsWithRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static EndsWithRule of(String field, String data) {
		return new EndsWithRule(field).withData(data);
	}

	private EndsWithRule() {
		this(null);
	}

	private EndsWithRule(String field) {
		super(field, FilterRuleOperation.ew);
	}

	@Override
	public String getSqlOp() {
		return " LIKE ";
	}

}
