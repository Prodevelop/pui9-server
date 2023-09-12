package es.prodevelop.pui9.filter.rules;

import java.util.Collection;

import javax.annotation.Nullable;

import es.prodevelop.pui9.filter.FilterRuleOperation;

public class InRule extends AbstractInRule {

	private static final long serialVersionUID = 1L;

	@Nullable
	public static InRule of(String field, Collection<?> data) {
		return (InRule) new InRule(field).withData(data);
	}

	private InRule() {
		this(null);
	}

	private InRule(String field) {
		super(field, FilterRuleOperation.in);
	}

	@Override
	public String getSqlOp() {
		return " IN ";
	}

}
