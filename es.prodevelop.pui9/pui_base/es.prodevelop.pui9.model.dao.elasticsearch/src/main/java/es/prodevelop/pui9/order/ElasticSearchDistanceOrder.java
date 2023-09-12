package es.prodevelop.pui9.order;

public class ElasticSearchDistanceOrder extends Order {

	private static final long serialVersionUID = 1L;

	public static ElasticSearchDistanceOrder of(String column, OrderDirection direction, Double x, Double y) {
		return new ElasticSearchDistanceOrder(column, direction, x, y);
	}

	private Double x;
	private Double y;

	private ElasticSearchDistanceOrder(String column, OrderDirection direction, Double x, Double y) {
		super(column, direction);
		this.x = x;
		this.y = y;
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

}
