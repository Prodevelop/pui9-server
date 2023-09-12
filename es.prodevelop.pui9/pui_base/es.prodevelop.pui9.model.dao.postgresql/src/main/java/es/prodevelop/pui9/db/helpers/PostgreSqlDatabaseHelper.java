package es.prodevelop.pui9.db.helpers;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectJoinStep;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import es.prodevelop.codegen.pui9.model.DatabaseType;

/**
 * Database Helper for PostgreSql. Concret implementation of Abstract Database
 * Helper
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PostgreSqlDatabaseHelper extends AbstractDatabaseHelper {

	private static final String DATE_TIME_FORMAT = DATE_FORMAT + " HH24:MI:SS";
	private static final String POSTGRESQL_DATE_TIME_FORMAT = "YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"";
	private static final String POSTGRESQL_DATE_TIME_MILLIS_FORMAT = "YYYY-MM-DD\"T\"HH24:MI:SS.MS\"Z\"";

	@Override
	public DatabaseType getDatabaseType() {
		return DatabaseType.POSTGRE_SQL;
	}

	@Override
	protected DSLContext initializeDSLContext() {
		return DSL.using(SQLDialect.POSTGRES,
				new Settings().withRenderFormatted(true).withRenderQuotedNames(RenderQuotedNames.NEVER));
	}

	@Override
	protected String getSqlCastToString() {
		return "CAST(" + COLUMNNAME + " AS VARCHAR)";
	}

	@Override
	protected String getSqlConvertDateIntoString(ZoneId zoneId) {
		return "TO_CHAR(" + COLUMNNAME + " at time zone '" + TIMEZONE + "', '" + adaptDateFormatToUser(DATE_TIME_FORMAT)
				+ "')";
	}

	@Override
	protected String getSqlConvertStringIntoDate(boolean hasMillis) {
		return "TO_TIMESTAMP('" + VALUE + "', '"
				+ (hasMillis ? POSTGRESQL_DATE_TIME_MILLIS_FORMAT : POSTGRESQL_DATE_TIME_FORMAT) + "')";
	}

	@Override
	protected String getSqlTextOperation(boolean caseSensitiveAndAccents, boolean isLargeStringField,
			boolean dataIsColumn) {
		if (dataIsColumn) {
			return COLUMNNAME + OP + "'" + BEGINNING + "' || " + VALUE + " || '" + END + "'";
		} else {
			if (caseSensitiveAndAccents) {
				return COLUMNNAME + OP + "'" + BEGINNING + "' || '" + VALUE + "' || '" + END + "'";
			} else {
				return "UNACCENT(LOWER(" + COLUMNNAME + "))" + OP + "'" + BEGINNING + "' || UNACCENT(LOWER('" + VALUE
						+ "')) || '" + END + "'";
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends SelectJoinStep<Record>> S getSqlForPagination(int page, int size, S select) {
		int from = page * size;

		return (S) select.limit(DSL.inline(size)).offset(DSL.inline(from));
	}

	@Override
	public Select<Record> getViewsSql(Collection<String> viewNames) {
		Select<Record> select = null;

		for (Iterator<String> it = viewNames.iterator(); it.hasNext();) {
			String next = it.next();
			List<Field<?>> fields = new ArrayList<>();
			fields.add(DSL.inline(next).as("name"));
			fields.add(
					DSL.function("pg_get_viewdef", Object.class, DSL.inline(next), DSL.inline(true)).as("definition"));

			Select<Record> sel = getDSLContext().select(fields);
			if (select != null) {
				select.union(sel);
			} else {
				select = sel;
			}
		}

		return select;
	}

	@Override
	protected String getBoundingBoxSql(String column, Integer srid, Double xmin, Double ymin, Double xmax,
			Double ymax) {
		StringBuilder sb = new StringBuilder();
		sb.append("ST_Intersects(");
		sb.append(column);
		sb.append(", ST_MakeEnvelope(" + xmin + "," + ymin + "," + xmax + "," + ymax + ",");
		sb.append(srid);
		sb.append(")) = true");

		return sb.toString();
	}

	@Override
	protected String getIntersectsByPoint(String column, Integer srid, Double x, Double y) {
		StringBuilder sb = new StringBuilder();
		sb.append("ST_Intersects(");
		sb.append(column);
		sb.append(",ST_SetSRID(ST_MakePoint(" + x + ", " + y + "),");
		sb.append(srid);
		sb.append(")) = true");

		return sb.toString();
	}

	@Override
	public String getSqlCastToJson() {
		return DSL.cast(DSL.field("?"), JSONB.class).toString();
	}

}
