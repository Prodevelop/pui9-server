package es.prodevelop.pui9.filter.rules;

import java.util.Collection;

import javax.annotation.Nullable;

import es.prodevelop.pui9.filter.FilterRuleOperation;

public class NotInRule extends AbstractInRule {

	private static final long serialVersionUID = 1L;

	@Nullable
	public static NotInRule of(String field, Collection<?> data) {
		return (NotInRule) new NotInRule(field).withData(data);
	}

	private NotInRule() {
		this(null);
	}

	private NotInRule(String field) {
		super(field, FilterRuleOperation.ni);
	}

	@Override
	public String getSqlOp() {
		return " NOT IN ";
	}

}
