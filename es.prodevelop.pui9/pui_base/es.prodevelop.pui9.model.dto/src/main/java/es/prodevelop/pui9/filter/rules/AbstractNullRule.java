package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public abstract class AbstractNullRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	protected AbstractNullRule(String field, FilterRuleOperation op) {
		super(field, op);
	}

	@Override
	@SuppressWarnings("unchecked")
	public AbstractNullRule withData(Object data) {
		return super.withData(null);
	}

}
