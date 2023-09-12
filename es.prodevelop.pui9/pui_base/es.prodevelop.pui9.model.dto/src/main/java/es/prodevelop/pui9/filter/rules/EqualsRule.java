package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class EqualsRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static EqualsRule of(String field, Object data) {
		return new EqualsRule(field).withData(data);
	}

	private EqualsRule() {
		this(null);
	}

	private EqualsRule(String field) {
		super(field, FilterRuleOperation.eq);
	}

	@Override
	public String getSqlOp() {
		return " = ";
	}

}
