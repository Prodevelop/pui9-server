package es.prodevelop.pui9.dashboards.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EChartsPieSeries implements Serializable {

	private static final long serialVersionUID = 1L;

	private EChartsTypes type = EChartsTypes.pie;
	private List<EChartsPieSeriesData> data;

	public EChartsPieSeries() {
		this.data = new ArrayList<>();
	}

	public EChartsTypes getType() {
		return type;
	}

	public void setType(EChartsTypes type) {
		this.type = type;
	}

	public List<EChartsPieSeriesData> getData() {
		return data;
	}

	public void setData(List<EChartsPieSeriesData> data) {
		this.data = data;
	}

}