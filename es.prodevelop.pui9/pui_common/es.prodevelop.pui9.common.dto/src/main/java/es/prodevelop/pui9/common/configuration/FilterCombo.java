package es.prodevelop.pui9.common.configuration;

import java.util.Map;

import es.prodevelop.pui9.order.OrderDirection;
import es.prodevelop.pui9.utils.IPuiObject;

public class FilterCombo implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private String id;
	private FilterComboSearch search;
	private FilterComboLocal local;
	private FilterComboRelatedCombo relatedCombo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FilterComboSearch getSearch() {
		return search;
	}

	public void setSearch(FilterComboSearch search) {
		this.search = search;
	}

	public FilterComboLocal getLocal() {
		return local;
	}

	public void setLocal(FilterComboLocal local) {
		this.local = local;
	}

	public FilterComboRelatedCombo getRelatedCombo() {
		return relatedCombo;
	}

	public void setRelatedCombo(FilterComboRelatedCombo relatedCombo) {
		this.relatedCombo = relatedCombo;
	}

	public class FilterComboSearch implements IPuiObject {

		private static final long serialVersionUID = 1L;

		private String model;
		private String value;
		private String text;
		private Map<String, OrderDirection> order;
		private String related;

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public Map<String, OrderDirection> getOrder() {
			return order;
		}

		public void setOrder(Map<String, OrderDirection> order) {
			this.order = order;
		}

		public String getRelated() {
			return related;
		}

		public void setRelated(String related) {
			this.related = related;
		}

	}

	public class FilterComboLocal implements IPuiObject {

		private static final long serialVersionUID = 1L;

		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	public class FilterComboRelatedCombo implements IPuiObject {

		private static final long serialVersionUID = 1L;

		private String id;
		private String toColumn;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getToColumn() {
			return toColumn;
		}

		public void setToColumn(String toColumn) {
			this.toColumn = toColumn;
		}

	}

}
