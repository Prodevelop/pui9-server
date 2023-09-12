package es.prodevelop.pui9.dashboards.dto;

import java.io.Serializable;

public class EChartsPieSeriesData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private double value;

	public EChartsPieSeriesData(String name, double value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}