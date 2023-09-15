package es.prodevelop.pui9.common.configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.utils.IPuiObject;

public class ModelConfiguration implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private Boolean isdefault = false;
	private String label; // for alerts
	private String name; // for user configurations
	private String description; // for user configurations
	private List<Order> order;
	private List<FilterCombo> filterCombo;
	private List<Columns> columns;
	private Boolean pinColumn = false;
	private Boolean grouped = false;
	private Boolean documentsShowRole = false;
	private List<String> documentsRoles;
	private Boolean documentsShowLanguage = false;
	private Boolean actionImportExport = false;
	private Boolean actionCopy = false;
	private Integer refreshSeconds;
	private AlertableModel alertable;
	private Map<String, Map<String, String>> filterColumnValues;
	private String extra;

	public Boolean isIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Boolean isdefault) {
		this.isdefault = isdefault;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Order> getOrder() {
		return order != null ? order : Collections.emptyList();
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

	public List<FilterCombo> getFilterCombo() {
		return filterCombo != null ? filterCombo : Collections.emptyList();
	}

	public void setFilterCombo(List<FilterCombo> filterCombo) {
		this.filterCombo = filterCombo;
	}

	public List<Columns> getColumns() {
		return columns != null ? columns : Collections.emptyList();
	}

	public void setColumns(List<Columns> columns) {
		this.columns = columns;
	}

	public Boolean isPinColumn() {
		return pinColumn;
	}

	public void setPinColumn(Boolean pinColumn) {
		this.pinColumn = pinColumn;
	}

	public Boolean isGrouped() {
		return grouped;
	}

	public void setGrouped(Boolean grouped) {
		this.grouped = grouped;
	}

	public Boolean isDocumentsShowRole() {
		return documentsShowRole;
	}

	public void setDocumentsShowRole(Boolean documentsShowRole) {
		this.documentsShowRole = documentsShowRole;
	}

	public List<String> getDocumentsRoles() {
		return documentsRoles != null ? documentsRoles : Collections.emptyList();
	}

	public void setDocumentsRoles(List<String> documentsRoles) {
		this.documentsRoles = documentsRoles;
	}

	public Boolean isDocumentsShowLanguage() {
		return documentsShowLanguage;
	}

	public void setDocumentsShowLanguage(Boolean documentsShowLanguage) {
		this.documentsShowLanguage = documentsShowLanguage;
	}

	public Boolean isActionImportExport() {
		return actionImportExport;
	}

	public void setActionImportExport(Boolean actionImportExport) {
		this.actionImportExport = actionImportExport;
	}

	public Boolean isActionCopy() {
		return actionCopy;
	}

	public void setActionCopy(Boolean actionCopy) {
		this.actionCopy = actionCopy;
	}

	public Integer getRefreshSeconds() {
		return refreshSeconds;
	}

	public void setRefreshSeconds(Integer refreshSeconds) {
		this.refreshSeconds = refreshSeconds;
	}

	public AlertableModel getAlertable() {
		return alertable;
	}

	public void setAlertable(AlertableModel alertable) {
		this.alertable = alertable;
	}

	public Map<String, Map<String, String>> getFilterColumnValues() {
		return filterColumnValues;
	}

	public void setFilterColumnValues(Map<String, Map<String, String>> filterColumnValues) {
		this.filterColumnValues = filterColumnValues;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

}
