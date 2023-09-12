package es.prodevelop.codegen.pui9.model.client;

public class SwitchControlConfiguration extends AbstractExtendedControlConfiguration
		implements IBooleanControlConfiguration {

	private String trueValue = "1";
	private String falseValue = "0";

	@Override
	public String getTrueValue() {
		return trueValue;
	}

	@Override
	public void setTrueValue(String trueValue) {
		this.trueValue = trueValue;
	}

	@Override
	public String getFalseValue() {
		return falseValue;
	}

	@Override
	public void setFalseValue(String falseValue) {
		this.falseValue = falseValue;
	}

}
