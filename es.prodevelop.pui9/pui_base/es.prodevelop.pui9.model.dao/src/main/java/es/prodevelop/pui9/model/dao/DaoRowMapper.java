package es.prodevelop.pui9.model.dao;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.utils.IPuiObject;

public class DaoRowMapper<T extends IDto> implements RowMapper<T> {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	private AbstractDatabaseDao<T> dao;
	private Class<T> dtoClass;

	public DaoRowMapper(AbstractDatabaseDao<T> dao, Class<T> dtoClass) {
		this.dao = dao;
		if (dtoClass.isInterface()) {
			dtoClass = DtoRegistry.getDtoImplementation(dtoClass);
		}
		this.dtoClass = dtoClass;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T dto;
		try {
			dto = dtoClass.newInstance();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}

		DtoRegistry.getAllColumnNames(dtoClass).forEach(columnName -> {
			try {
				String fieldName = DtoRegistry.getFieldNameFromColumnName(dtoClass, columnName);
				Field field = DtoRegistry.getJavaFieldFromColumnName(dtoClass, columnName);
				if (field == null) {
					field = DtoRegistry.getJavaFieldFromLangFieldName(dtoClass, fieldName);
				}

				Object value = null;
				if (DtoRegistry.getDateTimeFields(dtoClass).contains(fieldName)) {
					value = rs.getTimestamp(columnName);
				} else {
					value = rs.getObject(columnName);
				}
				value = extractRealValue(field, value);

				FieldUtils.writeField(field, dto, value, true);
			} catch (Exception e) {
				// do nothing
			}
		});

		if (dao != null) {
			dao.customizeDto(dto);
		}

		return dto;
	}

	/**
	 * Extract the value for special cases: when the type of the field is different
	 * of the type that the DB returns to us
	 * 
	 * @param field The field of the DTO
	 * @param value The value extracted from database
	 * @return The value transformed
	 */
	private Object extractRealValue(Field field, Object value) {
		if (value == null) {
			return null;
		}

		try {
			if (value instanceof Array) {
				List<Object> list = new ArrayList<>();
				Object[] array = (Object[]) ((Array) value).getArray();
				list.addAll(Arrays.asList(array));
				value = list;
			} else if (value instanceof Timestamp) {
				Timestamp timestamp = (Timestamp) value;
				Instant instant = timestamp.toInstant();
				value = instant;
			} else if (value instanceof Double) {
				if (field.getType().equals(BigDecimal.class)) {
					value = new BigDecimal(((Double) value).toString());
				}
			} else if (value instanceof Float) {
				if (field.getType().equals(BigDecimal.class)) {
					value = new BigDecimal(((Float) value).toString());
				}
			} else if (value instanceof BigDecimal) {
				if (field.getType().equals(Long.class)) {
					value = Long.valueOf(((BigDecimal) value).longValue());
				} else if (field.getType().equals(Integer.class)) {
					value = Integer.valueOf(((BigDecimal) value).intValue());
				} else {
					value = new BigDecimal(((BigDecimal) value).stripTrailingZeros().toPlainString());
				}
			} else if (value instanceof Integer) {
				if (field.getType().equals(Short.class)) {
					value = Short.valueOf(value.toString());
				}
			} else if (value instanceof Clob) {
				Clob clob = (Clob) value;
				StringWriter stringWriter = new StringWriter();
				IOUtils.copy(clob.getCharacterStream(), stringWriter);
				value = stringWriter.toString();
			} else if (value instanceof String) {
				// do nothing
			} else if (value instanceof Boolean) {
				// do nothing
			} else {
				if (field.getType().equals(String.class)) {
					value = value.toString();
				}
			}
		} catch (Exception e) {
			// do nothing
		}

		if (IPuiObject.class.isAssignableFrom(field.getType())) {
			value = GsonSingleton.getSingleton().getGson().fromJson(value.toString(), field.getType());
		}

		return value;
	}

}
