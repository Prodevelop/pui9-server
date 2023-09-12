package es.prodevelop.pui9.model.dao;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.exceptions.PuiDaoAttributeLengthException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoNoNumericColumnException;
import es.prodevelop.pui9.exceptions.PuiDaoNullParametersException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * This abstract class provides the implementation of the all the DAO for JDBC
 * approach. It uses the JdbcTemplate to manage the statements and connections
 * against the Database
 * <p>
 * If you want to use a DAO, you must to create an Autowired property using the
 * interface of this DAO.
 * 
 * @param <T> The whole {@link IDto} class that represents this DAO Class
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractDao<T extends IDto> implements IDao<T> {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	protected DaoRegistry daoRegistry;

	protected Class<T> dtoClass;

	/**
	 * Registers the DAO into the DaoRegistry
	 */
	@PostConstruct
	protected void postConstruct() {
		daoRegistry.registerDao(getClass());

		dtoClass = daoRegistry.getDtoFromDao(getClass(), false);
	}

	@Override
	public Class<T> getDtoClass() {
		return dtoClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends IDao<T>> getDaoClass() {
		return (Class<IDao<T>>) (Object) getClass();
	}

	/**
	 * Executes an equality search over the given fieldName with the given value
	 * 
	 * @param fieldName The field name
	 * @param value     The value of the field
	 * @return The list of registries that acomplish the equality
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected List<T> findByColumn(String fieldName, Object value) throws PuiDaoFindException {
		return findByColumn(fieldName, value, PuiUserSession.getSessionLanguage());
	}

	/**
	 * Executes an equality search over the given fieldName with the given value and
	 * for the given language
	 * 
	 * @param fieldName The field name
	 * @param value     The value of the field
	 * @param language  The language used in the search
	 * @return The list of registries that acomplish the equality
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected List<T> findByColumn(String fieldName, Object value, PuiLanguage language) throws PuiDaoFindException {
		String columnName = DtoRegistry.getColumnNameFromFieldName(dtoClass, fieldName);

		FilterBuilder filterBuilder = FilterBuilder.newAndFilter();
		if (DtoRegistry.getStringFields(dtoClass).contains(fieldName) && value instanceof String) {
			filterBuilder.addEqualsExact(columnName, (String) value);
		} else {
			filterBuilder.addEquals(columnName, value);
		}

		return findWhere(filterBuilder, language);
	}

	/**
	 * Check the values of the given DTO. By default, null values and maximum length
	 * values are checked
	 * 
	 * @param dto The DTO to be checked
	 * @throws PuiDaoNullParametersException  If an attribute is set to null and its
	 *                                        value is mandatory
	 * @throws PuiDaoAttributeLengthException If an attribute length is higher that
	 *                                        the indicated in the
	 *                                        {@link PuiField#maxlength()} attribute
	 */
	protected void checkValues(ITableDto dto) throws PuiDaoNullParametersException, PuiDaoAttributeLengthException {
		Map<String, Field> map = new LinkedHashMap<>();
		map.putAll(DtoRegistry.getMapFieldsFromFieldName(dto.getClass()));
		map.putAll(DtoRegistry.getLangMapFieldsFromFieldName(dto.getClass()));

		// Not null values
		List<String> notNullFieldNames = DtoRegistry.getNotNullFields(dto.getClass());
		for (String fieldName : notNullFieldNames) {
			if (DtoRegistry.getSequenceFields(dto.getClass()).contains(fieldName)) {
				continue;
			}

			Field field = map.get(fieldName);
			if (field == null) {
				continue;
			}

			Object value;
			try {
				value = FieldUtils.readField(field, dto, true);
			} catch (Exception e) {
				continue;
			}

			if (value == null) {
				throw new PuiDaoNullParametersException(fieldName);
			}

			if (DtoRegistry.getStringFields(dto.getClass()).contains(fieldName) && ObjectUtils.isEmpty(value)) {
				throw new PuiDaoNullParametersException(fieldName);
			}
		}

		// Max length
		Map<String, Integer> maxLengthFieldNames = DtoRegistry.getFieldNamesMaxLength(dto.getClass());
		for (Entry<String, Integer> entry : maxLengthFieldNames.entrySet()) {
			String fieldName = entry.getKey();
			Integer maxLength = entry.getValue();
			if (maxLength.equals(-1)) {
				continue;
			}

			Field field = map.get(fieldName);
			if (field == null) {
				continue;
			}

			if (!field.getType().equals(String.class)) {
				continue;
			}

			String value;
			try {
				value = (String) FieldUtils.readField(field, dto, true);
			} catch (Exception e) {
				continue;
			}

			if (value != null && value.length() > maxLength) {
				throw new PuiDaoAttributeLengthException(fieldName, maxLength, value.length());
			}
		}
	}

	/**
	 * It is called in the insert operation.
	 * <p>
	 * Set all the autoincrementable fields in the DTO (only if it is not set
	 * before).
	 * <p>
	 * For each autoincrementable field,
	 * {@link #getAutoincrementableColumnFilter(ITableDto, String)} is called in
	 * order to provide a filter to be used in the calculus of the next value of the
	 * column
	 * 
	 * @param dto The registry to be modified
	 * @throws PuiDaoNoNumericColumnException If the column to be modified is not of
	 *                                        numeric type
	 */
	protected void setAutoincrementableValues(List<T> dtoList) throws PuiDaoNoNumericColumnException {
		List<String> fieldNames = DtoRegistry.getAutoincrementableFieldNames(dtoClass);

		for (String fieldName : fieldNames) {
			try {
				Field field = DtoRegistry.getJavaFieldFromFieldName(dtoClass, fieldName);
				if (field.get(dtoList.get(0)) != null) {
					continue;
				}

				String columnName = DtoRegistry.getColumnNameFromFieldName(dtoClass, fieldName);
				FilterBuilder filterBuilder = getAutoincrementableColumnFilter(dtoList.get(0), columnName);

				Number nextId = getNextValue(columnName, filterBuilder);

				for (T dto : dtoList) {
					if (FieldUtils.readField(field, dto, true) == null) {
						continue;
					}

					FieldUtils.writeField(field, dto, nextId, true);

					if (nextId instanceof Long) {
						nextId = nextId.longValue() + 1;
					} else if (nextId instanceof Integer) {
						nextId = nextId.intValue() + 1;
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do nothing
			}
		}
	}

	/**
	 * Creates a filter to be applied in the calculus of the next value of the
	 * column
	 * 
	 * @param dto        The registry to be modified
	 * @param columnName The autoincrementable column name
	 * @return The filter to be used in the calculus of the next value
	 */
	protected FilterBuilder getAutoincrementableColumnFilter(T dto, String columnName) {
		return null;
	}

	protected Map<String, Object> convertFieldsToColumns(Map<String, Object> fieldValuesMap) {
		List<String> dtoColumns = DtoRegistry.getColumnNames(dtoClass);

		// convert all possible fields to columns
		Map<String, Object> columnValuesMap = new LinkedHashMap<>();
		fieldValuesMap.forEach((fieldName, value) -> {
			String columnName = DtoRegistry.getColumnNameFromFieldName(dtoClass, fieldName);
			if (columnName == null) {
				columnName = fieldName;
			}
			columnValuesMap.put(columnName, value);
		});

		columnValuesMap.keySet().removeIf(fieldName -> !dtoColumns.contains(fieldName));
		return columnValuesMap;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}