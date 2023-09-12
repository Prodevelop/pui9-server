package es.prodevelop.pui9.dashboards.dto;

import java.util.List;

import es.prodevelop.pui9.utils.IPuiObject;

public class EChartsLineBarOptions implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private List<String> categories;
	private List<EChartsLineBarSeries> series;

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<EChartsLineBarSeries> getSeries() {
		return series;
	}

	public void setSeries(List<EChartsLineBarSeries> series) {
		this.series = series;
	}

}