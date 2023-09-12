package es.prodevelop.pui9.filter.rules;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public class GreaterThanRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	public static GreaterThanRule of(String field, Object data) {
		if (data instanceof LocalDate) {
			data = Instant.from(((LocalDate) data).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()));
		}
		return new GreaterThanRule(field).withData(data);
	}

	private GreaterThanRule() {
		this(null);
	}

	private GreaterThanRule(String field) {
		super(field, FilterRuleOperation.gt);
	}

	@Override
	public String getSqlOp() {
		return " > ";
	}

}
