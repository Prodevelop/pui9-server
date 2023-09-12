package es.prodevelop.pui9.filter.rules;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class GreaterEqualsThanRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static GreaterEqualsThanRule of(String field, Object data) {
		if (data instanceof LocalDate) {
			data = Instant.from(((LocalDate) data).atStartOfDay().atZone(ZoneId.systemDefault()));
		}
		return new GreaterEqualsThanRule(field).withData(data);
	}

	private GreaterEqualsThanRule() {
		this(null);
	}

	private GreaterEqualsThanRule(String field) {
		super(field, FilterRuleOperation.ge);
	}

	@Override
	public String getSqlOp() {
		return " >= ";
	}

}
