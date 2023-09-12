package es.prodevelop.codegen.pui9.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PuiConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient String modelName = "";
	private transient Table selectedTable;
	private transient View selectedView;
	private transient File file;

	private String projectId;
	private String pui9Version;
	private DatabaseConnection database;
	private ServerConfiguration server;
	private ClientConfiguration client;
	private List<Table> tables;
	private List<View> views;

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;

		if (!modelName.isEmpty()) {
			server.setReadFunctionality("READ_" + modelName.toUpperCase());
			server.setWriteFunctionality("WRITE_" + modelName.toUpperCase());
			server.setInsertFunctionality("INSERT_" + modelName.toUpperCase());
			server.setUpdateFunctionality("UPDATE_" + modelName.toUpperCase());
			server.setDeleteFunctionality("DELETE_" + modelName.toUpperCase());
			server.setGetFunctionality("GET_" + modelName.toUpperCase());
			server.setListFunctionality("LIST_" + modelName.toUpperCase());
		} else {
			server.setReadFunctionality("");
			server.setWriteFunctionality("");
			server.setInsertFunctionality("");
			server.setUpdateFunctionality("");
			server.setDeleteFunctionality("");
			server.setGetFunctionality("");
			server.setListFunctionality("");
		}
	}

	public Table getSelectedTable() {
		return selectedTable;
	}

	public void setSelectedTable(Table selectedTable) {
		this.selectedTable = selectedTable;
	}

	public View getSelectedView() {
		return selectedView;
	}

	public void setSelectedView(View selectedView) {
		this.selectedView = selectedView;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Table findTable(String name) {
		for (Table table : tables) {
			if (table.getDbName().equals(name)) {
				return table;
			}
		}
		return null;
	}

	public View findView(String name) {
		for (View view : views) {
			if (view.getDbName().equals(name)) {
				return view;
			}
		}
		return null;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getPui9Version() {
		return pui9Version;
	}

	public void setPui9Version(String pui9Version) {
		this.pui9Version = pui9Version;
	}

	public DatabaseConnection getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseConnection database) {
		this.database = database;
	}

	public ServerConfiguration getServer() {
		return server;
	}

	public void setServer(ServerConfiguration server) {
		this.server = server;
	}

	public ClientConfiguration getClient() {
		return client;
	}

	public void setClient(ClientConfiguration client) {
		this.client = client;
	}

	public List<Table> getTables() {
		if (tables == null) {
			tables = new ArrayList<>();
		}
		return tables;
	}

	public void addTable(Table table) {
		getTables().add(table);
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public List<View> getViews() {
		if (views == null) {
			views = new ArrayList<>();
		}
		return views;
	}

	public void addView(View view) {
		getViews().add(view);
	}

	public void setViews(List<View> views) {
		this.views = views;
	}

	@Override
	public String toString() {
		return "Project ID: " + projectId + "\nPUI9 Version: " + pui9Version + "\nDatabase: " + database
				+ (selectedTable != null ? "\n\nTable:\n" + selectedTable : "")
				+ (selectedView != null ? "\n\nView:\n" + selectedView : "");
	}

}
