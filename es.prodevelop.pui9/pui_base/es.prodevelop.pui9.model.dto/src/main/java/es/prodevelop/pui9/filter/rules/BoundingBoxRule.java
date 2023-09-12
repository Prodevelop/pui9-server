package es.prodevelop.pui9.filter.rules;

import es.prodevelop.pui9.filter.FilterRuleOperation;

public class BoundingBoxRule extends AbstractGeoRule {

	private static final long serialVersionUID = 1L;

	public static BoundingBoxRule of(String field, Integer srid, Double bottomLeftX, Double bottomLeftY,
			Double topRightX, Double topRightY) {
		return new BoundingBoxRule(field, srid, bottomLeftX, bottomLeftY, topRightX, topRightY);
	}

	public static BoundingBoxRule of(String field, Double bottomLeftX, Double bottomLeftY, Double topRightX,
			Double topRightY) {
		return new BoundingBoxRule(field, null, bottomLeftX, bottomLeftY, topRightX, topRightY);
	}

	private Double xmin;
	private Double ymin;
	private Double xmax;
	private Double ymax;

	private BoundingBoxRule() {
		this(null, null, null, null, null, null);
	}

	private BoundingBoxRule(String field, Integer srid, Double xmin, Double ymin, Double xmax, Double ymax) {
		super(FilterRuleOperation.boundingBox, field, srid);
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}

	public Double getXmin() {
		return xmin;
	}

	public Double getYmin() {
		return ymin;
	}

	public Double getXmax() {
		return xmax;
	}

	public Double getYmax() {
		return ymax;
	}

	@Override
	public String getSql() {
		return null;
	}

}
