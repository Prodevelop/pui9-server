package es.prodevelop.codegen.pui9.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import es.prodevelop.codegen.pui9.enums.ClientControlType;
import es.prodevelop.codegen.pui9.utils.CodegenUtils;

public abstract class Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient PuiConfiguration configuration;
	private transient boolean withGeometry;
	private transient boolean withAutowhere;

	private transient String javaName;
	private transient String lowercaseName;

	private String dbName;
	private List<Column> columns;

	public PuiConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PuiConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean isWithGeometry() {
		return withGeometry;
	}

	public boolean isWithAutowhere() {
		return withAutowhere;
	}

	public void setWithAutowhere(boolean withAutowhere) {
		this.withAutowhere = withAutowhere;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
		computeAttributes();
	}

	public String getJavaName() {
		return javaName;
	}

	public String getLowercaseName() {
		return lowercaseName;
	}

	public List<Column> getColumns() {
		if (columns == null) {
			columns = new ArrayList<>();
		}
		return columns;
	}

	/**
	 * This method adds the new column into the Entity, but if it exists a column
	 * with the same name, it's modified with the values of the given column
	 */
	public void addColumn(Column column) {
		Optional<Column> existColOpt = getColumns().stream().filter(c -> c.getDbName().equals(column.getDbName()))
				.findFirst();

		Column existentColumn = null;

		if (!existColOpt.isPresent()) {
			getColumns().add(column.getPosition(), column);
			existentColumn = column;
		} else {
			existentColumn = existColOpt.get();
			existentColumn.setPk(column.isPk());
			existentColumn.setSequence(column.isSequence());
			existentColumn.setGeometry(column.isGeometry());
			existentColumn.setGeometryType(column.getGeometryType());
			existentColumn.setDbType(column.getDbType());
			existentColumn.setDbRawType(column.getDbRawType());
			existentColumn.setDbDefaultValue(column.getDbDefaultValue());
			existentColumn.setDbSize(column.getDbSize());
			existentColumn.setDbDecimals(column.getDbDecimals());
			existentColumn.setPosition(column.getPosition());
		}

		existentColumn.computeJavaAttributes();
		withGeometry |= existentColumn.isGeometry();

		getColumns().sort((c1, c2) -> Integer.valueOf(c1.getPosition()).compareTo(Integer.valueOf(c2.getPosition())));
	}

	public void mergeColumns(List<Column> columns) {
		getColumns().removeIf(origColumn -> columns.stream()
				.filter(newColumn -> newColumn.getDbName().equals(origColumn.getDbName())).count() == 0);
		columns.forEach(this::addColumn);
	}

	public void removeWrongColumns(List<String> validColumns) {
		getColumns().removeIf(column -> !validColumns.contains(column.getDbName()));
	}

	public void reorderColumns() {
		Collections.sort(getColumns());
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Column> getColumnsOfType(ClientControlType clientControlType) {
		return getColumnsOfType(clientControlType.name());
	}

	public List<Column> getColumnsOfType(String clientControlType) {
		ClientControlType type = ClientControlType.valueOf(clientControlType);
		return columns.stream().filter(column -> column.isClientInForm() && column.getClientControlType().equals(type))
				.collect(Collectors.toList());
	}

	public void computeAttributes() {
		setJavaName();
	}

	private void setJavaName() {
		javaName = CodegenUtils.getJavaName(dbName);
		lowercaseName = javaName.toLowerCase();
	}

	public String checkColumnsType() {
		StringBuilder sb = new StringBuilder();

		for (Column column : columns) {
			if (!column.getJavaType().equals(JavaAttributeType.DATETIME)) {
				continue;
			}

			switch (configuration.getDatabase().getType()) {
			case ORACLE:
				if (!column.getDbType().equals(DatabaseColumnType.TIMESTAMP_TIMEZONE)) {
					sb.append("Column '" + column.getDbName() + "' DB type is " + column.getDbType() + "\n");
				}
				break;
			case POSTGRE_SQL:
				if (column.getDbType().equals(DatabaseColumnType.DATE)
						|| (column.getDbType().equals(DatabaseColumnType.TIMESTAMP))) {
					sb.append("Column '" + column.getDbName() + "' DB type is " + column.getDbType() + "\n");
				}
				break;
			case SQL_SERVER:
				if (column.getDbType().equals(DatabaseColumnType.DATE)
						|| (column.getDbType().equals(DatabaseColumnType.TIMESTAMP))) {
					sb.append("Column '" + column.getDbName() + "' DB type is " + column.getDbType() + "\n");
				}
				break;
			}
		}

		if (!sb.toString().isEmpty()) {
			switch (configuration.getDatabase().getType()) {
			case ORACLE:
				sb.append("\nBetter change them to TIMESTAMP WITH TIME ZONE");
				break;
			case POSTGRE_SQL:
				sb.append("\nBetter change them to TIMESTAMPTZ(3)");
				break;
			case SQL_SERVER:
				sb.append("\nBetter change them to DATETIME");
				break;
			}
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		return "DB name: " + dbName + "\nJava name: " + javaName + "\nWith geometry: " + withGeometry;
	}

}
