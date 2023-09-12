package es.prodevelop.pui9.model.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jooq.Condition;
import org.jooq.DeleteConditionStep;
import org.jooq.DeleteUsingStep;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.Table;
import org.jooq.UpdateConditionStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.eventlistener.ThreadDaoEvents;
import es.prodevelop.pui9.eventlistener.event.DeleteDaoEvent;
import es.prodevelop.pui9.eventlistener.event.InsertDaoEvent;
import es.prodevelop.pui9.eventlistener.event.UpdateDaoEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;
import es.prodevelop.pui9.exceptions.PuiDaoAttributeLengthException;
import es.prodevelop.pui9.exceptions.PuiDaoDataAccessException;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoDuplicatedException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoIntegrityOnDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoIntegrityOnInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoIntegrityOnUpdateException;
import es.prodevelop.pui9.exceptions.PuiDaoNoNumericColumnException;
import es.prodevelop.pui9.exceptions.PuiDaoNullParametersException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiDaoUpdateException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.utils.IPuiObject;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * This abstract class provides the implementation of the all the Table DAO for
 * JDBC approach. It implements {@link ITableDao} interface for bringing the
 * necessary methods to manage the tables
 * 
 * @param <TPK> The PK {@link IDto} class that represents this DAO Class
 * @param <T>   The whole {@link IDto} class that represents this DAO Class
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractTableDao<TPK extends ITableDto, T extends TPK> extends AbstractDatabaseDao<T>
		implements ITableDao<TPK, T> {

	@Autowired
	private ThreadDaoEvents threadDaoEvents;

	private ITableDao<ITableDto, ITableDto> tableTranslationDao = null;

	@Override
	public boolean exists(TPK dtoPk) throws PuiDaoFindException {
		return findOne(dtoPk) != null;
	}

	@Override
	public T findOne(TPK dtoPk) throws PuiDaoFindException {
		return findOne(dtoPk, PuiUserSession.getSessionLanguage());
	}

	@Override
	public T findOne(TPK dtoPk, PuiLanguage language) throws PuiDaoFindException {
		if (dtoPk == null) {
			return null;
		}

		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		FilterBuilder filterBuilder = FilterBuilder.newAndFilter();
		for (Iterator<String> it = pkColumnNames.iterator(); it.hasNext();) {
			String next = it.next();
			try {
				Field pkField = mapPk.get(next);
				if (pkField == null) {
					return null;
				}

				Object val = FieldUtils.readField(pkField, dtoPk, true);
				if (val == null) {
					// PK attribute should always have value
					return null;
				}

				if (DtoRegistry.getStringFields(dtoClass).contains(next)) {
					filterBuilder.addEqualsExact(next, val.toString());
				} else {
					filterBuilder.addEquals(next, val);
				}
			} catch (Exception e) {
				return null;
			}
		}

		List<T> list = findWhere(filterBuilder, language);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public T insert(T dto) throws PuiDaoInsertException {
		if (dto == null) {
			return null;
		}

		try {
			checkValues(dto);
		} catch (PuiDaoNullParametersException | PuiDaoAttributeLengthException e) {
			throw new PuiDaoInsertException(e);
		}

		List<T> dtoList = Collections.singletonList(dto);

		try {
			setAutoincrementableValues(dtoList);
		} catch (PuiDaoNoNumericColumnException e) {
			throw new PuiDaoInsertException(e);
		}

		InsertValuesStepN<Record> insert = createInsertStatement();

		try {
			doInsert(dtoList, insert);

			insertTranslations(dtoList);

			afterInsert(dtoList);
		} catch (DuplicateKeyException e) {
			throw new PuiDaoInsertException(new PuiDaoDuplicatedException());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnInsertException(e);
		} catch (Exception e) {
			throw new PuiDaoInsertException(new PuiDaoDataAccessException(e));
		}

		return dto;
	}

	@Override
	public List<T> bulkInsert(List<T> dtoList) throws PuiDaoInsertException {
		if (dtoList == null) {
			return Collections.emptyList();
		}

		try {
			for (T dto : dtoList) {
				checkValues(dto);
			}
		} catch (PuiDaoNullParametersException | PuiDaoAttributeLengthException e) {
			throw new PuiDaoInsertException(e);
		}

		try {
			setAutoincrementableValues(dtoList);
		} catch (PuiDaoNoNumericColumnException e) {
			throw new PuiDaoInsertException(e);
		}

		InsertValuesStepN<Record> insert = createInsertStatement();

		try {
			doInsert(dtoList, insert);

			insertTranslations(dtoList);

			afterInsert(dtoList);
		} catch (DuplicateKeyException e) {
			throw new PuiDaoInsertException(new PuiDaoDuplicatedException());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnInsertException(e);
		} catch (Exception e) {
			throw new PuiDaoInsertException(new PuiDaoDataAccessException(e));
		}

		return dtoList;
	}

	/**
	 * Perform the insert in the table of the specified DTO
	 */
	private InsertValuesStepN<Record> createInsertStatement() {
		Table<Record> table = DSL.table(getEntityName());

		List<String> columnNames = new ArrayList<>(DtoRegistry.getColumnNames(dtoClass));
		columnNames.removeAll(DtoRegistry.getSequenceColumns(dtoClass));

		List<org.jooq.Field<Object>> columns = columnNames.stream().map(DSL::field).collect(Collectors.toList());

		List<org.jooq.Field<Object>> values = columnNames.stream()
				.map(colName -> DSL.field(getParameterTextForColumnInsert(colName))).collect(Collectors.toList());

		InsertValuesStepN<Record> insert = dbHelper.getDSLContext().insertInto(table).columns(columns).values(values);
		if (onInsertConflictDoNothing()) {
			insert.onConflictDoNothing();
		}

		return insert;
	}

	/**
	 * Get the parameter text in the SQL for the column on insert
	 * 
	 * @param columnName The column name
	 * @return The parameter text in the SQL
	 */
	protected String getParameterTextForColumnInsert(String columnName) {
		return getParameterForSql(columnName);
	}

	/**
	 * True if do nothing when Insert fails (only supported databases, ie.
	 * PostgreSQL)
	 * 
	 * @return true/false
	 */
	protected boolean onInsertConflictDoNothing() {
		return false;
	}

	/**
	 * Really performs the Insert into the database
	 * 
	 * @param dtoList     The DTO list to be inserted
	 * @param insertQuery The query to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected void doInsert(List<T> dtoList, InsertValuesStepN<Record> insertQuery) throws DataAccessException {
		checkDataSource();
		List<String> columnNames = new ArrayList<>(DtoRegistry.getColumnNames(dtoClass));
		List<String> sequenceColumns = DtoRegistry.getSequenceColumns(dtoClass);
		columnNames.removeAll(sequenceColumns);
		Map<String, Field> map = DtoRegistry.getMapFieldsFromColumnName(dtoClass);

		if (dtoList.size() == 1) {
			// when the list size of DTO is only 1 element...
			T dto = dtoList.get(0);

			// following code sets the parameters for the statement, using a KeyHolder to
			// retrieve any generated key by the database
			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement;
					if (sequenceColumns.isEmpty()) {
						statement = con.prepareStatement(insertQuery.getSQL(), Statement.RETURN_GENERATED_KEYS);
					} else {
						statement = con.prepareStatement(insertQuery.getSQL(), sequenceColumns.toArray(new String[0]));
					}
					int nextParameter = 1;
					for (String columnName : columnNames) {
						Field field = map.get(columnName);
						if (field == null) {
							continue;
						}

						Object val;
						try {
							val = FieldUtils.readField(field, dto, true);
						} catch (Exception e) {
							continue;
						}

						val = convertPuiObjectToJsonValue(field, val);
						val = modifyInsertColumnValue(dto, columnName, val);

						if (val instanceof Instant) {
							val = Timestamp.from((Instant) val);
						}
						statement.setObject(nextParameter++, val);
					}

					return statement;
				}
			}, holder);

			Map<String, Object> keys = holder.getKeys();
			if (keys == null) {
				return;
			}

			for (String seqCol : sequenceColumns) {
				Object val = null;
				try {
					if (holder.getKeys() != null && keys.containsKey(seqCol)) {
						val = keys.get(seqCol);
					}
					if (val == null) {
						// sometimes previous operation doesn't get the real value
						val = holder.getKey();
					}
				} catch (InvalidDataAccessApiUsageException e) {
					break;
				}

				if (val == null) {
					continue;
				}

				Field field = map.get(seqCol);
				if (!field.getType().equals(val.getClass())) {
					if (Long.class.equals(field.getType())) {
						val = new Long(val.toString());
					} else if (Integer.class.equals(field.getType())) {
						val = new Integer(val.toString());
					} else if (BigDecimal.class.equals(field.getType())) {
						val = new BigDecimal(val.toString());
					}
				}
				try {
					FieldUtils.writeField(field, dto, val, true);
				} catch (Exception e) {
					// should never occurs
				}
			}
		} else {
			// when the list size of DTO is more than 1 element...
			jdbcTemplate.batchUpdate(insertQuery.getSQL(), new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					T dto = dtoList.get(i);
					int nextParameter = 1;
					for (String columnName : columnNames) {
						Field field = map.get(columnName);
						if (field == null) {
							continue;
						}

						Object val;
						try {
							val = FieldUtils.readField(field, dto, true);
						} catch (Exception e) {
							continue;
						}

						val = convertPuiObjectToJsonValue(field, val);
						val = modifyInsertColumnValue(dto, columnName, val);

						if (val instanceof Instant) {
							val = Timestamp.from((Instant) val);
						}
						ps.setObject(nextParameter++, val);
					}
				}

				@Override
				public int getBatchSize() {
					return dtoList.size();
				}

			});
		}
	}

	/**
	 * This method is useful to modify the value used in the insert sql. By default
	 * returns the given value
	 * 
	 * @param dto        The DTO to be inserted
	 * @param columnName The column to be modified
	 * @param value      The value to be inserted
	 * 
	 * @return The modified value. By default, the same parameter value
	 */
	protected Object modifyInsertColumnValue(T dto, String columnName, Object value) {
		return value;
	}

	/**
	 * Insert the translations of the given DTO by calling the
	 * {@link #insert(ITableDto)} method of the translation DAO
	 * 
	 * @param dtoList The DTO list to be inserted
	 * @throws PuiDaoSaveException If any SQL error while executing the statement is
	 *                             thrown
	 */
	private void insertTranslations(List<T> dtoList) throws PuiDaoSaveException {
		if (getTableTranslationDao() == null) {
			return;
		}

		Class<ITableDto> translationClass = getTableTranslationDao().getDtoClass();
		Field langField = DtoRegistry.getJavaFieldFromColumnName(translationClass, IDto.LANG_COLUMN_NAME);
		Field langStatusField = DtoRegistry.getJavaFieldFromColumnName(translationClass, IDto.LANG_STATUS_COLUMN_NAME);

		try {
			for (T dto : dtoList) {
				ITableDto translation = translationClass.newInstance();
				PuiObjectUtils.copyProperties(translation, dto);
				FieldUtils.writeField(langStatusField, translation, PuiConstants.TRUE_INT, true);
				getTableTranslationDao().insert(translation);

				String baseLang = (String) langField.get(translation);
				for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
					PuiLanguage lang = it.next();
					if (lang.getIsocode().equals(baseLang)) {
						continue;
					}
					translation = translationClass.newInstance();
					PuiObjectUtils.copyProperties(translation, dto);
					FieldUtils.writeField(langField, translation, lang.getIsocode(), true);
					FieldUtils.writeField(langStatusField, translation, PuiConstants.FALSE_INT, true);
					getTableTranslationDao().insert(translation);
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// do nothing
		}
	}

	/**
	 * Perform some actions after inserting the DTO. By default, the
	 * {@link InsertDaoEvent} will be fired, and all the associated
	 * {@link PuiListener} will be executed
	 * 
	 * @param dtoList The inserted DTO list
	 */
	protected void afterInsert(List<T> dtoList) {
		dtoList.forEach(dto -> {
			if (daoRegistry.getAllTableDaoLang().contains(daoRegistry.getDaoFromDto(dtoClass))) {
				return;
			}
			threadDaoEvents.addEventType(new InsertDaoEvent(dto));
		});
	}

	@Override
	public T update(T dto) throws PuiDaoUpdateException {
		if (dto == null) {
			return null;
		}

		PuiLanguage lang = PuiLanguageUtils.getLanguage(dto);
		T oldDto;
		try {
			oldDto = findOne(dto.<TPK>createPk(), lang);
		} catch (PuiDaoFindException e) {
			throw new PuiDaoUpdateException(e);
		}

		try {
			checkValues(dto);
		} catch (PuiDaoNullParametersException | PuiDaoAttributeLengthException e) {
			throw new PuiDaoUpdateException(e);
		}

		List<T> dtoList = Collections.singletonList(dto);
		UpdateConditionStep<Record> update = createUpdateStatement();

		try {
			doUpdate(dtoList, update);

			updateTranslations(dtoList);

			afterUpdate(oldDto, dtoList);
		} catch (DuplicateKeyException e) {
			throw new PuiDaoUpdateException(new PuiDaoDuplicatedException());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnUpdateException(e);
		} catch (Exception e) {
			throw new PuiDaoUpdateException(new PuiDaoDataAccessException(e));
		}

		return dto;
	}

	@Override
	public List<T> bulkUpdate(List<T> dtoList) throws PuiDaoUpdateException {
		if (dtoList == null) {
			return Collections.emptyList();
		}

		try {
			for (T dto : dtoList) {
				checkValues(dto);
			}
		} catch (PuiDaoNullParametersException | PuiDaoAttributeLengthException e) {
			throw new PuiDaoUpdateException(e);
		}

		UpdateConditionStep<Record> update = createUpdateStatement();

		try {
			doUpdate(dtoList, update);

			updateTranslations(dtoList);

			afterUpdate(null, dtoList);
		} catch (DuplicateKeyException e) {
			throw new PuiDaoUpdateException(new PuiDaoDuplicatedException());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnUpdateException(e);
		} catch (Exception e) {
			throw new PuiDaoUpdateException(new PuiDaoDataAccessException(e));
		}

		return dtoList;
	}

	/**
	 * Perform the update in the table of the specified DTO
	 */
	private UpdateConditionStep<Record> createUpdateStatement() {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());

		List<String> columnNames = new ArrayList<>(DtoRegistry.getColumnNames(dtoClass));
		columnNames.removeAll(DtoRegistry.getSequenceColumns(dtoClass));

		Table<Record> table = DSL.table(getEntityName());

		Map<Object, Object> set = columnNames.stream()
				.collect(Collectors.toMap(DSL::field, cn -> DSL.field(getParameterTextForColumnUpdate(cn)), (u, v) -> {
					throw new IllegalStateException(String.format("Duplicate key %s", u));
				}, LinkedHashMap::new));

		List<Condition> condition = pkColumnNames.stream()
				.map(pkColName -> DSL.row(DSL.field(pkColName)).equal(DSL.field(PARAMETER)))
				.collect(Collectors.toList());

		return dbHelper.getDSLContext().update(table).set(set).where(condition);
	}

	/**
	 * Get the parameter text in the SQL for the column on update
	 * 
	 * @param columnName The column name
	 * @return The parameter text in the SQL
	 */
	protected String getParameterTextForColumnUpdate(String columnName) {
		return getParameterForSql(columnName);
	}

	/**
	 * Really performs the Uptate into the database
	 * 
	 * @param dtoList     The DTO list to be updated
	 * @param updateQuery The query to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected void doUpdate(List<T> dtoList, UpdateConditionStep<Record> updateQuery) throws DataAccessException {
		checkDataSource();
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());

		List<String> columnNames = new ArrayList<>(DtoRegistry.getColumnNames(dtoClass));
		columnNames.removeAll(DtoRegistry.getSequenceColumns(dtoClass));

		if (ObjectUtils.isEmpty(columnNames)) {
			return;
		}

		Map<String, Field> map = DtoRegistry.getMapFieldsFromColumnName(dtoClass);
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		if (dtoList.size() == 1) {
			// when the list size of DTO is only 1 element...
			T dto = dtoList.get(0);
			List<Object> values = new ArrayList<>();
			for (String columnName : columnNames) {
				Field field = map.get(columnName);
				if (field == null) {
					continue;
				}

				Object val;
				try {
					val = FieldUtils.readField(field, dto, true);
				} catch (Exception e) {
					continue;
				}

				val = convertPuiObjectToJsonValue(field, val);
				val = modifyUpdateColumnValue(dto, columnName, val);

				if (val instanceof Instant) {
					val = Timestamp.from((Instant) val);
				}

				values.add(val);
			}

			TPK dtoPk = dto.createPk();
			for (String pkColumnName : pkColumnNames) {
				Field field = mapPk.get(pkColumnName);
				if (field == null) {
					continue;
				}

				Object val;
				try {
					val = FieldUtils.readField(field, dtoPk, true);
				} catch (Exception e1) {
					try {
						val = FieldUtils.readField(field, dto, true);
					} catch (Exception e2) {
						continue;
					}
				}

				val = modifyUpdateColumnValue(dto, pkColumnName, val);

				if (val instanceof Instant) {
					val = Timestamp.from((Instant) val);
				}

				values.add(val);
			}

			jdbcTemplate.update(updateQuery.getSQL(), values.toArray());
		} else {
			// when the list size of DTO is more than 1 element...
			jdbcTemplate.batchUpdate(updateQuery.getSQL(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					T dto = dtoList.get(i);
					int nextParameter = 1;
					for (String columnName : columnNames) {
						Field field = map.get(columnName);
						if (field == null) {
							continue;
						}

						Object val;
						try {
							val = FieldUtils.readField(field, dto, true);
						} catch (Exception e) {
							continue;
						}

						val = convertPuiObjectToJsonValue(field, val);
						val = modifyUpdateColumnValue(dto, columnName, val);

						if (val instanceof Instant) {
							val = Timestamp.from((Instant) val);
						}
						ps.setObject(nextParameter++, val);
					}

					TPK dtoPk = dto.createPk();
					for (String pkColumnName : pkColumnNames) {
						Field field = mapPk.get(pkColumnName);
						if (field == null) {
							continue;
						}

						Object val;
						try {
							val = FieldUtils.readField(field, dtoPk, true);
						} catch (Exception e1) {
							try {
								val = FieldUtils.readField(field, dto, true);
							} catch (Exception e2) {
								continue;
							}
						}

						val = modifyUpdateColumnValue(dto, pkColumnName, val);

						if (val instanceof Instant) {
							val = Timestamp.from((Instant) val);
						}
						ps.setObject(nextParameter++, val);
					}
				}

				@Override
				public int getBatchSize() {
					return dtoList.size();
				}
			});
		}
	}

	/**
	 * This method is useful to modify the value used in the update sql. By default
	 * returns the given value
	 * 
	 * @param dto        The DTO to be updated
	 * @param columnName The column to be modified
	 * @param value      The value to be updated
	 * 
	 * @return The modified value. By default, the same parameter value
	 */
	protected Object modifyUpdateColumnValue(T dto, String columnName, Object value) {
		return value;
	}

	/**
	 * Update the translations of the given DTO by calling the
	 * {@link #update(ITableDto)} method of the translation DAO
	 * 
	 * @param dto The DTO to be updated
	 * @throws PuiDaoSaveException If any SQL error while executing the statement is
	 *                             thrown
	 */
	private void updateTranslations(List<T> dtoList) throws PuiDaoSaveException {
		if (getTableTranslationDao() == null) {
			return;
		}

		Class<ITableDto> translationClass = getTableTranslationDao().getDtoClass();
		try {
			for (T dto : dtoList) {
				ITableDto translation = translationClass.newInstance();
				PuiObjectUtils.copyProperties(translation, dto);
				getTableTranslationDao().update(translation);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// do nothing
		}
	}

	/**
	 * Perform some actions after updating the DTO. By default, the
	 * {@link UpdateDaoEvent} will be fired, and all the associated
	 * {@link PuiListener} will be executed
	 * 
	 * @param oldDto  The old DTO
	 * @param dtoList The updated DTO
	 */
	protected void afterUpdate(T oldDto, List<T> dtoList) {
		dtoList.forEach(dto -> {
			if (daoRegistry.getAllTableDaoLang().contains(daoRegistry.getDaoFromDto(dtoClass))) {
				return;
			}
			threadDaoEvents.addEventType(new UpdateDaoEvent(dto, oldDto));
		});
	}

	@Override
	public TPK patch(TPK dtoPk, Map<String, Object> fieldValuesMap) throws PuiDaoSaveException {
		Map<String, Object> columnValuesMap = convertFieldsToColumns(fieldValuesMap);
		if (columnValuesMap.isEmpty()) {
			return dtoPk;
		}

		List<TPK> dtoPkList = Collections.singletonList(dtoPk);
		UpdateConditionStep<Record> update = createPatchStatement(columnValuesMap);

		try {
			doPatch(dtoPkList, columnValuesMap, update);

			patchTranslations(dtoPkList, fieldValuesMap);

			afterPatch(dtoPkList, fieldValuesMap);
		} catch (DuplicateKeyException e) {
			throw new PuiDaoUpdateException(new PuiDaoDuplicatedException());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnUpdateException(e);
		} catch (Exception e) {
			throw new PuiDaoUpdateException(new PuiDaoDataAccessException(e));
		}

		return dtoPk;
	}

	@Override
	public void bulkPatch(List<TPK> dtoPkList, Map<String, Object> fieldValuesMap) throws PuiDaoUpdateException {
		if (dtoPkList == null) {
			return;
		}

		Map<String, Object> columnValuesMap = convertFieldsToColumns(fieldValuesMap);
		if (columnValuesMap.isEmpty()) {
			return;
		}

		UpdateConditionStep<Record> update = createPatchStatement(columnValuesMap);

		try {
			doPatch(dtoPkList, columnValuesMap, update);

			patchTranslations(dtoPkList, fieldValuesMap);

			afterPatch(dtoPkList, fieldValuesMap);
		} catch (DuplicateKeyException e) {
			throw new PuiDaoUpdateException(new PuiDaoDuplicatedException());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnUpdateException(e);
		} catch (Exception e) {
			throw new PuiDaoUpdateException(new PuiDaoDataAccessException(e));
		}
	}

	private UpdateConditionStep<Record> createPatchStatement(Map<String, Object> columnValuesMap) {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());

		Table<Record> table = DSL.table(getEntityName());

		Map<Object, Object> set = columnValuesMap.keySet().stream()
				.collect(Collectors.toMap(DSL::field, cn -> DSL.field(getParameterTextForColumnUpdate(cn)), (u, v) -> {
					throw new IllegalStateException(String.format("Duplicate key %s", u));
				}, LinkedHashMap::new));

		List<Condition> where = pkColumnNames.stream()
				.map(pkColName -> DSL.row(DSL.field(pkColName)).equal(DSL.field(PARAMETER)))
				.collect(Collectors.toList());

		return dbHelper.getDSLContext().update(table).set(set).where(where);
	}

	protected void doPatch(List<TPK> dtoPkList, Map<String, Object> columnValuesMap,
			UpdateConditionStep<Record> patchQuery) {
		checkDataSource();
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		if (dtoPkList.size() == 1) {
			// when the list size of DTO is only 1 element...
			TPK dtoPk = dtoPkList.get(0);
			List<Object> values = new ArrayList<>();
			for (Iterator<Entry<String, Object>> it = columnValuesMap.entrySet().iterator(); it.hasNext();) {
				Entry<String, Object> next = it.next();
				Object val = next.getValue();
				val = convertPuiObjectToJsonValue(DtoRegistry.getJavaFieldFromColumnName(dtoClass, next.getKey()), val);
				val = modifyPatchColumnValue(dtoPk, next.getKey(), val);
				if (val instanceof Instant) {
					val = Timestamp.from((Instant) val);
				}
				values.add(val);
			}

			for (Iterator<String> it = pkColumnNames.iterator(); it.hasNext();) {
				String next = it.next();
				Field field = mapPk.get(next);
				if (field == null) {
					continue;
				}

				try {
					Object val = FieldUtils.readField(field, dtoPk, true);
					val = modifyPatchColumnValue(dtoPk, next, val);
					if (val instanceof Instant) {
						val = Timestamp.from((Instant) val);
					}
					values.add(val);
				} catch (Exception e) {
					// do nothing
				}
			}

			jdbcTemplate.update(patchQuery.getSQL(), values.toArray());
		} else {
			// when the list size of DTO is more than 1 element...
			jdbcTemplate.batchUpdate(patchQuery.getSQL(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					int nextParameter = 1;
					TPK dtoPk = dtoPkList.get(i);

					for (Iterator<Entry<String, Object>> it = columnValuesMap.entrySet().iterator(); it.hasNext();) {
						Entry<String, Object> next = it.next();
						Object val = next.getValue();
						val = convertPuiObjectToJsonValue(
								DtoRegistry.getJavaFieldFromColumnName(dtoClass, next.getKey()), val);
						val = modifyPatchColumnValue(dtoPk, next.getKey(), val);
						if (val instanceof Instant) {
							val = Timestamp.from((Instant) val);
						}
						ps.setObject(nextParameter++, val);
					}

					for (Iterator<String> it = pkColumnNames.iterator(); it.hasNext();) {
						String pkColumnName = it.next();
						Field field = mapPk.get(pkColumnName);
						if (field == null) {
							continue;
						}

						try {
							Object val = FieldUtils.readField(field, dtoPk, true);
							val = modifyPatchColumnValue(dtoPk, pkColumnName, val);
							if (val instanceof Instant) {
								val = Timestamp.from((Instant) val);
							}
							ps.setObject(nextParameter++, val);
						} catch (Exception e) {
							// do nothing
						}
					}
				}

				@Override
				public int getBatchSize() {
					return dtoPkList.size();
				}
			});
		}
	}

	/**
	 * This method is useful to modify the value used in the update sql. By default
	 * returns the given value
	 * 
	 * @param dtoPk      The DTO PK to be updated
	 * @param columnName The column to be modified
	 * @param value      The value to be updated
	 * 
	 * @return The modified value. By default, the same parameter value
	 */
	protected Object modifyPatchColumnValue(TPK dtoPk, String columnName, Object value) {
		return value;
	}

	/**
	 * Update the translations of the given DTO by calling the
	 * {@link #patch(ITableDto)} method of the translation DAO
	 * 
	 * @param dtoPkList      The list of DTO to be updated
	 * @param fieldValuesMap The map of fields to be updated
	 * @throws PuiDaoSaveException If any SQL error while executing the statement is
	 *                             thrown
	 */
	private void patchTranslations(List<TPK> dtoPkList, Map<String, Object> fieldValuesMap) throws PuiDaoSaveException {
		if (getTableTranslationDao() == null) {
			return;
		}

		Class<ITableDto> translationClass = getTableTranslationDao().getDtoClass();
		for (TPK dtoPk : dtoPkList) {
			ITableDto translation;
			try {
				translation = translationClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				return;
			}

			PuiObjectUtils.copyProperties(translation, dtoPk);
			PuiLanguage lang;
			if (fieldValuesMap.containsKey(IDto.LANG_COLUMN_NAME)) {
				lang = new PuiLanguage(fieldValuesMap.get(IDto.LANG_COLUMN_NAME).toString());
			} else {
				lang = PuiUserSession.getSessionLanguage();
			}

			PuiLanguageUtils.setLanguage(translation, lang);
			getTableTranslationDao().patch(translation.createPk(), fieldValuesMap);
		}
	}

	/**
	 * Perform some actions after updating the DTO. By default, the
	 * {@link UpdateDaoEvent} will be fired, and all the associated
	 * {@link PuiListener} will be executed
	 * 
	 * @param oldDto  The old DTO
	 * @param dtoList The updated DTO
	 */
	protected void afterPatch(List<TPK> dtoPkList, Map<String, Object> fieldValuesMap) {
		dtoPkList.forEach(dtoPk -> {
			if (daoRegistry.getAllTableDaoLang().contains(daoRegistry.getDaoFromDto(dtoClass))) {
				return;
			}
			threadDaoEvents.addEventType(new UpdateDaoEvent(dtoPk, fieldValuesMap));
		});
	}

	@Override
	public TPK delete(TPK dtoPk) throws PuiDaoDeleteException {
		List<TPK> dtoPkList = Collections.singletonList(dtoPk);

		DeleteConditionStep<Record> delete = createDeleteStatement();

		try {
			deleteTranslations(dtoPkList);

			doDelete(dtoPkList, delete);

			afterDelete(dtoPkList);
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnDeleteException(e);
		} catch (Exception e) {
			throw new PuiDaoDeleteException(new PuiDaoDataAccessException(e));
		}

		return dtoPk;
	}

	@Override
	public List<TPK> bulkDelete(List<TPK> dtoPkList) throws PuiDaoDeleteException {
		if (dtoPkList == null) {
			return Collections.emptyList();
		}

		DeleteConditionStep<Record> delete = createDeleteStatement();

		try {
			deleteTranslations(dtoPkList);

			doDelete(dtoPkList, delete);

			afterDelete(dtoPkList);
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnDeleteException(e);
		} catch (Exception e) {
			throw new PuiDaoDeleteException(new PuiDaoDataAccessException(e));
		}

		return dtoPkList;
	}

	/**
	 * Perform the delete in the table of the specified DTO
	 * 
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	private DeleteConditionStep<Record> createDeleteStatement() throws DataAccessException {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());

		Table<Record> table = DSL.table(getEntityName());

		List<Condition> where = pkColumnNames.stream()
				.map(pkColName -> DSL.row(DSL.field(pkColName)).equal(DSL.field(PARAMETER)))
				.collect(Collectors.toList());

		return DSL.delete(table).where(where);
	}

	/**
	 * Really performs the Delete into the database
	 * 
	 * @param dtoPkList The DTO list to be deleted
	 * @param sql       The query to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected void doDelete(List<TPK> dtoPkList, DeleteConditionStep<Record> deleteQuery) throws DataAccessException {
		checkDataSource();
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		if (dtoPkList.size() == 1) {
			// when the list size of DTO is only 1 element...
			TPK dtoPk = dtoPkList.get(0);
			List<Object> values = new ArrayList<>();
			for (String pkColumnName : pkColumnNames) {
				Field field = mapPk.get(pkColumnName);
				if (field == null) {
					continue;
				}

				try {
					Object value = FieldUtils.readField(field, dtoPk, true);
					values.add(value);
				} catch (Exception e) {
					// do nothing
				}
			}

			jdbcTemplate.update(deleteQuery.getSQL(), values.toArray());
		} else {
			// when the list size of DTO is more than 1 element...
			jdbcTemplate.batchUpdate(deleteQuery.getSQL(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					TPK dtoPk = dtoPkList.get(i);
					int nextParameter = 1;
					for (String pkColumnName : pkColumnNames) {
						Field field = mapPk.get(pkColumnName);
						if (field == null) {
							continue;
						}

						try {
							Object value = FieldUtils.readField(field, dtoPk, true);
							ps.setObject(nextParameter++, value);
						} catch (Exception e) {
							// do nothing
						}
					}
				}

				@Override
				public int getBatchSize() {
					return dtoPkList.size();
				}
			});
		}
	}

	/**
	 * Deletes the given DTO from the language table
	 * 
	 * @param dtoPkList The DTO PK list to be deleted
	 * @throws PuiDaoDeleteException If any SQL error while executing the statement
	 *                               is thrown
	 */
	private void deleteTranslations(List<TPK> dtoPkList) throws PuiDaoDeleteException {
		if (getTableTranslationDao() == null) {
			return;
		}

		Class<ITableDto> translationClass = getTableTranslationDao().getDtoPkClass();
		try {
			FilterBuilder filter = FilterBuilder.newOrFilter();
			for (TPK dtoPk : dtoPkList) {
				ITableDto translation = translationClass.newInstance();
				PuiObjectUtils.copyProperties(translation, dtoPk);
				FilterBuilder pkFilter = FilterBuilder.newAndFilter();

				DtoRegistry.getPkFields(getDtoPkClass()).forEach(pkField -> {
					try {
						Object value = FieldUtils.readField(
								DtoRegistry.getJavaFieldFromFieldName(getDtoPkClass(), pkField), dtoPk, true);
						if (DtoRegistry.getStringFields(getDtoPkClass()).contains(pkField)) {
							pkFilter.addEqualsExact(pkField, (String) value);
						} else {
							pkFilter.addEquals(pkField, value);
						}
					} catch (IllegalAccessException e) {
						// do nothing
					}
				});

				filter.addGroup(pkFilter);
			}

			getTableTranslationDao().deleteWhere(filter);
		} catch (InstantiationException | IllegalAccessException e) {
			// do nothing
		}
	}

	/**
	 * Perform some actions after deleting the DTO. By default, the
	 * {@link DeleteDaoEvent} will be fired, and all the associated
	 * {@link PuiListener} will be executed
	 * 
	 * @param dtoPkList The deleted DTO list
	 */
	protected void afterDelete(List<TPK> dtoPkList) {
		dtoPkList.forEach(dtoPk -> {
			if (daoRegistry.getAllTableDaoLang().contains(daoRegistry.getDaoFromDto(dtoClass))) {
				return;
			}
			threadDaoEvents.addEventType(new DeleteDaoEvent(dtoPk));
		});
	}

	@Override
	public void deleteAll() throws PuiDaoDeleteException {
		deleteWhere((FilterBuilder) null);
	}

	@Override
	public void deleteAll(PuiLanguage language) throws PuiDaoDeleteException {
		deleteWhere(FilterBuilder.newAndFilter().addEqualsExact(IDto.LANG_COLUMN_NAME, language.getIsocode()));
	}

	@Override
	public void deleteWhere(FilterBuilder filterBuilder) throws PuiDaoDeleteException {
		Table<Record> table = DSL.table(getEntityName());
		DeleteUsingStep<Record> delete = DSL.delete(table);
		if (filterBuilder != null) {
			String where = dbHelper.processFilters(dtoClass, filterBuilder.asFilterGroup(), false);
			delete.where(where);
		}

		doDeleteWhere(delete);
	}

	/**
	 * Really performs the Delete into the database
	 * 
	 * @param sql The query to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected void doDeleteWhere(DeleteUsingStep<Record> deleteQuery) throws PuiDaoDeleteException {
		checkDataSource();
		try {
			jdbcTemplate.update(deleteQuery.getSQL());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnDeleteException(e);
		} catch (Exception e) {
			throw new PuiDaoDeleteException(new PuiDaoDataAccessException(e));
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ITableDao<ITableDto, ITableDto> getTableTranslationDao() {
		if (tableTranslationDao == null && daoRegistry.hasLanguageSupport(this)) {
			Class<? extends IDao> daoLangClass = daoRegistry.getDaoFromEntityName(daoRegistry.getTableLangName(this),
					false);
			tableTranslationDao = (ITableDao<ITableDto, ITableDto>) PuiApplicationContext.getInstance()
					.getBean(daoLangClass);
		}

		return tableTranslationDao;
	}

	@Override
	protected void addTranslationJoins(SelectJoinStep<? extends Record> select) {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());

		org.jooq.Field<?>[] masterFields = pkColumnNames.stream().map(c -> DSL.field(DSL.unquotedName(TABLE_PREFIX, c)))
				.collect(Collectors.toList()).toArray(new org.jooq.Field[] {});

		org.jooq.Field<?>[] relatedFields = pkColumnNames.stream()
				.map(c -> DSL.field(DSL.unquotedName(TABLE_LANG_PREFIX, c))).collect(Collectors.toList())
				.toArray(new org.jooq.Field[] {});

		select.leftJoin(DSL.table(daoRegistry.getTableLangName(this)).as(DSL.unquotedName(TABLE_LANG_PREFIX)))
				.on(DSL.row(masterFields).equal(relatedFields));
	}

	/**
	 * Get the default parameter for the given column in the SQL
	 * 
	 * @param columnName The column
	 * @return The parameter to use in the SQL
	 */
	protected String getParameterForSql(String columnName) {
		Field field = DtoRegistry.getJavaFieldFromColumnName(dtoClass, columnName);
		boolean isPuiObject = IPuiObject.class.isAssignableFrom(field.getType());
		boolean isJson = DtoRegistry.getJsonFields(dtoClass).contains(columnName);
		if (isPuiObject && isJson) {
			return dbHelper.getSqlCastToJson();
		} else {
			return PARAMETER;
		}
	}

	private Object convertPuiObjectToJsonValue(Field field, Object value) {
		if (value == null) {
			return value;
		}

		if (IPuiObject.class.isAssignableFrom(field.getType())) {
			return GsonSingleton.getSingleton().getGson().toJson(value);
		} else {
			return value;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends ITableDao<TPK, T>> getDaoClass() {
		return (Class<? extends ITableDao<TPK, T>>) super.getDaoClass();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<TPK> getDtoPkClass() {
		if (ITableDto.class.isAssignableFrom(dtoClass)) {
			// is a table
			return (Class<TPK>) dtoClass.getSuperclass();
		} else {
			// is a view
			return (Class<TPK>) dtoClass;
		}
	}

}