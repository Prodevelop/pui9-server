package es.prodevelop.codegen.pui9.model.client;

import java.util.ArrayList;
import java.util.List;

import es.prodevelop.codegen.pui9.model.client.extra.RadioItem;

public class RadioGroupControlConfiguration extends AbstractExtendedControlConfiguration {

	private boolean asRow = true;
	private List<RadioItem> items = new ArrayList<>();

	public boolean isAsRow() {
		return asRow;
	}

	public void setAsRow(boolean asRow) {
		this.asRow = asRow;
	}

	public List<RadioItem> getItems() {
		return items;
	}

	public void setItems(List<RadioItem> items) {
		this.items = items;
	}

}
