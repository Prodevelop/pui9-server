package es.prodevelop.codegen.pui9.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import es.prodevelop.codegen.pui9.enums.EntityTypeEnum;
import es.prodevelop.codegen.pui9.model.Column;
import es.prodevelop.codegen.pui9.model.ColumnVisibility;
import es.prodevelop.codegen.pui9.model.DatabaseColumnType;
import es.prodevelop.codegen.pui9.model.DatabaseType;
import es.prodevelop.codegen.pui9.model.Entity;
import es.prodevelop.codegen.pui9.model.GeometryType;
import es.prodevelop.codegen.pui9.model.Table;
import es.prodevelop.codegen.pui9.model.View;

public class CodegenModelsDatabaseUtils {

	private static CodegenModelsDatabaseUtils singleton;

	public static CodegenModelsDatabaseUtils singleton(Connection conn) {
		singleton = new CodegenModelsDatabaseUtils(conn);
		return singleton;
	}

	public static CodegenModelsDatabaseUtils singleton() {
		if (singleton == null) {
			throw new IllegalStateException(
					"You should call singleton method with Model on first call in order to create the conection");
		}
		return singleton;
	}

	private Connection conn;
	private DatabaseType dbType;
	private Map<String, List<String>> triggersMap = new LinkedHashMap<>();

	private CodegenModelsDatabaseUtils(Connection conn) {
		this.conn = conn;
		try {
			dbType = DatabaseType.getByOfficialName(conn.getMetaData().getDatabaseProductName());
		} catch (SQLException e) {
			// do nothing
		}
	}

	public List<String> loadTables() {
		return loadEntities(EntityTypeEnum.TABLE);
	}

	public List<String> loadViews() {
		return loadEntities(EntityTypeEnum.VIEW);
	}

	private List<String> loadEntities(EntityTypeEnum entityType) {
		List<String> list = new ArrayList<>();

		try (ResultSet rs = conn.getMetaData().getTables(null, conn.getSchema(), "%",
				new String[] { entityType.name() })) {
			while (rs.next()) {
				list.add(rs.getString(EntityMetadataEnum.TABLE_NAME.name()));
			}
		} catch (SQLException e) {
			// do nothing
		}

		return list;
	}

	public boolean loadTable(Table table) {
		boolean exists = modifyRealTableName(table, EntityTypeEnum.TABLE);
		if (!exists) {
			return false;
		}

		loadColumns(table);
		loadPrimaryKeys(table);
		return true;
	}

	public boolean loadView(View view) {
		boolean exists = modifyRealTableName(view, EntityTypeEnum.VIEW);
		if (!exists) {
			return false;
		}

		loadColumns(view);
		return true;
	}

	private void loadColumns(Entity entity) {
		try (ResultSet rs = conn.getMetaData().getColumns(null, conn.getSchema(), entity.getDbName(), "%")) {
			List<String> columns = new ArrayList<>();
			while (rs.next()) {
				Column column = new Column();
				column.setEntity(entity);
				column.setDbName(rs.getString(ColumnMetadataEnum.COLUMN_NAME.name()));
				column.setDbType(DatabaseColumnType.getByTypeId(rs.getInt(ColumnMetadataEnum.DATA_TYPE.name())));
				if (column.getDbType() == null) {
					continue;
				}
				column.setDbRawType(rs.getString(ColumnMetadataEnum.TYPE_NAME.name()));
				column.setDbDefaultValue(rs.getString(ColumnMetadataEnum.COLUMN_DEF.name()));
				column.setDbSize(rs.getInt(ColumnMetadataEnum.COLUMN_SIZE.name()));
				column.setDbDecimals(rs.getInt(ColumnMetadataEnum.DECIMAL_DIGITS.name()));
				column.setNullable(rs.getInt(ColumnMetadataEnum.NULLABLE.name()) == DatabaseMetaData.columnNullable);
				column.setPosition(rs.getInt(ColumnMetadataEnum.ORDINAL_POSITION.name()) - 1);
				column.setPk(false);
				column.setColumnVisibility(ColumnVisibility.visible);
				if (column.getDbName().equalsIgnoreCase("lang")) {
					column.setColumnVisibility(ColumnVisibility.completelyhidden);
				}
				column.setAutoincrementable(false);
				column.setGeometry(
						dbType.geometryColumnNames.stream().anyMatch(gcm -> column.getDbRawType().contains(gcm)));

				boolean isDbAutoinc = rs.getBoolean(ColumnMetadataEnum.IS_AUTOINCREMENT.name());
				switch (dbType) {
				case ORACLE:
					column.setGeometryType(GeometryType.NONE);
					column.setSequence(
							isDbAutoinc || lookForOracleTriggerForColumn(entity.getDbName(), column.getDbName()));
					break;
				case POSTGRE_SQL:
					if (column.getDbType().equals(DatabaseColumnType.TIMESTAMP) && column.getDbDecimals() == 3) {
						column.setDbType(DatabaseColumnType.TIMESTAMP_LOCAL_TIMEZONE);
					}
					if (column.isGeometry()) {
						column.setGeometryType(lookForGeometryTypePostgresql(entity.getDbName(), column.getDbName()));
					} else {
						column.setGeometryType(GeometryType.NONE);
					}
					column.setSequence(isDbAutoinc || column.getDbRawType().toLowerCase().contains("serial"));
					break;
				case SQL_SERVER:
					if (column.getDbType().equals(DatabaseColumnType.TIMESTAMP)) {
						if (column.getDbDecimals() == 3) {
							column.setDbType(DatabaseColumnType.TIMESTAMP_LOCAL_TIMEZONE);
						} else {
							column.setDbType(DatabaseColumnType.DATE);
						}
					}
					column.setGeometryType(GeometryType.NONE);
					column.setSequence(isDbAutoinc || column.getDbRawType().toLowerCase().contains("identity"));
					break;
				}

				entity.addColumn(column);
				columns.add(column.getDbName());
			}
			entity.removeWrongColumns(columns);
			entity.reorderColumns();
		} catch (SQLException e) {
			// do nothing
		}
	}

	private boolean lookForOracleTriggerForColumn(String tableName, String columnName) {
		if (!triggersMap.containsKey(tableName)) {
			triggersMap.put(tableName, new ArrayList<>());

			StringBuilder sql = new StringBuilder();
			sql.append("select * ");
			sql.append("from user_triggers ");
			sql.append("where lower(table_name) = lower(?) ");
			try (PreparedStatement stm = conn.prepareStatement(sql.toString())) {
				stm.setString(1, tableName);
				try (ResultSet rs = stm.executeQuery()) {
					while (rs.next()) {
						String triggerBody = rs.getString(EntityMetadataEnum.TRIGGER_BODY.name());
						if (!StringUtils.isEmpty(triggerBody)) {
							// move to lowercase
							triggerBody = triggerBody.toLowerCase();
							// remove break lines
							triggerBody = triggerBody.replace(System.getProperty("line.separator"), "");
							// convert 2 or more spaces with a single one
							triggerBody = triggerBody.trim().replaceAll(" +", " ");
							triggersMap.get(tableName).add(triggerBody);
						}
					}
				}
			} catch (SQLException e) {
				return false;
			}
		}

		for (String triggerBody : triggersMap.get(tableName)) {
			if (triggerBody.contains(".nextval into :new." + columnName.toLowerCase() + " from dual")) {
				return true;
			}
		}

		return false;
	}

	private GeometryType lookForGeometryTypePostgresql(String table, String column) {
		StringBuilder sql = new StringBuilder();
		sql.append("select type ");
		sql.append("from public.geometry_columns ");
		sql.append("where f_table_name = ? ");
		sql.append("and f_geometry_column = ?");
		try (PreparedStatement stm = conn.prepareStatement(sql.toString())) {
			stm.setString(1, table);
			stm.setString(2, column);
			try (ResultSet rs = stm.executeQuery()) {
				while (rs.next()) {
					String type = rs.getString(1);
					try {
						return GeometryType.valueOf(type);
					} catch (Exception e) {
						return GeometryType.NONE;
					}
				}
			} catch (SQLException e) {
				return GeometryType.NONE;
			}
			return null;
		} catch (SQLException e) {
			return GeometryType.NONE;
		}
	}

	private void loadPrimaryKeys(Table table) {
		try (ResultSet rs = conn.getMetaData().getPrimaryKeys(null, conn.getSchema(), table.getDbName())) {
			while (rs.next()) {
				table.addPrimaryKeyName(rs.getString(ColumnMetadataEnum.COLUMN_NAME.name()));
			}
		} catch (SQLException e) {
			// do nothing
		}
	}

	/**
	 * Return if the entity exists or not
	 * 
	 * @param entity The entity (table or view)
	 * @return true if exists, false if not
	 */
	private boolean modifyRealTableName(Entity entity, EntityTypeEnum entityType) {
		if (existsTable(entity.getDbName(), entityType)) {
			return true;
		}
		if (existsTable(entity.getDbName().toLowerCase(), entityType)) {
			entity.setDbName(entity.getDbName().toLowerCase());
			return true;
		}
		if (existsTable(entity.getDbName().toUpperCase(), entityType)) {
			entity.setDbName(entity.getDbName().toUpperCase());
			return true;
		}
		return false;
	}

	private boolean existsTable(String entityName, EntityTypeEnum entityType) {
		try (ResultSet rs = conn.getMetaData().getTables(null, conn.getSchema(), entityName,
				new String[] { entityType.name() })) {
			return rs.next();
		} catch (SQLException e) {
			return false;
		}
	}

	private enum EntityMetadataEnum {
		TABLE_NAME,

		TRIGGER_BODY;
	}

	private enum ColumnMetadataEnum {
		COLUMN_NAME,

		DATA_TYPE,

		COLUMN_SIZE,

		TYPE_NAME,

		DECIMAL_DIGITS,

		COLUMN_DEF,

		NULLABLE,

		ORDINAL_POSITION,

		IS_AUTOINCREMENT;
	}

}
