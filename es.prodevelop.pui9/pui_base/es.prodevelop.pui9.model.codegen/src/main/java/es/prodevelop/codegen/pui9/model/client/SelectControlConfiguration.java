package es.prodevelop.codegen.pui9.model.client;

public class SelectControlConfiguration extends AbstractExtendedControlConfiguration
		implements IReadOnlyControlConfiguration {

	private Boolean clearable = true;
	private Boolean multiple = false;
	private Boolean itemTemplate = false;
	private Boolean selectTemplate = false;
	private Boolean readOnly = false;
	private Boolean resultsFromRequest = true;
	private String modelName = "";
	private String referencedAttribute = "";
	private String detailModelName = "";
	private String detailComponentName = "";

	public Boolean getClearable() {
		return clearable;
	}

	public void setClearable(Boolean clearable) {
		this.clearable = clearable;
	}

	public Boolean getMultiple() {
		return multiple;
	}

	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}

	public Boolean getItemTemplate() {
		return itemTemplate;
	}

	public void setItemTemplate(Boolean itemTemplate) {
		this.itemTemplate = itemTemplate;
	}

	public Boolean getSelectTemplate() {
		return selectTemplate;
	}

	public void setSelectTemplate(Boolean selectTemplate) {
		this.selectTemplate = selectTemplate;
	}

	@Override
	public Boolean getReadOnly() {
		return readOnly;
	}

	@Override
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getResultsFromRequest() {
		return resultsFromRequest;
	}

	public void setResultsFromRequest(Boolean resultsFromRequest) {
		this.resultsFromRequest = resultsFromRequest;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getReferencedAttribute() {
		return referencedAttribute;
	}

	public void setReferencedAttribute(String referencedAttribute) {
		this.referencedAttribute = referencedAttribute;
	}

	public String getDetailModelName() {
		return detailModelName;
	}

	public void setDetailModelName(String detailModelName) {
		this.detailModelName = detailModelName;
	}

	public String getDetailComponentName() {
		return detailComponentName;
	}

	public void setDetailComponentName(String detailComponentName) {
		this.detailComponentName = detailComponentName;
	}

}
