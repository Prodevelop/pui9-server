package es.prodevelop.codegen.pui9.model.client;

public class MultiSelectControlConfiguration implements IDisabledControlConfiguration {

	private Boolean disabled;
	private String itemsModel = "";
	private String itemValue = "";
	private String itemText = "";
	private String itemDescription = "";

	@Override
	public Boolean getDisabled() {
		return disabled;
	}

	@Override
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getItemsModel() {
		return itemsModel;
	}

	public void setItemsModel(String itemsModel) {
		this.itemsModel = itemsModel;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getItemText() {
		return itemText;
	}

	public void setItemText(String itemText) {
		this.itemText = itemText;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

}
