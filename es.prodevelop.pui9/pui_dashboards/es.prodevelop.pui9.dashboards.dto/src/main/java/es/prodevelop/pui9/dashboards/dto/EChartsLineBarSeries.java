package es.prodevelop.pui9.dashboards.dto;

import java.io.Serializable;
import java.util.List;

public class EChartsLineBarSeries implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private EChartsTypes type;
	private List<String> data;

	public EChartsLineBarSeries(String name, EChartsTypes type, List<String> data) {
		this.name = name;
		this.type = type;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EChartsTypes getType() {
		return type;
	}

	public void setType(EChartsTypes type) {
		this.type = type;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

}