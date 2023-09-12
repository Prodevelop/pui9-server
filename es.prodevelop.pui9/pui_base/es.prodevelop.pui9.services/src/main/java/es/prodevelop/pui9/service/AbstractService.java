package es.prodevelop.pui9.service;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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

import es.prodevelop.pui9.eventlistener.PuiEventLauncher;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.exceptions.AbstractPuiDaoException;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoDuplicatedException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiDaoNoNumericColumnException;
import es.prodevelop.pui9.exceptions.PuiDaoNotExistsException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiDaoUpdateException;
import es.prodevelop.pui9.exceptions.PuiServiceCopyRegistryException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceExistsException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceNewException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.service.interfaces.IService;
import es.prodevelop.pui9.service.registry.ServiceRegistry;
import es.prodevelop.pui9.utils.PuiDateUtil;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * This class is the abstract implementation of all the {@link IService}
 * services of PUI. All the services that uses the stack of PUI should use this
 * class in its Services.
 * <p>
 * This class offers a lot of methods that could be used to manage registries,
 * avoiding using directly the {@link IDao} methods.
 * <p>
 * If you want to use a service, you must to create an Autowired property using
 * the interface of this Service. It is highly recommended to use and reference
 * the Services instead of the DAOs
 * 
 * @param <TPK>  The {@link ITableDto} PK for the Table (if the service has one
 *               associated)
 * @param <T>    The whole {@link ITableDto} for the Table (if the service has
 *               one associated)
 * @param <V>    The {@link IViewDto} for the View (if the service has one
 *               associated)
 * @param <DAO>  The {@link ITableDao} for the Table
 * @param <VDAO> The {@link IViewDao} for the View
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractService<TPK extends ITableDto, T extends TPK, V extends IViewDto, DAO extends ITableDao<TPK, T>, VDAO extends IViewDao<V>>
		implements IService<TPK, T, V, DAO, VDAO> {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private DAO tableDao;

	@Autowired
	private VDAO viewDao;

	@Autowired
	private ServiceRegistry serviceRegistry;

	@Autowired
	private DaoRegistry daoRegistry;

	private List<MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?>> multiValuedAttributes = new ArrayList<>();

	@Autowired
	private PuiEventLauncher eventLauncher;

	/**
	 * Registers the Service into ServiceRegistry
	 */
	@PostConstruct
	private void postConstruct() {
		serviceRegistry.registerService(getClass(), getTableDtoPkClass(), getTableDtoClass(), getViewDtoClass(),
				getTableDao().getClass(), getViewDao().getClass());
		addMultiValuedAttributes();
	}

	@Override
	public DAO getTableDao() {
		return tableDao;
	}

	@Override
	public VDAO getViewDao() {
		return viewDao;
	}

	@Override
	public Class<T> getTableDtoClass() {
		return tableDao.getDtoClass();
	}

	@Override
	public Class<TPK> getTableDtoPkClass() {
		return tableDao.getDtoPkClass();
	}

	@Override
	public Class<V> getViewDtoClass() {
		return viewDao.getDtoClass();
	}

	/**
	 * Get the DaoRegistry bean
	 * 
	 * @return The DaoRegistry
	 */
	protected DaoRegistry getDaoRegistry() {
		return daoRegistry;
	}

	/**
	 * Get the ServiceRegistry bean
	 * 
	 * @return The ServiceRegistry
	 */
	protected ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	/**
	 * Get the PUI Event Launcher, that allows to fire PUI Events over the
	 * application. See {@link PuiEvent} class
	 * 
	 * @return
	 */
	protected PuiEventLauncher getEventLauncher() {
		return eventLauncher;
	}

	/**
	 * Override this method to add multivalued attributes configurations. Use
	 * {@link #addMultiValuedAttribute(MultiValuedAttribute)} method in order to add
	 * every multivalued attribute. Remember that the join should be done with the
	 * Java Fields (not with the Columns)
	 */
	protected void addMultiValuedAttributes() {
		// nothing to do
	}

	/**
	 * In order to call this method properly, override
	 * {@link #addMultiValuedAttributes()} method and call it so many times as
	 * multivalued attributes you have.
	 * 
	 * @param multivaluedAttribute The multivalued attribute to be added
	 */
	protected final void addMultiValuedAttribute(MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?> multivaluedAttribute) {
		multiValuedAttributes.add(multivaluedAttribute);
	}

	@Override
	public T getNew() throws PuiServiceNewException {
		return getNew(null);
	}

	@Override
	public T getNew(PuiLanguage language) throws PuiServiceNewException {
		if (language == null) {
			language = PuiUserSession.getSessionLanguage();
		}

		try {
			T dto = getTableDtoClass().newInstance();
			PuiLanguageUtils.setLanguage(dto, language);
			afterGetMultivaluedAttributes(dto);
			afterNew(dto);

			return dto;
		} catch (InstantiationException | IllegalAccessException | PuiServiceException e) {
			throw new PuiServiceNewException(e);
		}
	}

	@Override
	public boolean exists(TPK dtoPk) throws PuiServiceExistsException {
		return exists(dtoPk, null);
	}

	@Override
	public boolean exists(TPK dtoPk, PuiLanguage language) throws PuiServiceExistsException {
		if (language == null) {
			language = PuiUserSession.getSessionLanguage();
		}

		try {
			PuiLanguageUtils.setLanguage(dtoPk, language);
			return tableDao.exists(dtoPk);
		} catch (PuiDaoFindException e) {
			throw new PuiServiceExistsException(e);
		}
	}

	@Override
	public T getByPk(TPK dtoPk) throws PuiServiceGetException {
		return getByPk(dtoPk, null);
	}

	@Override
	public T getByPk(TPK dtoPk, PuiLanguage language) throws PuiServiceGetException {
		if (language == null) {
			language = PuiUserSession.getSessionLanguage();
		}

		try {
			T dto = tableDao.findOne(dtoPk, language);
			if (dto != null) {
				afterGetMultivaluedAttributes(dto);
				afterGet(dto);
			} else {
				throw new PuiDaoNotExistsException(dtoPk.toString());
			}

			return dto;
		} catch (AbstractPuiDaoException | PuiServiceException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public V getViewByPk(final TPK dtoPk) throws PuiServiceGetException {
		return getViewByPk(dtoPk, PuiUserSession.getSessionLanguage());
	}

	@Override
	public V getViewByPk(final TPK dtoPk, PuiLanguage language) throws PuiServiceGetException {
		try {
			List<String> columnNames = DtoRegistry.getAllColumnNames(dtoPk.getClass());
			FilterBuilder filterBuilder = FilterBuilder.newAndFilter();

			for (String columnName : columnNames) {
				Field field = DtoRegistry.getJavaFieldFromColumnName(dtoPk.getClass(), columnName);
				try {
					Object value = FieldUtils.readField(field, dtoPk, true);
					if (DtoRegistry.getStringFields(dtoPk.getClass()).contains(columnName)) {
						filterBuilder.addEqualsExact(columnName, (String) value);
					} else {
						filterBuilder.addEquals(columnName, value);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new PuiServiceGetException(e);
				}
			}

			return getViewDao().findOne(filterBuilder, language);
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public List<T> getAll() throws PuiServiceGetException {
		return getAll(null, null);
	}

	@Override
	public List<T> getAll(PuiLanguage language) throws PuiServiceGetException {
		return getAll(null, language);
	}

	@Override
	public List<T> getAll(OrderBuilder orderBuilder) throws PuiServiceGetException {
		return getAll(orderBuilder, null);
	}

	@Override
	public List<T> getAll(OrderBuilder orderBuilder, PuiLanguage language) throws PuiServiceGetException {
		if (language == null) {
			language = PuiUserSession.getSessionLanguage();
		}

		try {
			List<T> list = getTableDao().findAll(orderBuilder, language);

			for (T dto : list) {
				afterGetMultivaluedAttributes(dto);
				afterGet(dto);
			}
			return list;
		} catch (PuiDaoFindException | PuiServiceException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public List<T> getAllWhere(FilterBuilder filterBuilder) throws PuiServiceGetException {
		return getAllWhere(filterBuilder, null, null);
	}

	@Override
	public List<T> getAllWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiServiceGetException {
		return getAllWhere(filterBuilder, orderBuilder, null);
	}

	@Override
	public List<T> getAllWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiServiceGetException {
		if (language == null) {
			language = PuiUserSession.getSessionLanguage();
		}

		try {
			List<T> list = getTableDao().findWhere(filterBuilder, orderBuilder, language);

			for (T dto : list) {
				afterGetMultivaluedAttributes(dto);
				afterGet(dto);
			}
			return list;
		} catch (PuiDaoFindException | PuiServiceException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public List<V> getAllView() throws PuiServiceGetException {
		return getAllView(null);
	}

	@Override
	public List<V> getAllView(PuiLanguage language) throws PuiServiceGetException {
		if (language == null) {
			language = PuiUserSession.getSessionLanguage();
		}

		try {
			return viewDao.findAll(language);
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public List<V> getAllViewWhere(FilterBuilder filterBuilder) throws PuiServiceGetException {
		return getAllViewWhere(filterBuilder, null, null);
	}

	@Override
	public List<V> getAllViewWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder)
			throws PuiServiceGetException {
		return getAllViewWhere(filterBuilder, orderBuilder, null);
	}

	@Override
	public List<V> getAllViewWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiServiceGetException {
		if (language == null) {
			language = PuiUserSession.getSessionLanguage();
		}

		try {
			return getViewDao().findWhere(filterBuilder, orderBuilder, language);
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public SearchResponse<T> searchTable(SearchRequest req) throws PuiServiceGetException {
		try {
			SearchResponse<T> resp = getTableDao().findPaginated(req);
			for (T dto : resp.getData()) {
				try {
					afterGet(dto);
				} catch (PuiServiceException e) {
					// do nothing
				}
			}
			return resp;
		} catch (PuiDaoListException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public SearchResponse<V> searchView(SearchRequest req) throws PuiServiceGetException {
		try {
			SearchResponse<V> res = getViewDao().findPaginated(req);
			afterSearchView(req, res);
			return res;
		} catch (PuiDaoListException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public T insert(T dto) throws PuiServiceInsertException {
		try {
			logger.debug("Insert: " + getTableDtoClass().getSimpleName());

			setAutoincrementableValues(Collections.singletonList(dto));

			// before check existence, remove all the sequence column values
			List<String> sequenceColumns = DtoRegistry.getSequenceColumns(getTableDtoPkClass());
			for (String seqCol : sequenceColumns) {
				Field seqField = DtoRegistry.getJavaFieldFromColumnName(getTableDtoPkClass(), seqCol);
				try {
					FieldUtils.writeField(seqField, dto, null, true);
				} catch (IllegalAccessException e) {
					// do nothing
				}
			}

			if (exists(dto.createPk())) {
				throw new PuiServiceInsertException(new PuiDaoDuplicatedException());
			}

			if (PuiLanguageUtils.hasLanguageSupport(dto) && PuiLanguageUtils.getLanguage(dto) == null) {
				PuiLanguageUtils.setLanguage(dto, PuiUserSession.getSessionLanguage());
			}

			beforeInsert(dto);
			dto = tableDao.insert(dto);
			afterInsertMultivaluedAttributes(dto);
			afterInsert(dto);

			return dto;
		} catch (PuiDaoInsertException | PuiDaoNoNumericColumnException e) {
			throw new PuiServiceInsertException(e);
		} catch (PuiServiceInsertException e) {
			throw e;
		} catch (PuiServiceException e) {
			throw new PuiServiceInsertException(e);
		}
	}

	@Override
	public List<T> bulkInsert(List<T> dtoList) throws PuiServiceInsertException {
		if (ObjectUtils.isEmpty(dtoList)) {
			return Collections.emptyList();
		}

		try {
			logger.debug("Bulk Insert: " + getTableDtoClass().getSimpleName());

			setAutoincrementableValues(dtoList);

			// before check existence, remove all the sequence column values
			List<String> sequenceColumns = DtoRegistry.getSequenceColumns(getTableDtoPkClass());
			for (String seqCol : sequenceColumns) {
				for (T dto : dtoList) {
					Field seqField = DtoRegistry.getJavaFieldFromColumnName(getTableDtoPkClass(), seqCol);
					try {
						FieldUtils.writeField(seqField, dto, null, true);
					} catch (IllegalAccessException e) {
						// do nothing
					}
				}
			}

			if (PuiLanguageUtils.hasLanguageSupport(dtoList.get(0))) {
				for (T dto : dtoList) {
					if (PuiLanguageUtils.getLanguage(dto) == null) {
						PuiLanguageUtils.setLanguage(dto, PuiUserSession.getSessionLanguage());
					}
				}
			}

			beforeBulkInsert(dtoList);
			dtoList = tableDao.bulkInsert(dtoList);
			afterBulkInsert(dtoList);

			return dtoList;
		} catch (PuiDaoSaveException | PuiDaoNoNumericColumnException e) {
			throw new PuiServiceInsertException(e);
		} catch (PuiServiceException e) {
			throw new PuiServiceInsertException(e);
		}
	}

	@Override
	public T update(T dto) throws PuiServiceUpdateException {
		try {
			TPK dtoPk = dto.<TPK>createPk();
			logger.debug("Update: " + getTableDtoClass().getSimpleName());

			PuiLanguage language = PuiLanguageUtils.getLanguage(dto);

			T oldDto = null;
			if (language == null) {
				oldDto = getByPk(dtoPk);
			} else {
				oldDto = getByPk(dtoPk, language);
			}

			if (oldDto == null) {
				throw new PuiServiceGetException();
			}

			if (PuiLanguageUtils.hasLanguageSupport(dto) && PuiLanguageUtils.getLanguage(dto) == null) {
				PuiLanguageUtils.setLanguage(dto, PuiUserSession.getSessionLanguage());
			}

			beforeUpdateMultivaluedAttributes(dto);
			beforeUpdate(oldDto, dto);
			dto = tableDao.update(dto);
			afterUpdate(oldDto, dto);

			return dto;
		} catch (PuiDaoUpdateException | PuiServiceException e) {
			throw new PuiServiceUpdateException(e);
		}
	}

	@Override
	public List<T> bulkUpdate(List<T> dtoList) throws PuiServiceUpdateException {
		if (ObjectUtils.isEmpty(dtoList)) {
			return Collections.emptyList();
		}

		try {
			logger.debug("Bulk Update: " + getTableDtoClass().getSimpleName());

			if (PuiLanguageUtils.hasLanguageSupport(dtoList.get(0))) {
				for (T dto : dtoList) {
					if (PuiLanguageUtils.getLanguage(dto) == null) {
						PuiLanguageUtils.setLanguage(dto, PuiUserSession.getSessionLanguage());
					}
				}
			}

			beforeBulkUpdate(dtoList);
			dtoList = tableDao.bulkUpdate(dtoList);
			afterBulkUpdate(dtoList);

			return dtoList;
		} catch (PuiDaoSaveException e) {
			throw new PuiServiceUpdateException(e);
		} catch (PuiServiceException e) {
			throw new PuiServiceUpdateException(e);
		}
	}

	@Override
	public T patch(TPK dtoPk, Map<String, Object> fieldValuesMap) throws PuiServiceUpdateException {
		logger.debug("Patch: " + getTableDtoClass().getSimpleName());

		// convert all possible columns to fields
		Map<String, Object> fieldValuesMapCopy = new LinkedHashMap<>();
		fieldValuesMap.forEach((columnName, value) -> {
			String fieldName = DtoRegistry.getFieldNameFromColumnName(getTableDtoClass(), columnName);
			if (fieldName == null) {
				fieldName = columnName;
			}
			fieldValuesMapCopy.put(fieldName, value);
		});

		for (Iterator<Entry<String, Object>> it = fieldValuesMapCopy.entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> next = it.next();
			String attributeFromMap = next.getKey();
			String columnName = DtoRegistry.getFieldNameFromColumnName(getTableDtoClass(), attributeFromMap);
			if ((DtoRegistry.getDateTimeFields(getTableDtoClass()).contains(attributeFromMap)
					|| DtoRegistry.getDateTimeFields(getTableDtoClass()).contains(columnName))
					&& next.getValue() instanceof String) {
				Instant instant = PuiDateUtil.stringToInstant((String) next.getValue());
				next.setValue(instant);
			}
		}

		try {
			T oldDto = getByPk(dtoPk);
			beforePatch(dtoPk, fieldValuesMapCopy);

			T dto = PuiObjectUtils.copyDeepObject(oldDto);

			PuiObjectUtils.populateObject(dto, fieldValuesMapCopy);

			beforeUpdate(oldDto, dto);
			tableDao.patch(dtoPk, fieldValuesMapCopy);
			afterUpdate(oldDto, dto);

			try {
				// try to replace original values in the map with the new. May throw an
				// exception if the original map is inmutable
				fieldValuesMap.clear();
				fieldValuesMap.putAll(fieldValuesMapCopy);
			} catch (Exception e) {
				// do nothing
			}

			return dto;
		} catch (PuiDaoSaveException | PuiServiceException e) {
			throw new PuiServiceUpdateException(e);
		}
	}

	@Override
	public void bulkPatch(List<TPK> dtoPkList, Map<String, Object> fieldValuesMap) throws PuiServiceUpdateException {
		if (ObjectUtils.isEmpty(dtoPkList)) {
			return;
		}

		// convert all possible columns to fields
		Map<String, Object> fieldValuesMapCopy = new LinkedHashMap<>();
		fieldValuesMap.forEach((columnName, value) -> {
			String fieldName = DtoRegistry.getFieldNameFromColumnName(getTableDtoClass(), columnName);
			if (fieldName == null) {
				fieldName = columnName;
			}
			fieldValuesMapCopy.put(fieldName, value);
		});

		for (Iterator<Entry<String, Object>> it = fieldValuesMapCopy.entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> next = it.next();
			String attributeFromMap = next.getKey();
			String columnName = DtoRegistry.getFieldNameFromColumnName(getTableDtoClass(), attributeFromMap);
			if ((DtoRegistry.getDateTimeFields(getTableDtoClass()).contains(attributeFromMap)
					|| DtoRegistry.getDateTimeFields(getTableDtoClass()).contains(columnName))
					&& next.getValue() instanceof String) {
				Instant instant = PuiDateUtil.stringToInstant((String) next.getValue());
				next.setValue(instant);
			}
		}

		try {
			logger.debug("Bulk Update: " + getTableDtoClass().getSimpleName());
			tableDao.bulkPatch(dtoPkList, fieldValuesMapCopy);
		} catch (PuiDaoSaveException e) {
			throw new PuiServiceUpdateException(e);
		}

		try {
			// try to replace original values in the map with the new. May throw an
			// exception if the original map is inmutable
			fieldValuesMap.clear();
			fieldValuesMap.putAll(fieldValuesMapCopy);
		} catch (Exception e) {
			// do nothing
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public TPK delete(TPK dtoPk) throws PuiServiceDeleteException {
		if (dtoPk == null) {
			return null;
		}

		try {
			logger.debug("Delete: " + getTableDtoClass().getSimpleName());

			T dto = null;
			if (getTableDtoPkClass().equals(dtoPk.getClass())) {
				dto = getByPk(dtoPk);
			} else {
				dto = (T) dtoPk;
				dtoPk = dto.createPk();
			}

			if (dto != null) {
				beforeDeleteMultivaluedAttributes(dto);
				beforeDelete(dto);

				dtoPk = tableDao.delete(dtoPk);

				afterDelete(dto);
			}

			return dtoPk;
		} catch (PuiDaoDeleteException e) {
			throw new PuiServiceDeleteException(e);
		} catch (PuiServiceException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	public List<TPK> bulkDelete(List<? extends TPK> dtoPkList) throws PuiServiceDeleteException {
		if (ObjectUtils.isEmpty(dtoPkList)) {
			return Collections.emptyList();
		}

		try {
			logger.debug("Bulk Delete: " + getTableDtoClass().getSimpleName());

			List<TPK> realDtoList = new ArrayList<>();
			for (TPK dto : dtoPkList) {
				if (tableDao.getDtoPkClass().equals(dto.getClass())) {
					realDtoList.add(dto);
				} else {
					realDtoList.add(dto.createPk());
				}
			}

			beforeBulkDelete(realDtoList);
			realDtoList = tableDao.bulkDelete(realDtoList);
			afterBulkDelete(realDtoList);

			return realDtoList;
		} catch (PuiDaoDeleteException e) {
			throw new PuiServiceDeleteException(e);
		} catch (PuiServiceException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	public void deleteAll() throws PuiServiceDeleteException {
		try {
			tableDao.deleteAll();
		} catch (PuiDaoDeleteException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	public void deleteAll(PuiLanguage language) throws PuiServiceDeleteException {
		try {
			tableDao.deleteAll(language);
		} catch (PuiDaoDeleteException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	public T copy(TPK dtoPk) throws PuiServiceCopyRegistryException {
		try {
			T dto = getByPk(dtoPk);
			DtoRegistry.getPkFields(getTableDtoClass()).forEach(pkFieldName -> {
				try {
					DtoRegistry.getJavaFieldFromFieldName(getTableDtoClass(), pkFieldName).set(dto, null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// do nothing
				}
			});
			afterCopy(dtoPk, dto);
			return dto;
		} catch (PuiServiceException e) {
			throw new PuiServiceCopyRegistryException(e);
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
		List<String> fieldNames = DtoRegistry.getAutoincrementableFieldNames(getTableDtoClass());

		for (String fieldName : fieldNames) {
			try {
				Field field = DtoRegistry.getJavaFieldFromFieldName(getTableDtoClass(), fieldName);
				if (field.get(dtoList.get(0)) != null) {
					continue;
				}

				String columnName = DtoRegistry.getColumnNameFromFieldName(getTableDtoClass(), fieldName);
				FilterBuilder filterBuilder = getAutoincrementableColumnFilter(dtoList.get(0), columnName);

				Number nextId = getTableDao().getNextValue(columnName, filterBuilder);

				for (T dto : dtoList) {
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

	/**
	 * Do something before inserting the registry
	 * 
	 * @param dto The registry about to be inserted
	 */
	protected void beforeInsert(T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something before the bulk insert operation
	 * 
	 * @param dtoList The list of DTO to be inserted
	 */
	protected void beforeBulkInsert(List<T> dtoList) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something before updating the registry
	 * 
	 * @param oldDto The old registry before being updated
	 * @param dto    The new registry to be updated
	 */
	protected void beforeUpdate(T oldDto, T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something before the bulk update operation
	 * 
	 * @param dtoList The list of DTO to be updated
	 */
	protected void beforeBulkUpdate(List<T> dtoList) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something before patching the registry. Use it mostly to modify the fields
	 * to be updated
	 * 
	 * @param dtoPk          The PK of the registry to be updated
	 * @param fieldValuesMap The map with the fields to be patched and its new
	 *                       values
	 */
	protected void beforePatch(TPK dtoPk, Map<String, Object> fieldValuesMap) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something before deleting the registry
	 * 
	 * @param dto The registry to be deleted
	 */
	protected void beforeDelete(T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something before the bulk delete operation
	 * 
	 * @param dtoList The list of DTO to be deleted
	 */
	protected void beforeBulkDelete(List<TPK> dtoList) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after getting an existing registry
	 * 
	 * @param dto The existing registry
	 */
	protected void afterGet(T dto) throws PuiServiceException {
		// Nothing to do
	}

	/**
	 * Do something after creating a new registry
	 * 
	 * @param dto The new registry
	 */
	protected void afterNew(T dto) throws PuiServiceException {
		// Nothing to do
	}

	/**
	 * Do something after inserting the registry
	 * 
	 * @param dto The inserted registry
	 */
	protected void afterInsert(T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after the bulk insert operation
	 * 
	 * @param dtoList The list of DTO to be inserted
	 */
	protected void afterBulkInsert(List<T> dtoList) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after updating the registry
	 * 
	 * @param oldDto The old registry before being updated
	 * @param dto    The new updated registry
	 */
	protected void afterUpdate(T oldDto, T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after the bulk update operation
	 * 
	 * @param dtoList The list of DTO to be updated
	 */
	protected void afterBulkUpdate(List<T> dtoList) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after deleting the registry
	 * 
	 * @param dto The deleted registry
	 */
	protected void afterDelete(T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after the bulk delete operation
	 * 
	 * @param dtoList The list of DTO to be deleted
	 */
	protected void afterBulkDelete(List<TPK> dtoList) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after copying the registry
	 * 
	 * @param dtoPk The PK of the copied registry
	 * @param dto   The copied registry
	 */
	protected void afterCopy(TPK dtoPk, T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after searching against the view
	 * 
	 * @param req The original request data
	 * @param res The result of the list
	 */
	protected void afterSearchView(SearchRequest req, SearchResponse<V> res) {
		// nothing to do
	}

	/**
	 * This method is called after getting an existing registry or creating a new
	 * one in order to populate the multivalued attributes
	 * 
	 * @param dto The registry to populated its multivalued attributes
	 */
	private void afterGetMultivaluedAttributes(T dto) {
		for (MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?> mva : multiValuedAttributes) {
			mva.populate(dto);
		}
	}

	/**
	 * This method is called after inserting a registry in order to evaluate the
	 * multivalued attributes
	 * 
	 * @param dto The registry to evaluate its multivalued attributes
	 * @throws PuiServiceInsertException If an error is throws while inserting the
	 *                                   multivalued attributes
	 */
	private void afterInsertMultivaluedAttributes(T dto) throws PuiServiceInsertException {
		for (MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?> mva : multiValuedAttributes) {
			mva.insert(dto);
		}
	}

	/**
	 * This method is called before updating a registry in order to evaluate the
	 * multivalued attributes
	 * 
	 * @param dto The registry to evaluate its multivalued attributes
	 * @throws PuiServiceInsertException If an error is throws while updating the
	 *                                   multivalued attributes
	 */
	private void beforeUpdateMultivaluedAttributes(T dto) throws PuiServiceUpdateException {
		for (MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?> mva : multiValuedAttributes) {
			mva.update(dto);
		}
	}

	/**
	 * This method is called before deleting a registry in order to evaluate the
	 * multivalued attributes
	 * 
	 * @param dto The registry to evaluate its multivalued attributes
	 * @throws PuiServiceInsertException If an error is throws while deleting the
	 *                                   multivalued attributes
	 */
	private void beforeDeleteMultivaluedAttributes(T dto) throws PuiServiceDeleteException {
		for (MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?> mva : multiValuedAttributes) {
			mva.delete(dto, true);
		}
	}

}
