package es.prodevelop.pui9.model.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.db.helpers.IDatabaseHelper;
import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiDaoNoNumericColumnException;
import es.prodevelop.pui9.exceptions.PuiDaoSumException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.order.OrderDirection;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

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
public abstract class AbstractDatabaseDao<T extends IDto> extends AbstractDao<T> {

	protected static final String PARAMETER = "?";

	@Autowired(required = false)
	protected IDatabaseHelper dbHelper;

	protected JdbcTemplate jdbcTemplate;
	private DaoRowMapper<T> rowMapper;

	@Autowired(required = false)
	private void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Registers the DAO into the DaoRegistry
	 */
	protected void postConstruct() {
		super.postConstruct();
		rowMapper = getRowMapper();
	}

	protected DaoRowMapper<T> getRowMapper() {
		return new DaoRowMapper<>(this, dtoClass);
	}

	/**
	 * Get the name of the entity, to be used in the SQL operations
	 * 
	 * @return The entity name
	 */
	protected String getEntityName() {
		return daoRegistry.getEntityName(this);
	}

	@Override
	public Long count() throws PuiDaoCountException {
		return count(null);
	}

	@Override
	public Long count(FilterBuilder filterBuilder) throws PuiDaoCountException {
		return count(null, false, filterBuilder);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Long count(String column, boolean distinct, FilterBuilder filterBuilder) throws PuiDaoCountException {
		SelectSelectStep<Record1<Integer>> count;
		if (!ObjectUtils.isEmpty(column)) {
			String prefix = null;
			if (DtoRegistry.getAllColumnNames(dtoClass).contains(column)) {
				prefix = TABLE_PREFIX;
			} else if (DtoRegistry.getLangColumnNames(dtoClass).contains(column)) {
				prefix = TABLE_LANG_PREFIX;
			}

			if (prefix == null) {
				return null;
			}

			if (distinct) {
				count = dbHelper.getDSLContext().select(DSL.countDistinct(DSL.field(DSL.unquotedName(prefix, column))));
			} else {
				count = dbHelper.getDSLContext().select(DSL.count(DSL.field(DSL.unquotedName(prefix, column))));
			}
		} else {
			count = dbHelper.getDSLContext().select(DSL.count());
		}

		count.from(DSL.table(getEntityName()).as(DSL.unquotedName(TABLE_PREFIX)));

		if (daoRegistry.hasLanguageSupport(this)) {
			addTranslationJoins((SelectJoinStep<? extends Record>) count);
		}

		if (filterBuilder != null) {
			String filter = dbHelper.processFilters(dtoClass, filterBuilder.asFilterGroup(), true);
			count.where(filter);
		}

		return performCount(count);
	}

	/**
	 * Really performs the Count against the database
	 * 
	 * @param sql The sql to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected Long performCount(Select<Record1<Integer>> query) throws PuiDaoCountException {
		checkDataSource();
		try {
			return jdbcTemplate.queryForObject(query.getSQL(), Long.class);
		} catch (DataAccessException e) {
			throw new PuiDaoCountException(e);
		}
	}

	@Override
	public BigDecimal sum(String column) throws PuiDaoSumException {
		return sum(column, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public BigDecimal sum(String column, FilterBuilder filterBuilder) throws PuiDaoSumException {
		if (!DtoRegistry.getNumericFields(dtoClass).contains(column)
				&& !DtoRegistry.getFloatingFields(dtoClass).contains(column)) {
			return null;
		}

		String prefix = null;
		if (DtoRegistry.getAllColumnNames(dtoClass).contains(column)) {
			prefix = TABLE_PREFIX;
		} else if (DtoRegistry.getLangColumnNames(dtoClass).contains(column)) {
			prefix = TABLE_LANG_PREFIX;
		}

		if (prefix == null) {
			return null;
		}

		SelectSelectStep<Record1<BigDecimal>> sum = dbHelper.getDSLContext()
				.select(DSL.sum(DSL.field(DSL.unquotedName(prefix, column), BigDecimal.class)));

		sum.from(DSL.table(getEntityName()).as(DSL.unquotedName(TABLE_PREFIX)));

		if (daoRegistry.hasLanguageSupport(this)) {
			addTranslationJoins((SelectJoinStep<? extends Record>) sum);
		}

		if (!filterBuilder.isEmpty()) {
			String filter = dbHelper.processFilters(dtoClass, filterBuilder.asFilterGroup(), true);
			sum.where(filter);
		}

		return performSum(sum);
	}

	/**
	 * Really performs the Sum against the database
	 * 
	 * @param sql The sql to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected BigDecimal performSum(Select<Record1<BigDecimal>> query) throws PuiDaoSumException {
		checkDataSource();
		try {
			return jdbcTemplate.queryForObject(query.getSQL(), BigDecimal.class);
		} catch (DataAccessException e) {
			throw new PuiDaoSumException(e);
		}
	}

	@Override
	public T findOne(FilterBuilder filterBuilder) throws PuiDaoFindException {
		return findOne(filterBuilder, (PuiLanguage) null);
	}

	@Override
	public T findOne(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiDaoFindException {
		List<T> list = doFindWhere(filterBuilder, orderBuilder, null);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public T findOne(FilterBuilder filterBuilder, PuiLanguage language) throws PuiDaoFindException {
		List<T> list = doFindWhere(filterBuilder, null, language);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<T> findAll() throws PuiDaoFindException {
		return doFindWhere(null, null, null);
	}

	@Override
	public List<T> findAll(OrderBuilder orderBuilder) throws PuiDaoFindException {
		return doFindWhere(null, orderBuilder, null);
	}

	@Override
	public List<T> findAll(PuiLanguage language) throws PuiDaoFindException {
		return doFindWhere(null, null, language);
	}

	@Override
	public List<T> findAll(OrderBuilder orderBuilder, PuiLanguage language) throws PuiDaoFindException {
		return doFindWhere(null, orderBuilder, language);
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder) throws PuiDaoFindException {
		return doFindWhere(filterBuilder, null, null);
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiDaoFindException {
		return doFindWhere(filterBuilder, orderBuilder, null);
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder, PuiLanguage language) throws PuiDaoFindException {
		return doFindWhere(filterBuilder, null, language);
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiDaoFindException {
		return doFindWhere(filterBuilder, orderBuilder, language);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Number> N getNextValue(String columnName, FilterBuilder filterBuilder)
			throws PuiDaoNoNumericColumnException {
		N value = getMaxValue(columnName, filterBuilder);

		if (value instanceof BigDecimal) {
			value = (N) ((BigDecimal) value).add(BigDecimal.ONE);
		} else if (value instanceof Integer) {
			value = (N) Integer.valueOf(((Integer) value) + 1);
		} else if (value instanceof Long) {
			value = (N) Long.valueOf(((Long) value) + 1L);
		} else if (value instanceof Double) {
			value = (N) Double.valueOf(((Double) value) + 1);
		} else if (value instanceof Float) {
			value = (N) Float.valueOf(((Float) value) + 1);
		} else if (value instanceof BigInteger) {
			value = (N) ((BigInteger) value).add(BigInteger.ONE);
		}

		return value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Number> N getMaxValue(String columnName, FilterBuilder filterBuilder)
			throws PuiDaoNoNumericColumnException {
		String auxFieldName = DtoRegistry.getFieldNameFromColumnName(dtoClass, columnName);
		if (auxFieldName == null) {
			auxFieldName = columnName;
		}

		if (!DtoRegistry.getNumericFields(dtoClass).contains(auxFieldName)
				&& !DtoRegistry.getFloatingFields(dtoClass).contains(auxFieldName)) {
			throw new PuiDaoNoNumericColumnException(columnName);
		}

		String auxColumnName = DtoRegistry.getColumnNameFromFieldName(dtoClass, columnName);
		if (auxColumnName != null) {
			columnName = auxColumnName;
		}

		String prefix = null;
		if (DtoRegistry.getAllColumnNames(dtoClass).contains(columnName)) {
			prefix = TABLE_PREFIX;
		} else if (DtoRegistry.getLangColumnNames(dtoClass).contains(columnName)) {
			prefix = TABLE_LANG_PREFIX;
		}

		if (prefix == null) {
			return null;
		}

		SelectJoinStep<Record1<Object>> max = dbHelper.getDSLContext()
				.select(DSL.max(DSL.field(DSL.unquotedName(prefix, columnName))))
				.from(DSL.table(getEntityName()).as(DSL.unquotedName(TABLE_PREFIX)));

		if (daoRegistry.hasLanguageSupport(this)) {
			addTranslationJoins(max);
		}

		if (filterBuilder != null) {
			String filter = dbHelper.processFilters(dtoClass, filterBuilder.asFilterGroup(), true);
			if (!ObjectUtils.isEmpty(filter)) {
				max.where(filter);
			}
		}

		String fieldName = DtoRegistry.getFieldNameFromColumnName(dtoClass, columnName);
		Field field = DtoRegistry.getJavaFieldFromFieldName(dtoClass, fieldName);
		N value = null;
		try {
			value = performMaxValue(max.getSQL(), field.getType());
		} catch (Exception e) {
			value = null;
		}

		if (value == null) {
			// if no values, instantiate a new one
			try {
				value = (N) field.getType().getConstructor(String.class).newInstance("0");
			} catch (Exception e) {
				return null;
			}
		}

		return value;
	}

	/**
	 * Really performs the Max Value against the database
	 * 
	 * @param sql       The sql to be executed
	 * @param fieldType The type of the field
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	@SuppressWarnings("unchecked")
	protected <N extends Number> N performMaxValue(String sql, Class<?> fieldType) {
		checkDataSource();
		return (N) jdbcTemplate.queryForObject(sql, fieldType);
	}

	@Override
	public void executeQuery(Query query) throws PuiDaoFindException {
		if (query == null) {
			return;
		}

		checkDataSource();
		try {
			jdbcTemplate.execute(query.getSQL());
		} catch (DataAccessException e) {
			throw new PuiDaoFindException(e);
		}
	}

	@Override
	public List<T> executeCustomQuery(Query query) throws PuiDaoFindException {
		if (query == null) {
			return Collections.emptyList();
		}

		checkDataSource();
		try {
			return jdbcTemplate.query(query.getSQL(), rowMapper);
		} catch (DataAccessException e) {
			throw new PuiDaoFindException(e);
		}
	}

	@Override
	public List<T> executeCustomQueryWithParameters(Query query, List<Object> parameters) throws PuiDaoFindException {
		if (query == null) {
			return Collections.emptyList();
		}

		checkDataSource();
		try {
			return jdbcTemplate.query(query.getSQL(), rowMapper, parameters.toArray());
		} catch (DataAccessException e) {
			throw new PuiDaoFindException(e);
		}
	}

	@Override
	public void executePaginagedOperation(SearchRequest req, Consumer<T> consumer, Consumer<List<T>> bulkConsumer) {
		req.setPage(SearchRequest.DEFAULT_PAGE);
		req.setPerformCount(false);

		while (true) {
			List<T> list;
			try {
				list = findPaginated(req).getData();
			} catch (PuiDaoListException e) {
				list = Collections.emptyList();
			}

			if (ObjectUtils.isEmpty(list)) {
				break;
			}

			if (consumer != null) {
				for (T obj : list) {
					consumer.accept(obj);
				}
			} else if (bulkConsumer != null) {
				bulkConsumer.accept(list);
			}

			req.setPage(req.getPage() + 1);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public SearchResponse<T> findPaginated(SearchRequest req) throws PuiDaoListException {
		req.setDtoClass(dtoClass);
		if (req.isFromClient() && PuiUserSession.getCurrentSession() != null) {
			req.setZoneId(PuiUserSession.getCurrentSession().getZoneId());
		}
		FilterBuilder filterBuilder = req.buildSearchFilter(dtoClass);
		OrderBuilder orderBuilder = req.createOrderForSearch();

		if (!ObjectUtils.isEmpty(req.getQueryLang()) && PuiLanguageUtils.hasLanguageSupport(dtoClass)) {
			FilterBuilder langBuilder = FilterBuilder.newOrFilter()
					.addEqualsExact(IDto.LANG_COLUMN_NAME, req.getQueryLang()).addIsNull(IDto.LANG_COLUMN_NAME);

			FilterBuilder filterBuilderAux = filterBuilder;

			filterBuilder = FilterBuilder.newAndFilter();
			filterBuilder.addGroup(langBuilder);
			filterBuilder.addGroup(filterBuilderAux);
		}

		String filters = dbHelper.processFilters(dtoClass, filterBuilder.asFilterGroup(), true);
		String quickSearch = null;
		if (!ObjectUtils.isEmpty(req.getQueryFields())) {
			quickSearch = dbHelper.processSearchText(dtoClass, req.getQueryFields(), req.getQueryText(),
					req.getZoneId());
		} else if (!ObjectUtils.isEmpty(req.getQueryFieldText())) {
			quickSearch = dbHelper.processSearchText(dtoClass, req.getQueryFieldText(), req.getZoneId());
		}

		String where;
		if (ObjectUtils.isEmpty(filters) && ObjectUtils.isEmpty(quickSearch)) {
			where = null;
		} else if (!ObjectUtils.isEmpty(filters) && ObjectUtils.isEmpty(quickSearch)) {
			where = filters;
		} else if (ObjectUtils.isEmpty(filters) && !ObjectUtils.isEmpty(quickSearch)) {
			where = quickSearch;
		} else {
			where = "(" + filters + ") AND (" + quickSearch + ")";
		}

		SelectSelectStep<? extends Record> select;
		if (ObjectUtils.isEmpty(req.getColumns())) {
			select = dbHelper.getDSLContext().select(DSL.asterisk());
		} else {
			if (req.isDistinctValues()) {
				select = dbHelper.getDSLContext()
						.selectDistinct(req.getColumns().stream().map(DSL::field).collect(Collectors.toList()));
			} else {
				select = dbHelper.getDSLContext()
						.select(req.getColumns().stream().map(DSL::field).collect(Collectors.toList()));
				if (!ObjectUtils.isEmpty(req.getDistinctOnColumns())) {
					select.distinctOn(
							req.getDistinctOnColumns().stream().map(dc -> DSL.field(dc)).collect(Collectors.toList()));
				}
			}
		}

		select.from(DSL.table(getEntityName()).as(DSL.unquotedName(TABLE_PREFIX)));

		if (daoRegistry.hasLanguageSupport(this)) {
			addTranslationJoins((SelectJoinStep<? extends Record>) select);
		}

		if (!ObjectUtils.isEmpty(where)) {
			select.where(where);
		}

		if (orderBuilder != null && !ObjectUtils.isEmpty(orderBuilder.toString())) {
			for (Iterator<Order> it = orderBuilder.getOrders().iterator(); it.hasNext();) {
				if (!DtoRegistry.getAllColumnNames(dtoClass).contains(it.next().getColumn())) {
					it.remove();
				}
			}

			if (daoRegistry.hasLanguageSupport(this)) {
				orderBuilder.getOrders().forEach(order -> {
					String column = DtoRegistry.getColumnNameFromFieldName(dtoClass, order.getColumn());
					if (column == null) {
						return;
					}

					if (DtoRegistry.getColumnNames(dtoClass).contains(column)) {
						order.setColumn(TABLE_PREFIX + "." + column);
					} else {
						order.setColumn(TABLE_LANG_PREFIX + "." + column);
					}
				});
			}

			if (!ObjectUtils.isEmpty(orderBuilder.getOrders())) {
				select.orderBy(orderBuilder.getOrders().stream()
						.map(o -> o.getDirection().equals(OrderDirection.asc) ? DSL.field(o.getColumn()).asc()
								: DSL.field(o.getColumn()).desc())
						.collect(Collectors.toList()));
			}
		}

		Long total = 0L;
		if (req.isPerformCount()) {
			SelectJoinStep<Record1<Integer>> count = DSL.select(DSL.count(DSL.asterisk()))
					.from(DSL.table(getEntityName()).as(DSL.unquotedName(TABLE_PREFIX)));
			if (daoRegistry.hasLanguageSupport(this)) {
				addTranslationJoins(count);
			}

			if (!ObjectUtils.isEmpty(where)) {
				count.where(where);
			}

			try {
				total = performCount(count);
			} catch (PuiDaoCountException e) {
				throw new PuiDaoListException(e);
			}
		}

		Integer from = req.getPage() - 1;
		Integer size = req.getRows();
		List<T> list = getListPaginated(from, size, (SelectJoinStep<Record>) select);

		SearchResponse<T> res = new SearchResponse<>();

		for (String sumColumn : req.getSumColumns()) {
			BigDecimal sum;
			try {
				sum = sum(sumColumn, filterBuilder);
			} catch (PuiDaoSumException e) {
				sum = null;
			}

			res.addSumData(sumColumn, sum);
		}

		res.setCurrentPage(from + 1);
		res.setCurrentRecords(list.size());
		res.setTotalRecords(total);
		res.setTotalPages(total / size);
		if (total % size > 0) {
			res.setTotalPages(res.getTotalPages() + 1);
		}
		res.setData(list);

		return res;
	}

	/**
	 * Builds the customized SQL using the given filter, order and language and
	 * executes it
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param orderBuilder  The order to be applied
	 * @param language      The language to be applied
	 * @return The list of filtered and ordered registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected List<T> doFindWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiDaoFindException {
		Select<Record> query = buildSelectSql(filterBuilder, orderBuilder, language);
		return executeCustomQuery(query);
	}

	/**
	 * Builds the SQL using the given filter, order and language
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param orderBuilder  The order to be applied
	 * @param language      The language to be applied
	 * @return The SQL
	 */
	private Select<Record> buildSelectSql(FilterBuilder filterBuilder, OrderBuilder orderBuilder,
			PuiLanguage language) {
		SelectJoinStep<Record> select = dbHelper.getDSLContext().select(DSL.asterisk())
				.from(DSL.table(getEntityName()).as(DSL.unquotedName(TABLE_PREFIX)));

		if (daoRegistry.hasLanguageSupport(this)) {
			addTranslationJoins(select);
		}

		Field field = DtoRegistry.getJavaFieldFromAllFields(dtoClass, IDto.LANG_FIELD_NAME);
		if (field != null && language != null && PuiLanguageUtils.hasLanguageSupport(dtoClass)) {
			FilterBuilder langFb = FilterBuilder.newOrFilter()
					.addEqualsExact(IDto.LANG_COLUMN_NAME, language.getIsocode()).addIsNull(IDto.LANG_COLUMN_NAME);

			FilterBuilder auxFb = filterBuilder;

			filterBuilder = FilterBuilder.newAndFilter().addGroup(auxFb).addGroup(langFb);
		}

		if (filterBuilder != null) {
			String filter = dbHelper.processFilters(dtoClass, filterBuilder.asFilterGroup(), true);
			if (!ObjectUtils.isEmpty(filter)) {
				select.where(filter);
			}
		}

		if (orderBuilder != null && !ObjectUtils.isEmpty(orderBuilder.toString())) {
			for (Iterator<Order> it = orderBuilder.getOrders().iterator(); it.hasNext();) {
				if (!DtoRegistry.getAllColumnNames(dtoClass).contains(it.next().getColumn())) {
					it.remove();
				}
			}

			if (daoRegistry.hasLanguageSupport(this)) {
				orderBuilder.getOrders().forEach(order -> {
					String column = DtoRegistry.getColumnNameFromFieldName(dtoClass, order.getColumn());
					if (column != null) {
						order.setColumn(column);
					}

					if (DtoRegistry.getColumnNames(dtoClass).contains(column)) {
						order.setColumn(TABLE_PREFIX + "." + column);
					} else {
						order.setColumn(TABLE_LANG_PREFIX + "." + column);
					}
				});
			}

			if (!ObjectUtils.isEmpty(orderBuilder.getOrders())) {
				select.orderBy(orderBuilder.getOrders().stream()
						.map(o -> o.getDirection().equals(OrderDirection.asc) ? DSL.field(o.getColumn()).asc()
								: DSL.field(o.getColumn()).desc())
						.collect(Collectors.toList()));
			}
		}

		return select;
	}

	protected void addTranslationJoins(SelectJoinStep<? extends Record> step) {
		// do nothing
	}

	/**
	 * Executes a paginated query, starting from the given page with the given size
	 * 
	 * @param page  The page from which return the results (start with 0)
	 * @param size  The number of items to be returned
	 * @param query The query to be executed
	 * @return The paginated list
	 * @throws PuiDaoListException If any SQL error while executing the statement is
	 *                             thrown
	 */
	private <S extends SelectJoinStep<Record>> List<T> getListPaginated(int page, int size, S select)
			throws PuiDaoListException {
		S newSelect = dbHelper.getSqlForPagination(page, size, select);
		return performListPaginated(newSelect);
	}

	/**
	 * Really performs the List paginated against the database
	 * 
	 * @param sql The sql to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected <S extends SelectJoinStep<Record>> List<T> performListPaginated(S select) throws PuiDaoListException {
		checkDataSource();
		try {
			return jdbcTemplate.query(select.getSQL(), rowMapper);
		} catch (DataAccessException e) {
			throw new PuiDaoListException(e);
		}
	}

	/**
	 * Override this method to execute some modification over the given DTO
	 * 
	 * @param dto The read DTO
	 */
	protected void customizeDto(T dto) {
	}

	protected void checkDataSource() {
		if (jdbcTemplate == null) {
			throw new DataSourceLookupFailureException("No database configured for this application");
		}
	}

}