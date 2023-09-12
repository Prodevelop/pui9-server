package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.FilterRuleOperation;

public class IsNotNullRule extends AbstractNullRule {

	private static final long serialVersionUID = 1L;

	public static IsNotNullRule of(String field) {
		return new IsNotNullRule(field);
	}

	private IsNotNullRule() {
		this(null);
	}

	private IsNotNullRule(String field) {
		super(field, FilterRuleOperation.nn);
	}

	@Override
	public String getSqlOp() {
		return " IS NOT NULL ";
	}

}
