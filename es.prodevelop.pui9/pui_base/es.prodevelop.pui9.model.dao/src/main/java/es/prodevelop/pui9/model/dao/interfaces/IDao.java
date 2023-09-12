package es.prodevelop.pui9.model.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import org.jooq.Query;
import org.jooq.impl.DSL;

import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiDaoNoNumericColumnException;
import es.prodevelop.pui9.exceptions.PuiDaoSumException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * This interface represents a DAO for JDBC approach. All the DAO classes that
 * belongs to JDBC, should inherit from this interface
 * <p>
 * If you want to use a DAO, you must to create an Autowired property using the
 * interface of this DAO.
 * 
 * @param <T> The whole DTO class that represents this DAO Interface
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IDao<T extends IDto> {

	String TABLE_PREFIX = "t1";
	String TABLE_LANG_PREFIX = "t2";

	/**
	 * Get the number of rows in the entity
	 * 
	 * @return The number of rows
	 * @throws PuiDaoCountException If any SQL error while executing the statement
	 *                              is thrown
	 */
	Long count() throws PuiDaoCountException;

	/**
	 * Get the number of rows in the table that accomplish the given filter
	 * 
	 * @param filterBuilder The filter applied to the operation
	 * @return The number of rows
	 * @throws PuiDaoCountException If any SQL error while executing the statement
	 *                              is thrown
	 */
	Long count(FilterBuilder filterBuilder) throws PuiDaoCountException;

	/**
	 * Get the number of rows in the table that accomplish the given filter. Also
	 * you can specify a column to make the count over it, and if you want to take
	 * into account only distinct values of this column
	 * 
	 * @param column        The column to make the count over it (<b>*</b> by
	 *                      default)
	 * @param distinct      If a column is specified, you can set if take into
	 *                      account only distinct values or not
	 * @param filterBuilder The filter applied to the operation
	 * @return The number of rows
	 * @throws PuiDaoCountException If any SQL error while executing the statement
	 *                              is thrown
	 */
	Long count(String column, boolean distinct, FilterBuilder filterBuilder) throws PuiDaoCountException;

	/**
	 * Sum the content of the given column for all the registries
	 * 
	 * @param column The column to make the sum over it
	 * @return The sum of the given column
	 * @throws PuiDaoSumException If any SQL error while executing the statement is
	 *                            thrown
	 */
	BigDecimal sum(String column) throws PuiDaoSumException;

	/**
	 * Sum the content of the given column for all the registries in the table that
	 * accomplish the given filter.
	 * 
	 * @param column        The column to make the sum over it
	 * @param filterBuilder The filter applied to the operation
	 * @return The sum of the given column with the specified filter
	 * @throws PuiDaoSumException If any SQL error while executing the statement is
	 *                            thrown
	 */
	BigDecimal sum(String column, FilterBuilder filterBuilder) throws PuiDaoSumException;

	/**
	 * Returns the next value for the given column that accomplished the given
	 * filter
	 * 
	 * @param columnName    The column to retrieve the next value
	 * @param filterBuilder The filter to be applied
	 * @return The next value for given column
	 * @throws PuiDaoNoNumericColumnException If the given column is not a numeric
	 *                                        column
	 */
	<N extends Number> N getNextValue(String columnName, FilterBuilder filterBuilder)
			throws PuiDaoNoNumericColumnException;

	/**
	 * Returns the max value for the given Column that accomplished the given
	 * condition
	 * 
	 * @param columnName    The column to retrieve the next value
	 * @param filterBuilder The filter to be applied
	 * @return The max value for given column
	 * @throws PuiDaoNoNumericColumnException If the given column is not a numeric
	 *                                        column
	 */
	<N extends Number> N getMaxValue(String columnName, FilterBuilder filterBuilder)
			throws PuiDaoNoNumericColumnException;

	/**
	 * Returns a single row matching the given filter
	 * 
	 * @param filterBuilder The filter to be applied
	 * @return The registry
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	T findOne(FilterBuilder filterBuilder) throws PuiDaoFindException;

	/**
	 * Returns a single row matching the given filter
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param orderBuilder  The order to be applied
	 * @return The registry
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	T findOne(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiDaoFindException;

	/**
	 * Returns a single row matching the given filter, using the given language
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param language      The language to be retrieved
	 * @return The registry for the specified language
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	T findOne(FilterBuilder filterBuilder, PuiLanguage language) throws PuiDaoFindException;

	/**
	 * Returns all the registries of this entity
	 * 
	 * @return All the registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findAll() throws PuiDaoFindException;

	/**
	 * Returns all the registries of this entity for the given language
	 * 
	 * @param language The language to be applied
	 * @return All the registries for the specified language
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findAll(PuiLanguage language) throws PuiDaoFindException;

	/**
	 * Returns all the registries of this entity ordered by the given order
	 * 
	 * @param orderBuilder The order to be applied
	 * @return All the registries order
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findAll(OrderBuilder orderBuilder) throws PuiDaoFindException;

	/**
	 * Returns all the registries of this entity for the given language ordered by
	 * the given order
	 * 
	 * @param orderBuilder The order to be applied
	 * @param language     The language to be applied
	 * @return All the registries for the specified language ordered
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findAll(OrderBuilder orderBuilder, PuiLanguage language) throws PuiDaoFindException;

	/**
	 * Returns a list of registries that accomplish the given filter
	 * 
	 * @param filterBuilder The filter to be applied
	 * @return The list of filtered registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findWhere(FilterBuilder filterBuilder) throws PuiDaoFindException;

	/**
	 * Returns a list of registries that accomplish the given filter, ordered by the
	 * given order
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param orderBuilder  The order to be applied
	 * @return The list of filtered and ordered registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiDaoFindException;

	/**
	 * Returns a list of registries that accomplish the given filter, and only for
	 * the specified language
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param language      The language to be applied
	 * @return The list of filtered registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findWhere(FilterBuilder filterBuilder, PuiLanguage language) throws PuiDaoFindException;

	/**
	 * Returns a list of registries that accomplish the given filter, ordered by the
	 * given order, and only for the specified language
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param orderBuilder  The order to be applied
	 * @param language      The language to be applied
	 * @return The list of filtered and ordered registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiDaoFindException;

	/**
	 * Execute a free query over the JDBC template. This Query is build on JOOQ
	 * using {@link DSL} builder
	 * 
	 * @param query The JOOQ Query
	 * @throws PuiDaoFindException If any error occurred while executing the
	 *                             statement
	 */
	void executeQuery(Query query) throws PuiDaoFindException;

	/**
	 * Execute a custom SQL statement over the entity. This Query is build on JOOQ
	 * using {@link DSL} builder
	 * 
	 * @param query The JOOQ Query
	 * @return The list of registries that accomplish the given statement
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> executeCustomQuery(Query query) throws PuiDaoFindException;

	/**
	 * Execute a custom SQL statement over the entity, with the capability of using
	 * parameters in the query. This Query is build on JOOQ using {@link DSL}
	 * builder
	 * 
	 * @param query      The JOOQ Query
	 * @param parameters The parameters of the query
	 * @return The list of registries that accomplish the given statement
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> executeCustomQueryWithParameters(Query query, List<Object> parameters) throws PuiDaoFindException;

	/**
	 * Returns the Class that represents the associated DTO Object
	 * 
	 * @return The DTO Class
	 */
	Class<T> getDtoClass();

	/**
	 * Returns the Dao Class. Use this method to obtain the real Dao class, useful
	 * if the class is a proxy
	 * 
	 * @return The real DAO Class
	 */
	Class<? extends IDao<T>> getDaoClass();

	/**
	 * Executes an operation in a paginated way in order to avoid memory problems.
	 * The SearchRequest parameter indicates how the search should be done (filters,
	 * order, etc...). Once perfromed each search, you can execute the desired
	 * operation for each element (consumer parameter) or for the whole paginated
	 * results (bulkConsumer parameter)
	 * 
	 * @param req          The request for pagginating
	 * @param consumer     The operation to be executed for each element
	 * @param bulkConsumer The operation to be executed for the whole paginated
	 *                     results
	 */
	void executePaginagedOperation(SearchRequest req, Consumer<T> consumer, Consumer<List<T>> bulkConsumer);

	/**
	 * Returns a list of records of the Table/View for the given configuration
	 * 
	 * @param req The configuration of the search
	 * @return The response with the data that fits the request
	 * @throws PuiDaoListException If an error is thrown while executing the
	 *                             statement
	 */
	SearchResponse<T> findPaginated(SearchRequest req) throws PuiDaoListException;

}
