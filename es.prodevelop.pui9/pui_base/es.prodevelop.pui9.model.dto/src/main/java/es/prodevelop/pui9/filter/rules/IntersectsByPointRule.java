package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.FilterRuleOperation;

public class IntersectsByPointRule extends AbstractGeoRule {

	private static final long serialVersionUID = 1L;

	public static IntersectsByPointRule of(String field, Integer srid, Double x, Double y) {
		return new IntersectsByPointRule(field, srid, x, y);
	}

	private Double x;
	private Double y;

	private IntersectsByPointRule() {
		this(null, null, null, null);
	}

	private IntersectsByPointRule(String field, Integer srid, Double x, Double y) {
		super(FilterRuleOperation.intersects, field, srid);
		this.x = x;
		this.y = y;
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	@Override
	public String getSql() {
		return null;
	}

}
