package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.AbstractFilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;

public abstract class AbstractGeoRule extends AbstractFilterRule {

	private static final long serialVersionUID = 1L;

	private Integer srid;

	protected AbstractGeoRule(FilterRuleOperation op, String field, Integer srid) {
		super(field, op);
		this.srid = srid;
	}

	public Integer getSrid() {
		return srid;
	}

	@Override
	public String getSqlOp() {
		return null;
	}

	public abstract String getSql();

}
