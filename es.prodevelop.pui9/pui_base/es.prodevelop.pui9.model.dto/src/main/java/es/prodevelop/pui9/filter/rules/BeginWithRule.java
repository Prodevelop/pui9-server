package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class BeginWithRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static BeginWithRule of(String field, String data) {
		return new BeginWithRule(field).withData(data);
	}

	private BeginWithRule() {
		this(null);
	}

	private BeginWithRule(String field) {
		super(field, FilterRuleOperation.bw);
	}

	@Override
	public String getSqlOp() {
		return " LIKE ";
	}

}
