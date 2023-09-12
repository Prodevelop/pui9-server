package es.prodevelop.codegen.pui9.model.client;

import es.prodevelop.codegen.pui9.enums.CodeEditorValues;

public class CodeEditorControlConfiguration
		implements IRequiredControlConfiguration, IValidationErrorsControlConfiguration {

	private Boolean required = false;
	private CodeEditorValues mode = CodeEditorValues.JSON;
	private Boolean validationErrors;

	@Override
	public Boolean getRequired() {
		return required;
	}

	@Override
	public void setRequired(Boolean required) {
		this.required = required;
	}

	public CodeEditorValues getMode() {
		return mode;
	}

	public void setMode(CodeEditorValues mode) {
		this.mode = mode;
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
