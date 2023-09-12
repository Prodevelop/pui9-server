package es.prodevelop.pui9.db.helpers;

import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.SelectJoinStep;

import es.prodevelop.codegen.pui9.model.DatabaseType;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * This interface database helper is intended to be used in the database
 * searches of JDBC approach. It provides useful methods valid for all the
 * database vendors.
 * <p>
 * Specific vendor implementations may be used in the most cases
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IDatabaseHelper {

	/**
	 * Get the DatabaseType being used in the application
	 * 
	 * @return The {@link DatabaseType}
	 */
	DatabaseType getDatabaseType();

	/**
	 * Get the DSL context for JOOQ
	 * 
	 * @return The DSL context for JOOQ
	 */
	DSLContext getDSLContext();

	/**
	 * Generate the SQL for searching the given text in the given fields
	 * 
	 * @param dtoClass The DTO class of the entity
	 * @param fields   The searched fields
	 * @param text     The text to search
	 * @param zoneId   The ZoneId to use in date operations
	 * @return The SQL
	 */
	String processSearchText(Class<? extends IDto> dtoClass, List<String> fields, String text, ZoneId zoneId);

	/**
	 * Generate the SQL for searching the given texts for each provided field
	 * 
	 * @param dtoClass     The DTO class of the entity
	 * @param fieldTextMap The map with the texts to search on each field
	 * @param zoneId       The ZoneId to use in date operations
	 * @return The SQL
	 */
	String processSearchText(Class<? extends IDto> dtoClass, Map<String, String> fieldTextMap, ZoneId zoneId);

	/**
	 * Generate the String SQL from the given Filters DTO
	 * 
	 * @param dtoClass The DTO class of the search
	 * @param filters  The Filters
	 * @param addAlias Add the alias to the tables
	 * @return The SQL of the given filters
	 */
	String processFilters(Class<? extends IDto> dtoClass, FilterGroup filters, boolean addAlias);

	/**
	 * Returns the real SQL that will be executed for pagination
	 * 
	 * @param page   The page to retrieve (starting with 0)
	 * @param size   The number of registries to retrieve
	 * @param select The real query built on JOOQ
	 * @return The real JOOQ select for pagination
	 */
	<S extends SelectJoinStep<Record>> S getSqlForPagination(int page, int size, S select);

	/**
	 * Get the SQL that retrieves the list of Views of the Database
	 * 
	 * @return The SQL
	 */
	Select<Record> getViewsSql(Collection<String> viewNames);

	/**
	 * Get the SQL to convert a value to JSON
	 * 
	 * @return
	 */
	String getSqlCastToJson();

}
