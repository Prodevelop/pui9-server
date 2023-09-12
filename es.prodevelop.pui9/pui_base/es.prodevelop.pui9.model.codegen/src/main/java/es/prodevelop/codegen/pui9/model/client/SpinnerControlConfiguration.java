package es.prodevelop.codegen.pui9.model.client;

public class SpinnerControlConfiguration extends AbstractExtendedControlConfiguration
		implements IMinMaxControlConfiguration {

	private Integer min = 0;
	private Integer max = 99999;

	@Override
	public Integer getMin() {
		return min;
	}

	@Override
	public void setMin(Integer min) {
		this.min = min;
	}

	@Override
	public Integer getMax() {
		return max;
	}

	@Override
	public void setMax(Integer max) {
		this.max = max;
	}

}
