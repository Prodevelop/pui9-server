package es.prodevelop.codegen.pui9.model.client;

public class NumberControlConfiguration extends AbstractExtendedControlConfiguration
		implements IMinMaxControlConfiguration {

	private Boolean integer = true;
	private Integer decimals = null;
	private Integer min = 0;
	private Integer max = 99999;

	public Boolean getInteger() {
		return integer;
	}

	public void setInteger(Boolean integer) {
		this.integer = integer;
	}

	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

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
