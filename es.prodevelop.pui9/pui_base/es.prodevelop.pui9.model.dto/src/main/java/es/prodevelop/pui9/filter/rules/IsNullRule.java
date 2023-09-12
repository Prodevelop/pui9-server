package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.FilterRuleOperation;

public class IsNullRule extends AbstractNullRule {

	private static final long serialVersionUID = 1L;

	public static IsNullRule of(String field) {
		return new IsNullRule(field);
	}

	private IsNullRule() {
		this(null);
	}

	private IsNullRule(String field) {
		super(field, FilterRuleOperation.nu);
	}

	@Override
	public String getSqlOp() {
		return " IS NULL ";
	}

}
