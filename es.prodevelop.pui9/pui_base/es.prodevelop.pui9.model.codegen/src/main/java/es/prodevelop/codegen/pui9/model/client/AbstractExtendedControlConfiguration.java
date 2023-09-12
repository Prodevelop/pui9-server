package es.prodevelop.codegen.pui9.model.client;

public abstract class AbstractExtendedControlConfiguration
		implements IRequiredControlConfiguration, IExtendedControlConfiguration {

	private Boolean required = false;
	private Boolean disabled;
	private Boolean editable = true;
	private Boolean topLabel = true;

	@Override
	public Boolean getRequired() {
		return required;
	}

	@Override
	public void setRequired(Boolean required) {
		this.required = required;
	}

	@Override
	public Boolean getDisabled() {
		return disabled;
	}

	@Override
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public Boolean getEditable() {
		return editable;
	}

	@Override
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	@Override
	public Boolean getTopLabel() {
		return topLabel;
	}

	@Override
	public void setTopLabel(Boolean topLabel) {
		this.topLabel = topLabel;
	}

}
