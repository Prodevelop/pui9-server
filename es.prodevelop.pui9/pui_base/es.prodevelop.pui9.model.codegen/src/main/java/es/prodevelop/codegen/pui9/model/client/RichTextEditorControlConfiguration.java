package es.prodevelop.codegen.pui9.model.client;

public class RichTextEditorControlConfiguration
		implements IRequiredControlConfiguration, IValidationErrorsControlConfiguration {

	private Boolean required = false;
	private Boolean validationErrors;

	@Override
	public Boolean getRequired() {
		return required;
	}

	@Override
	public void setRequired(Boolean required) {
		this.required = required;
	}

	@Override
	public Boolean getValidationErrors() {
		return validationErrors;
	}

	@Override
	public void setValidationErrors(Boolean validationErrors) {
		this.validationErrors = validationErrors;
	}

}
