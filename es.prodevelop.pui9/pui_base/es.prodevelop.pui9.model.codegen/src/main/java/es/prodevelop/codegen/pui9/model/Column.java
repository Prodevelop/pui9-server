package es.prodevelop.codegen.pui9.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import es.prodevelop.codegen.pui9.enums.ClientControlType;
import es.prodevelop.codegen.pui9.model.client.IControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.IMaxLengthControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.IRequiredControlConfiguration;
import es.prodevelop.codegen.pui9.utils.CodegenUtils;

public class Column implements Serializable, Comparable<Column> {

	private static final long serialVersionUID = 1L;

	private transient Entity entity;
	private transient String javaTypeString;
	private transient Integer javaSize;

	private transient boolean pk;
	private transient boolean sequence;
	private transient boolean geometry;
	private transient GeometryType geometryType;
	private transient DatabaseColumnType dbType;
	private transient String dbRawType;
	private transient String dbDefaultValue;
	private transient Integer dbSize;
	private transient Integer dbDecimals;
	private transient String javaName;
	private transient JavaAttributeType javaType;
	private transient JavaAttributeType javaSubType;
	private transient ColumnType columnType;
	private transient boolean lang;

	private transient String clientTranslation = "";
	private transient boolean clientInForm = true;
	private transient boolean clientInHeader = false;
	private transient ClientControlType clientControlType;
	private transient IControlConfiguration controlConfiguration;

	private String dbName;
	private String javaClassName;
	private ColumnVisibility columnVisibility;
	private boolean nullable;
	private boolean autoincrementable;
	private String autowhere;
	private String javaDefaultValue;
	private int position;

	public Column() {
		setClientControlType(ClientControlType.TEXT);
		try {
			setControlConfiguration(clientControlType.configClass.getDeclaredConstructor().newInstance());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// do nothing
		}
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public String getJavaTypeString() {
		return javaTypeString;
	}

	public Integer getJavaSize() {
		return javaSize;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName.toLowerCase();
	}

	public String getJavaClassName() {
		return javaClassName;
	}

	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}

	public DatabaseColumnType getDbType() {
		return dbType;
	}

	public void setDbType(DatabaseColumnType dbType) {
		this.dbType = dbType;
	}

	public String getDbRawType() {
		return dbRawType;
	}

	public void setDbRawType(String dbRawType) {
		this.dbRawType = dbRawType;
	}

	public String getDbDefaultValue() {
		return dbDefaultValue;
	}

	public void setDbDefaultValue(String dbDefaultValue) {
		if (dbDefaultValue == null) {
			dbDefaultValue = "";
		}
		this.dbDefaultValue = dbDefaultValue;
	}

	public Integer getDbSize() {
		return dbSize;
	}

	public void setDbSize(Integer dbSize) {
		if (dbSize == null) {
			dbSize = -1;
		}
		if (DatabaseColumnType.CLOB.equals(dbType)) {
			dbSize = -1;
		}
		this.dbSize = dbSize;
		if (this.controlConfiguration instanceof IMaxLengthControlConfiguration) {
			((IMaxLengthControlConfiguration) this.controlConfiguration).setMaxlength(dbSize != null ? dbSize : null);
		}
	}

	public Integer getDbDecimals() {
		return dbDecimals;
	}

	public void setDbDecimals(Integer dbDecimals) {
		if (dbDecimals == null) {
			dbDecimals = 0;
		}
		this.dbDecimals = dbDecimals;
	}

	public String getJavaName() {
		return javaName;
	}

	public JavaAttributeType getJavaType() {
		return javaType;
	}

	public JavaAttributeType getJavaSubType() {
		return javaSubType;
	}

	public ColumnType getColumnType() {
		return columnType;
	}

	public String getJavaDefaultValue() {
		return javaDefaultValue;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public ColumnVisibility getColumnVisibility() {
		return columnVisibility;
	}

	public void setColumnVisibility(ColumnVisibility columnVisibility) {
		this.columnVisibility = columnVisibility;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
		if (this.controlConfiguration instanceof IRequiredControlConfiguration) {
			((IRequiredControlConfiguration) this.controlConfiguration).setRequired(!nullable);
		}
	}

	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}

	public boolean isAutoincrementable() {
		return autoincrementable;
	}

	public void setAutoincrementable(boolean autoincrementable) {
		this.autoincrementable = autoincrementable;
	}

	public String getAutowhere() {
		return autowhere;
	}

	public void setAutowhere(String autowhere) {
		this.autowhere = autowhere;
		entity.setWithAutowhere(entity.isWithAutowhere() || !StringUtils.isEmpty(autowhere));
	}

	public boolean isSequence() {
		return sequence;
	}

	public void setSequence(boolean sequence) {
		this.sequence = sequence;
		if (sequence) {
			javaDefaultValue = "";
		}
	}

	public boolean isGeometry() {
		return geometry;
	}

	public void setGeometry(boolean geometry) {
		this.geometry = geometry;
	}

	public GeometryType getGeometryType() {
		return geometryType;
	}

	public void setGeometryType(GeometryType geometryType) {
		this.geometryType = geometryType;
	}

	public boolean isLang() {
		return lang;
	}

	public void setLang(boolean lang) {
		this.lang = lang;
	}

	public String getClientTranslation() {
		return clientTranslation;
	}

	public void setClientTranslation(String clientTranslation) {
		this.clientTranslation = clientTranslation;
	}

	public boolean isClientInForm() {
		return clientInForm;
	}

	public void setClientInForm(boolean clientInForm) {
		this.clientInForm = clientInForm;
	}

	public boolean isClientInHeader() {
		return clientInHeader;
	}

	public void setClientInHeader(boolean clientInHeader) {
		this.clientInHeader = clientInHeader;
	}

	public ClientControlType getClientControlType() {
		return clientControlType;
	}

	public void setClientControlType(ClientControlType clientControlType) {
		this.clientControlType = clientControlType;
	}

	public IControlConfiguration getControlConfiguration() {
		return controlConfiguration;
	}

	public void setControlConfiguration(IControlConfiguration controlConfiguration) {
		this.controlConfiguration = controlConfiguration;
		if (this.controlConfiguration instanceof IMaxLengthControlConfiguration) {
			((IMaxLengthControlConfiguration) this.controlConfiguration).setMaxlength(dbSize != null ? dbSize : null);
		}
		if (this.controlConfiguration instanceof IRequiredControlConfiguration) {
			((IRequiredControlConfiguration) this.controlConfiguration).setRequired(!nullable);
		}
	}

	/**
	 * Before calling this method, take into accound these conditions:
	 * <p>
	 * <ul>
	 * <li>All the non Java properties are set</li>
	 * <li>Add the column to its Entity</li>
	 * </ul>
	 */
	public void computeJavaAttributes() {
		setAutowhere(autowhere);
		setJavaName();
		setJavaType();
		setJavaDefaultValue();
	}

	private void setJavaName() {
		this.javaName = CodegenUtils.convertColumnNameToJavaName(dbName);
	}

	private void setJavaType() {
		if (this.dbType == null || StringUtils.isEmpty(this.dbRawType)) {
			return;
		}

		switch (dbType) {
		case ARRAY:
			javaType = JavaAttributeType.LIST;
			columnType = ColumnType.text;
			if (dbRawType.contains(DatabaseColumnType.VARCHAR.name().toLowerCase())
					|| dbRawType.contains(DatabaseColumnType.LONGVARCHAR.name().toLowerCase())
					|| dbRawType.contains(DatabaseColumnType.TEXT.name().toLowerCase())
					|| dbRawType.contains(DatabaseColumnType.CLOB.name().toLowerCase())) {
				javaSubType = JavaAttributeType.STRING;
			} else if (dbRawType.contains(DatabaseColumnType.CHAR.name().toLowerCase())) {
				javaSubType = JavaAttributeType.CHARACTER;
			} else if (dbRawType.contains(DatabaseColumnType.BIT.name().toLowerCase())) {
				javaSubType = JavaAttributeType.BOOLEAN;
			} else if (dbRawType.contains(DatabaseColumnType.TINYINT.name().toLowerCase())) {
				javaSubType = JavaAttributeType.BYTE;
			} else if (dbRawType.contains(DatabaseColumnType.INTEGER.name().toLowerCase())
					|| dbRawType.contains(DatabaseColumnType.INT.name().toLowerCase())) {
				javaSubType = JavaAttributeType.INTEGER;
			} else if (dbRawType.contains(DatabaseColumnType.BIGINT.name().toLowerCase())) {
				javaSubType = JavaAttributeType.LONG;
			} else if (dbRawType.contains(DatabaseColumnType.REAL.name().toLowerCase())
					|| dbRawType.contains(DatabaseColumnType.DOUBLE.name().toLowerCase())
					|| dbRawType.contains(DatabaseColumnType.DECIMAL.name().toLowerCase())
					|| dbRawType.contains(DatabaseColumnType.NUMERIC.name().toLowerCase())
					|| dbRawType.contains(DatabaseColumnType.FLOAT.name().toLowerCase())) {
				javaSubType = JavaAttributeType.BIGDECIMAL;
			} else if (dbRawType.contains(DatabaseColumnType.DATE.name().toLowerCase())
					|| dbRawType.contains(DatabaseColumnType.TIME.name().toLowerCase())
					|| dbRawType.contains(DatabaseColumnType.TIMESTAMP.name().toLowerCase())) {
				javaSubType = JavaAttributeType.DATETIME;
			}
			break;
		case CHAR:
		case NCHAR:
			javaType = dbSize <= 1 ? JavaAttributeType.CHARACTER : JavaAttributeType.STRING;
			columnType = ColumnType.text;
			break;
		case VARCHAR:
		case NVARCHAR:
		case LONGVARCHAR:
		case CLOB:
		case BLOB:
		case DISTINCT:
		case STRUCT:
		case VARBINARY:
		case TEXT:
			javaType = JavaAttributeType.STRING;
			columnType = ColumnType.text;
			break;
		case OTHER:
			javaType = JavaAttributeType.OBJECT;
			if (geometry) {
				columnType = ColumnType.text;
			} else {
				columnType = ColumnType.json;
			}
			break;
		case BIT:
		case BOOLEAN:
			javaType = JavaAttributeType.BOOLEAN;
			columnType = ColumnType.logic;
			break;
		case TINYINT:
			javaType = JavaAttributeType.BYTE;
			columnType = ColumnType.text;
			break;
		case SMALLINT:
			javaType = JavaAttributeType.SHORT;
			columnType = ColumnType.numeric;
			break;
		case INTEGER:
		case INT:
			javaType = JavaAttributeType.INTEGER;
			columnType = ColumnType.numeric;
			break;
		case BIGINT:
			javaType = JavaAttributeType.LONG;
			columnType = ColumnType.numeric;
			break;
		case NUMERIC:
		case DECIMAL:
			if (dbDecimals == 0) {
				// in case the column is created as Integer type, it normally has 38 of length
				javaType = dbSize <= 22 || dbSize == 38 ? JavaAttributeType.INTEGER : JavaAttributeType.LONG;
				columnType = ColumnType.numeric;
			} else {
				javaType = JavaAttributeType.BIGDECIMAL;
				columnType = ColumnType.decimal;
			}
			break;
		case REAL:
		case DOUBLE:
		case FLOAT:
			javaType = JavaAttributeType.BIGDECIMAL;
			columnType = ColumnType.decimal;
			break;
		case DATE:
		case TIME:
		case TIMESTAMP:
		case TIMESTAMP_TIMEZONE:
		case TIMESTAMP_LOCAL_TIMEZONE:
			javaType = JavaAttributeType.DATETIME;
			columnType = ColumnType.datetime;
			break;
		default:
			javaType = JavaAttributeType.OBJECT;
			columnType = ColumnType.text;
			break;
		}

		if (dbRawType.toLowerCase().startsWith("timestamp")) {
			javaType = JavaAttributeType.DATETIME;
			columnType = ColumnType.datetime;
		}

		javaTypeString = (javaType.clazz.getName().contains("java.lang") ? javaType.clazz.getSimpleName()
				: javaType.clazz.getName()) + (javaSubType == null ? "" : "<" + javaSubType.clazz.getName() + ">");

		javaSize = javaType.equals(JavaAttributeType.STRING) ? dbSize : -1;
	}

	private void setJavaDefaultValue() {
		if (javaDefaultValue != null) {
			return;
		}

		if (StringUtils.isEmpty(dbDefaultValue) || dbDefaultValue.equalsIgnoreCase("null")
				|| dbDefaultValue.equalsIgnoreCase("(null)")) {
			javaDefaultValue = "";
			return;
		}

		String pattern = "[^\\w\\s\\-]";

		switch (dbType) {
		case CHAR:
		case NCHAR:
			javaDefaultValue = dbDefaultValue;
			break;
		case VARCHAR:
		case NVARCHAR:
		case LONGVARCHAR:
		case CLOB:
		case BLOB:
		case OTHER:
		case DISTINCT:
		case STRUCT:
		case VARBINARY:
			if (dbDefaultValue.contains("\'")) {
				javaDefaultValue = "\""
						+ dbDefaultValue.substring(dbDefaultValue.indexOf('\'') + 1, dbDefaultValue.lastIndexOf('\''))
						+ "\"";
			} else {
				javaDefaultValue = "\"" + dbDefaultValue + "\"";
			}
			break;
		case BIT:
		case BOOLEAN:
			javaDefaultValue = dbDefaultValue.trim();
			break;
		case TINYINT:
			javaDefaultValue = "new " + Byte.class.getSimpleName() + "(\""
					+ dbDefaultValue.replaceAll(pattern, "").trim() + "\")";
			break;
		case SMALLINT:
			javaDefaultValue = dbDefaultValue.replaceAll(pattern, "").trim();
			break;
		case INTEGER:
			javaDefaultValue = dbDefaultValue.replaceAll(pattern, "").trim();
			break;
		case BIGINT:
			javaDefaultValue = dbDefaultValue.replaceAll(pattern, "").trim() + "L";
			break;
		case NUMERIC:
		case DECIMAL:
			String val = dbDefaultValue.replaceAll(pattern, "").trim();
			if (JavaAttributeType.INTEGER.equals(javaType)) {
				javaDefaultValue = val;
			} else if (JavaAttributeType.LONG.equals(javaType)) {
				javaDefaultValue = val + "L";
			} else if (JavaAttributeType.BIGDECIMAL.equals(javaType)) {
				javaDefaultValue = "new " + BigDecimal.class.getName() + "(\"" + val + "\")";
			}
			break;
		case REAL:
		case DOUBLE:
		case FLOAT:
			javaDefaultValue = "new " + BigDecimal.class.getName() + "(\""
					+ dbDefaultValue.replaceAll(pattern, "").trim() + "\")";
			break;
		case DATE:
		case TIME:
		case TIMESTAMP:
		case TIMESTAMP_TIMEZONE:
		case TIMESTAMP_LOCAL_TIMEZONE:
			javaDefaultValue = getDateDefaultValue();
			break;
		case ARRAY:
			javaDefaultValue = "new " + ArrayList.class.getName() + "<>()";
			break;
		default:
			javaDefaultValue = null;
			break;
		}
	}

	private String getDateDefaultValue() {
		if (dbDefaultValue == null) {
			return null;
		}

		String parsed = dbDefaultValue.toLowerCase().replace("(", "").replace(")", "").replace("'", "").trim();
		if (parsed.equals("null")) {
			return null;
		}

		boolean today = false;
		String toFormatPuiMethod = "es.prodevelop.pui9.utils.PuiDateUtil.stringToInstant(\"%s\")";

		switch (entity.getConfiguration().getDatabase().getType()) {
		case ORACLE:
			if (parsed.contains("sysdate") || parsed.contains("current_timestamp")) {
				today = true;
			} else if (parsed.contains("date") || parsed.contains("timestamp")) {
				parsed = parsed.replace("date", "").replace("timestamp", "").trim();
				if (parsed.contains(",")) {
					parsed = parsed.split(",")[0];
				}
			}
			break;
		case SQL_SERVER:
			if (parsed.contains("getdate") || parsed.contains("getutcdate") || parsed.contains("sysdatetime")
					|| parsed.contains("sysutcdatetime")) {
				today = true;
			}
			break;
		case POSTGRE_SQL:
			if (parsed.contains("now") || parsed.contains("current_date")) {
				today = true;
			}
			break;
		}

		if (today) {
			return "java.time.Instant.now()";
		} else {
			return String.format(toFormatPuiMethod, parsed);
		}
	}

	@Override
	public String toString() {
		return "DB name: " + dbName + "\nDB type: " + dbType + "\nJava name: " + javaName + "\nJava type: " + javaType
				+ "\nPK: " + pk + "\nNullable: " + nullable + "\nAutoincrementable: " + autoincrementable
				+ "\nSequence: " + sequence + "\nGeometry: " + geometry;
	}

	@Override
	public int compareTo(Column otherColumn) {
		if (position < otherColumn.getPosition()) {
			return -1;
		} else if (position == otherColumn.getPosition()) {
			return 0;
		} else {
			return 1;
		}
	}

}
