package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class NotEqualsRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static NotEqualsRule of(String field, Object data) {
		return new NotEqualsRule(field).withData(data);
	}

	private NotEqualsRule() {
		this(null);
	}

	private NotEqualsRule(String field) {
		super(field, FilterRuleOperation.ne);
	}

	@Override
	public String getSqlOp() {
		return " <> ";
	}

}
