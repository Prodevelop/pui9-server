package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class NotEndsWithRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static NotEndsWithRule of(String field, String data) {
		return new NotEndsWithRule(field).withData(data);
	}

	private NotEndsWithRule() {
		this(null);
	}

	private NotEndsWithRule(String field) {
		super(field, FilterRuleOperation.en);
	}

	@Override
	public String getSqlOp() {
		return " NOT LIKE ";
	}

}
