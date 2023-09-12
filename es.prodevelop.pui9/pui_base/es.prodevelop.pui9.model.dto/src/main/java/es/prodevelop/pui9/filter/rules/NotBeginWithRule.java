package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class NotBeginWithRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static NotBeginWithRule of(String field, String data) {
		return new NotBeginWithRule(field).withData(data);
	}

	private NotBeginWithRule() {
		this(null);
	}

	private NotBeginWithRule(String field) {
		super(field, FilterRuleOperation.bn);
	}

	@Override
	public String getSqlOp() {
		return " NOT LIKE ";
	}

}
