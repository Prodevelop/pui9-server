package es.prodevelop.pui9.dashboards.dto;

import java.util.ArrayList;
import java.util.List;

import es.prodevelop.pui9.utils.IPuiObject;

public class EChartsPieOptions implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private List<EChartsPieSeries> series;

	public EChartsPieOptions() {
		this.series = new ArrayList<>();
	}

	public List<EChartsPieSeries> getSeries() {
		return series;
	}

	public void setSeries(List<EChartsPieSeries> series) {
		this.series = series;
	}

}