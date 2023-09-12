package es.prodevelop.codegen.pui9.model.client;

public class TextControlConfiguration extends AbstractExtendedControlConfiguration
		implements IMaxLengthControlConfiguration, IReadOnlyControlConfiguration {

	private Integer maxlength = null;
	private Boolean readOnly = false;
	private Boolean isPassword = false;

	@Override
	public Integer getMaxlength() {
		return maxlength;
	}

	@Override
	public void setMaxlength(Integer maxlength) {
		this.maxlength = maxlength;
	}

	@Override
	public Boolean getReadOnly() {
		return readOnly;
	}

	@Override
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getIsPassword() {
		return isPassword;
	}

	public void setIsPassword(Boolean isPassword) {
		this.isPassword = isPassword;
	}

}
