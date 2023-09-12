package es.prodevelop.pui9.filter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import es.prodevelop.pui9.filter.rules.BeginWithRule;
import es.prodevelop.pui9.filter.rules.BetweenRule;
import es.prodevelop.pui9.filter.rules.BoundingBoxRule;
import es.prodevelop.pui9.filter.rules.ContainsRule;
import es.prodevelop.pui9.filter.rules.EndsWithRule;
import es.prodevelop.pui9.filter.rules.EqualsRule;
import es.prodevelop.pui9.filter.rules.GreaterEqualsThanRule;
import es.prodevelop.pui9.filter.rules.GreaterThanRule;
import es.prodevelop.pui9.filter.rules.InRule;
import es.prodevelop.pui9.filter.rules.IntersectsByPointRule;
import es.prodevelop.pui9.filter.rules.IsNotNullRule;
import es.prodevelop.pui9.filter.rules.IsNullRule;
import es.prodevelop.pui9.filter.rules.LowerEqualsThanRule;
import es.prodevelop.pui9.filter.rules.LowerThanRule;
import es.prodevelop.pui9.filter.rules.NotBeginWithRule;
import es.prodevelop.pui9.filter.rules.NotBetweenRule;
import es.prodevelop.pui9.filter.rules.NotContainsRule;
import es.prodevelop.pui9.filter.rules.NotEndsWithRule;
import es.prodevelop.pui9.filter.rules.NotEqualsRule;
import es.prodevelop.pui9.filter.rules.NotInRule;

/**
 * Utility class to make easier creating Where clauses. Better use Columns
 * instead of Fields here
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class FilterBuilder {

	private FilterGroup filter;

	/**
	 * Create a new group with global 'and' operation
	 * 
	 * @return The Filter Builder
	 */
	public static FilterBuilder newAndFilter() {
		return new FilterBuilder(FilterGroup.andGroup());
	}

	/**
	 * Create a new group with global 'or' operation
	 * 
	 * @return The Filter Builder
	 */
	public static FilterBuilder newOrFilter() {
		return new FilterBuilder(FilterGroup.orGroup());
	}

	/**
	 * Create a new group with the given group
	 * 
	 * @param filters The group to be added
	 * @return The Filter Builder
	 */
	public static FilterBuilder newFilter(FilterGroup filters) {
		return new FilterBuilder(filters.copy());
	}

	/**
	 * Create a new group with the specified filter group
	 * 
	 * @param filters The filter group
	 */
	private FilterBuilder(FilterGroup filters) {
		this.filter = filters;
	}

	/**
	 * Add an "Equals" operation for an Object value: "column = ?". If value is a
	 * LocalDate, real operation is converted to a "between" operation from the
	 * start of the day to the end of the day
	 * 
	 * @param column The column name
	 * @param value  The Object value (String, Number, LocalDate, Instant...)
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEquals(String column, Object value) {
		EqualsRule.of(column, value);
		if (value instanceof LocalDate) {
			addBetween(column, value, value);
		} else {
			filter.addRule(EqualsRule.of(column, value));
		}
		return this;
	}

	/**
	 * Add an "Equals" operation for an String value: "column = ?". This operation
	 * is exact in terms that is case sensitive and accents are taken into account
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEqualsExact(String column, String value) {
		filter.addRule(EqualsRule.of(column, value).withCaseSensitiveAndAccents());
		return this;
	}

	/**
	 * Add an "Equals" operation comparing two columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEqualsToColumn(String columnLeft, String columnRight) {
		filter.addRule(EqualsRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Not Equals" operation for an Object value: "column <> ?". If value is
	 * a LocalDate, real operation is a "not between" operation from the start of
	 * the day to the end of the day
	 * 
	 * @param column The column name
	 * @param value  The Object value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEquals(String column, Object value) {
		if (value instanceof LocalDate) {
			addNotBetween(column, value, value);
		} else {
			filter.addRule(NotEqualsRule.of(column, value));
		}
		return this;
	}

	/**
	 * Add a "Not Equals" operation for an String value: "column <> ?". This
	 * operation is exact in terms that is case sensitive and accents are taken into
	 * account
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEqualsExact(String column, String value) {
		filter.addRule(NotEqualsRule.of(column, value).withCaseSensitiveAndAccents());
		return this;
	}

	/**
	 * Add a "Not Equals" operation comparing two columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEqualsToColumn(String columnLeft, String columnRight) {
		filter.addRule(NotEqualsRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Is Null" opeartion: "column IS NULL"
	 * 
	 * @param column The column name
	 * @return The current Filter Builder
	 */
	public FilterBuilder addIsNull(String column) {
		filter.addRule(IsNullRule.of(column));
		return this;
	}

	/**
	 * Add a "Is Not Null" operation: "column IS NOT NULL"
	 * 
	 * @param column The column name
	 * @return The current Filter Builder
	 */
	public FilterBuilder addIsNotNull(String column) {
		filter.addRule(IsNotNullRule.of(column));
		return this;
	}

	/**
	 * Add a "Begins With" operation: "column LIKE '?%'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addBeginWith(String column, String value) {
		filter.addRule(BeginWithRule.of(column, value));
		return this;
	}

	/**
	 * Add a "Begins With" operation: "column LIKE '?%'". This operation is exact in
	 * terms that is case sensitive and accents are taken into account
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addBeginWithExact(String column, String value) {
		filter.addRule(BeginWithRule.of(column, value).withCaseSensitiveAndAccents());
		return this;
	}

	/**
	 * Add a "Begins With" operation: "column LIKE '?%'", comparing two columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addBeginWithColumn(String columnLeft, String columnRight) {
		filter.addRule(BeginWithRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Not Begins With" operation: "column NOT LIKE '?%'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotBeginWith(String column, String value) {
		filter.addRule(NotBeginWithRule.of(column, value));
		return this;
	}

	/**
	 * Add a "Not Begins With" operation: "column NOT LIKE '?%'". This operation is
	 * exact in terms that is case sensitive and accents are taken into account
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotBeginWithExact(String column, String value) {
		filter.addRule(NotBeginWithRule.of(column, value).withCaseSensitiveAndAccents());
		return this;
	}

	/**
	 * Add a "Not Begins With" operation: "column NOT LIKE '?%'", comparing two
	 * columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotBeginWithColumn(String columnLeft, String columnRight) {
		filter.addRule(NotBeginWithRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Ends With" operation: "column LIKE '%?'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEndsWith(String column, String value) {
		filter.addRule(EndsWithRule.of(column, value));
		return this;
	}

	/**
	 * Add a "Ends With" operation: "column LIKE '%?'". This operation is exact in
	 * terms that is case sensitive and accents are taken into account
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEndsWithExact(String column, String value) {
		filter.addRule(EndsWithRule.of(column, value).withCaseSensitiveAndAccents());
		return this;
	}

	/**
	 * Add a "Ends With" operation: "column LIKE '%?'", comparing two columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEndsWithColumn(String columnLeft, String columnRight) {
		filter.addRule(EndsWithRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Not Ends With" operation: "column NOT LIKE '%?'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEndsWith(String column, String value) {
		filter.addRule(NotEndsWithRule.of(column, value));
		return this;
	}

	/**
	 * Add a "Not Ends With" operation: "column NOT LIKE '%?'". This operation is
	 * exact in terms that is case sensitive and accents are taken into account
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEndsWithExact(String column, String value) {
		filter.addRule(NotEndsWithRule.of(column, value).withCaseSensitiveAndAccents());
		return this;
	}

	/**
	 * Add a "Not Ends With" operation: "column NOT LIKE '%?'", comparing two
	 * columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEndsWithColumn(String columnLeft, String columnRight) {
		filter.addRule(NotEndsWithRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Contains" operation: "column LIKE '%?%'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addContains(String column, String value) {
		filter.addRule(ContainsRule.of(column, value));
		return this;
	}

	/**
	 * Add a "Contains" operation: "column LIKE '%?%'". This operation is exact in
	 * terms that is case sensitive and accents are taken into account
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addContainsExact(String column, String value) {
		filter.addRule(ContainsRule.of(column, value).withCaseSensitiveAndAccents());
		return this;
	}

	/**
	 * Add a "Contains" operation: "column LIKE '%?%'", comparing two columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addContainsColumn(String columnLeft, String columnRight) {
		filter.addRule(ContainsRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Not Contains" operation: "column NOT LIKE '%?%'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotContains(String column, String value) {
		filter.addRule(NotContainsRule.of(column, value));
		return this;
	}

	/**
	 * Add a "Not Contains" operation: "column NOT LIKE '%?%'. This operation is
	 * exact in terms that is case sensitive and accents are taken into account
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotContainsExact(String column, String value) {
		filter.addRule(NotContainsRule.of(column, value).withCaseSensitiveAndAccents());
		return this;
	}

	/**
	 * Add a "Not Contains" operation: "column NOT LIKE '%?%'", comparing two
	 * columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotContainsColumn(String columnLeft, String columnRight) {
		filter.addRule(NotContainsRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Lower Than" operation: "column < ?". If value is a LocalDate, the
	 * value is converted to an Instant to the start (00:00:00.000) of the given the
	 * day at the TimeZone of the system
	 * 
	 * @param column The column name
	 * @param value  The value. Number or TemporalObject
	 * @return The current Filter Builder
	 */
	public FilterBuilder addLowerThan(String column, Object value) {
		filter.addRule(LowerThanRule.of(column, value));
		return this;
	}

	/**
	 * Add a "Lower Than" operation comparing two columns
	 * 
	 * @param column
	 * @param value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addLowerThanColumn(String columnLeft, String columnRight) {
		filter.addRule(LowerThanRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Lower or Equals Than" operation: "column <= ?". If value is a
	 * LocalDate, the value is converted to an Instant to the end (23:59:59.999) of
	 * the given the day at the TimeZone of the system
	 * 
	 * @param column The column name
	 * @param value  The value. Number or TemporalObject
	 * @return The current Filter Builder
	 */
	public FilterBuilder addLowerEqualsThan(String column, Object value) {
		if (value instanceof LocalDate) {
			value = Instant.from(((LocalDate) value).atTime(LocalTime.MAX).with(ChronoField.MILLI_OF_SECOND, 999)
					.atZone(ZoneId.systemDefault()));
		}

		filter.addRule(LowerEqualsThanRule.of(column, value));
		return this;
	}

	/**
	 * Add a "Lower or Equals Than" operation comparing two columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addLowerEqualsThanColumn(String columnLeft, String columnRight) {
		filter.addRule(LowerEqualsThanRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Greater Than" operation value: "column > ?". If value is a LocalDate,
	 * the value is converted to an Instant to the end (23:59:59.999) of the given
	 * the day at the TimeZone of the system
	 * 
	 * @param column The column name
	 * @param value  The value. Number or TemporalObject
	 * @return The current Filter Builder
	 */
	public FilterBuilder addGreaterThan(String column, Object value) {
		filter.addRule(GreaterThanRule.of(column, value));
		return this;
	}

	/**
	 * Add a "Greater Than" operation comparing two columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addGreaterThanColumn(String columnLeft, String columnRight) {
		filter.addRule(GreaterThanRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Greater or Equals Than" operation: "column >= ?". If value is a
	 * LocalDate, the value is converted to an Instant to the start (00:00:00.000)
	 * of the given the day at the TimeZone of the system
	 * 
	 * @param column The column name
	 * @param value  The value. Number or TemporalObject
	 * @return The current Filter Builder
	 */
	public FilterBuilder addGreaterEqualsThan(String column, Object value) {
		filter.addRule(GreaterEqualsThanRule.of(column, value));
		return this;
	}

	/**
	 * Add a "Greater or Equals Than" operation comparing two columns
	 * 
	 * @param columnLeft
	 * @param columnRight
	 * @return The current Filter Builder
	 */
	public FilterBuilder addGreaterEqualsThanColumn(String columnLeft, String columnRight) {
		filter.addRule(GreaterEqualsThanRule.of(columnLeft, columnRight).withDataIsColumn());
		return this;
	}

	/**
	 * Add a "Between" operation for a Object value: "column between {lower} and
	 * {upper}". If values are LocalDate, the operation is converted to Instants
	 * from the start of the day (00:00:00.000) and the end of the day
	 * (23:59:59.999)
	 * 
	 * @param column The column name
	 * @param lower  The lower Object value. Allowed values are Number, LocalDate
	 *               and Instant
	 * @param upper  The upper Object value. Allowed values are Number, LocalDate
	 *               and Instant
	 * @return The current Filter Builder
	 */
	public FilterBuilder addBetween(String column, Object lower, Object upper) {
		BetweenRule br = BetweenRule.of(column, lower, upper);
		if (br != null) {
			filter.addRule(br);
		}
		return this;
	}

	/**
	 * Add a "Between" operation over two columns: "value not between {lowerColumn}
	 * and {upperColumn}". Allowed values are Number or Instant
	 * 
	 * @param value       The value. Number or Instant
	 * @param leftColumn  The lower column to compare against
	 * @param rightColumn The upper column to compare against
	 * @return The current Filter Builder
	 */
	public FilterBuilder addBetweenColumns(Object value, String leftColumn, String rightColumn) {
		filter.addRule(BetweenRule.of(value, leftColumn, rightColumn));
		return this;
	}

	/**
	 * Add a "Not Between" operation for an Object value: "column not between
	 * {lower} and {upper}". If values are LocalDate, the operation is converted to
	 * Instants from the start of the day (00:00:00.000) and the end of the day
	 * (23:59:59.999)
	 * 
	 * @param column The column name
	 * @param lower  The lower Object value. Allowed values are Number, LocalDate
	 *               and Instant
	 * @param upper  The upper Object value. Allowed values are Number, LocalDate
	 *               and Instant
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotBetween(String column, Object lower, Object upper) {
		NotBetweenRule nbr = NotBetweenRule.of(column, lower, upper);
		if (nbr != null) {
			filter.addRule(nbr);
		}
		return this;
	}

	/**
	 * Add a "Not Between" operation over two columns: "value not between
	 * {lowerColumn} and {upperColumn}". Allowed values are Number or Instant
	 * 
	 * @param value       The value. Number or Instant
	 * @param leftColumn  The lower column to compare against
	 * @param rightColumn The upper column to compare against
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotBetweenColumns(Object value, String leftColumn, String rightColumn) {
		filter.addRule(NotBetweenRule.of(value, leftColumn, rightColumn));
		return this;
	}

	/**
	 * Add a "In" operation for a Object values: "column in (val1, val2, val3)".
	 * Number or Strings are allowed
	 * 
	 * @param column     The column name
	 * @param collection The Object value collection. Number or Strings are allowed
	 * @return The current Filter Builder
	 */
	public FilterBuilder addIn(String column, Collection<?> collection) {
		InRule ir = InRule.of(column, collection);
		if (ir != null) {
			filter.addRule(ir);
		}
		return this;
	}

	/**
	 * Add a "In" operation for a Object values: "column in (val1, val2, val3)".
	 * Number or Strings are allowed
	 * 
	 * @param column The column name
	 * @param values The values as array. Number or Strings are allowed
	 * @return The current Filter Builder
	 */
	public FilterBuilder addIn(String column, Object... values) {
		InRule ir = InRule.of(column, Arrays.asList(values));
		if (ir != null) {
			filter.addRule(ir);
		}
		return this;
	}

	/**
	 * Add a "Not In" operation for a Object values: "column not in (val1, val2,
	 * val3)". Number or Strings are allowed
	 * 
	 * @param column     The column name
	 * @param collection The Object value collection. Number or Strings are allowed
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotIn(String column, Collection<?> collection) {
		NotInRule nir = NotInRule.of(column, collection);
		if (nir != null) {
			filter.addRule(nir);
		}
		return this;
	}

	/**
	 * Add a Bounding Box rule, where test if the the given column value falls in
	 * the given bounding box coordinates (specifying the SRID)
	 * 
	 * @param column      The column value
	 * @param srid        The srid (could be null)
	 * @param bottomLeftX The xmin value (bottom left X)
	 * @param bottomLeftY The ymin value (bottom left Y)
	 * @param topRightX   The xmax value (top right X)
	 * @param topRightY   The ymax value (top right Y)
	 * @return The current Filter Builder
	 */
	public FilterBuilder addBoundingBox(String column, Integer srid, Double bottomLeftX, Double bottomLeftY,
			Double topRightX, Double topRightY) {
		filter.addRule(BoundingBoxRule.of(column, srid, bottomLeftX, bottomLeftY, topRightX, topRightY));
		return this;
	}

	/**
	 * Add an Intersects by point rule, where test if the given point intersects the
	 * given column value line
	 * 
	 * @param column The column value
	 * @param srid   The srid
	 * @param x      The X coordinate
	 * @param y      The Y coordinate
	 * @return The current Filter Builder
	 */
	public FilterBuilder addIntersectsByPoint(String column, Integer srid, Double x, Double y) {
		filter.addRule(IntersectsByPointRule.of(column, srid, x, y));
		return this;
	}

	/**
	 * Add a group to the filter. The group is an exact copy of the original given
	 * 
	 * @param filterBuilder The group to be added
	 * @return The current Filter Builder
	 */
	public FilterBuilder addGroup(FilterBuilder filterBuilder) {
		if (filterBuilder != null) {
			filter.addGroup(filterBuilder.asFilterGroup().copy());
		}
		return this;
	}

	/**
	 * Return the builtin filter group object
	 * 
	 * @return The Filter group
	 */
	public FilterGroup asFilterGroup() {
		return filter;
	}

	/**
	 * Check if the builtin filter is empty or not
	 * 
	 * @return true if it's empty; false if not
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(filter.toString());
	}

	@Override
	public String toString() {
		return filter != null ? filter.toString() : "*empty_filters*";
	}

}