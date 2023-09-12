package es.prodevelop.pui9.filter.rules;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class LowerThanRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static LowerThanRule of(String field, Object data) {
		if (data instanceof LocalDate) {
			data = Instant.from(((LocalDate) data).atStartOfDay().atZone(ZoneId.systemDefault()));
		}
		return new LowerThanRule(field).withData(data);
	}

	private LowerThanRule() {
		this(null);
	}

	private LowerThanRule(String field) {
		super(field, FilterRuleOperation.lt);
	}

	@Override
	public String getSqlOp() {
		return " < ";
	}

}
