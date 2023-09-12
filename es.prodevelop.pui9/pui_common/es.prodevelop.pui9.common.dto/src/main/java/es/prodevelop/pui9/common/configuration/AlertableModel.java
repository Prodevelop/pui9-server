package es.prodevelop.pui9.common.configuration;

import java.util.ArrayList;
import java.util.List;

import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.utils.IPuiObject;

public class AlertableModel implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private List<AlertableColumnDefinition> alertableColumns = new ArrayList<>();
	private List<String> idColumns = new ArrayList<>();

	public List<AlertableColumnDefinition> getAlertableColumns() {
		return alertableColumns;
	}

	public void setAlertableColumns(List<AlertableColumnDefinition> alertableColumns) {
		this.alertableColumns = alertableColumns;
	}

	public List<String> getIdColumns() {
		return idColumns;
	}

	public void setIdColumns(List<String> idColumns) {
		this.idColumns = idColumns;
	}

	public class AlertableColumnDefinition implements IPuiObject {

		private static final long serialVersionUID = 1L;

		private String columnName;
		private String columnLabel;
		private String notificationSubject;
		private Boolean notificationContentLink = false;
		private Boolean notifyUserModel = false;
		private FilterGroup filter;

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public String getColumnLabel() {
			return columnLabel;
		}

		public void setColumnLabel(String columnLabel) {
			this.columnLabel = columnLabel;
		}

		public String getNotificationSubject() {
			return notificationSubject;
		}

		public void setNotificationSubject(String notificationSubject) {
			this.notificationSubject = notificationSubject;
		}

		public Boolean isNotificationContentLink() {
			return notificationContentLink;
		}

		public void setNotificationContentLink(Boolean notificationContentLink) {
			this.notificationContentLink = notificationContentLink;
		}

		public Boolean isNotifyUserModel() {
			return notifyUserModel;
		}

		public void setNotifyUserModel(Boolean notifyUserModel) {
			this.notifyUserModel = notifyUserModel;
		}

		public FilterGroup getFilter() {
			return filter;
		}

		public void setFilter(FilterGroup filter) {
			this.filter = filter;
		}

	}

}
