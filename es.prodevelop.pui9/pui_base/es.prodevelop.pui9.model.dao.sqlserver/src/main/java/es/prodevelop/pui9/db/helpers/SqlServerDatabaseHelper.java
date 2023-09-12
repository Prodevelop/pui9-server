package es.prodevelop.pui9.db.helpers;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectJoinStep;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import es.prodevelop.codegen.pui9.model.DatabaseType;
import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * Database Helper for SqlServer. Concret implementation of Abstract Database
 * Helper
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class SqlServerDatabaseHelper extends AbstractDatabaseHelper {

	private static final String DATE_TIME_FORMAT = DATE_FORMAT + " HH:mm:ss";

	@Override
	public DatabaseType getDatabaseType() {
		return DatabaseType.SQL_SERVER;
	}

	@Override
	protected DSLContext initializeDSLContext() {
		return DSL.using(SQLDialect.DERBY,
				new Settings().withRenderFormatted(true).withRenderQuotedNames(RenderQuotedNames.NEVER));
	}

	@Override
	protected String getSqlCastToString() {
		return "CAST(" + COLUMNNAME + " AS VARCHAR)";
	}

	@Override
	protected String getSqlConvertDateIntoString(ZoneId zoneId) {
		if (zoneId.equals(PuiDateUtil.europeMadridZone)) {
			String atTimeZone = COLUMNNAME + " AT TIME ZONE 'Romance Standard Time'";
			String offset = "DATEPART(tz, " + atTimeZone + ")";
			String dateAdd = "DATEADD(minute, " + offset + ", " + COLUMNNAME + ")";
			return "FORMAT(" + dateAdd + ", '" + adaptDateFormatToUser(DATE_TIME_FORMAT) + "')";
		} else {
			return "FORMAT(" + COLUMNNAME + ", '" + adaptDateFormatToUser(DATE_TIME_FORMAT) + "')";
		}
	}

	@Override
	protected String getSqlConvertStringIntoDate(boolean hasMillis) {
		return "CONVERT(datetime, '" + VALUE + "')";
	}

	@Override
	protected String getSqlTextOperation(boolean caseSensitiveAndAccents, boolean isLargeStringField,
			boolean dataIsColumn) {
		if (dataIsColumn) {
			return COLUMNNAME + OP + "'" + BEGINNING + "' + " + VALUE + " + '" + END + "'";
		} else {
			if (caseSensitiveAndAccents) {
				return COLUMNNAME + OP + "'" + BEGINNING + "' + '" + VALUE + "' + '" + END + "'";
			} else {
				return "LOWER(" + COLUMNNAME + ") collate SQL_Latin1_General_Cp1251_CS_AS" + OP + "'" + BEGINNING
						+ "' + LOWER('" + VALUE + "') + '" + END + "'";
			}
		}
	}

	@Override
	public <S extends SelectJoinStep<Record>> S getSqlForPagination(int page, int size, S select) {
		int from = page * size;

		select.getQuery().attach(getDSLContext().configuration());

		if (!select.getSQL().toLowerCase().contains("order by")) {
			select.orderBy(DSL.inline(1));
		}
		select.offset(DSL.inline(from));
		select.limit(DSL.inline(size));

		return select;
	}

	@Override
	public Select<Record> getViewsSql(Collection<String> viewNames) {
		List<Field<?>> fields = new ArrayList<>();
		fields.add(DSL.field("name"));
		fields.add(DSL.field(DSL.function("object_definition", Object.class, DSL.field("object_id")).as("definition")));

		List<Condition> conditions = new ArrayList<>();
		conditions.add(DSL.field("type").eq(DSL.inline("V")));
		conditions.add(DSL.function(DSL.unquotedName("lower"), Object.class, DSL.field("name"))
				.in(viewNames.stream().map(DSL::inline).collect(Collectors.toList())));

		return getDSLContext().select(fields).from(DSL.unquotedName("sys", "objects")).where(conditions);
	}

	@Override
	protected String getBoundingBoxSql(String column, Integer srid, Double xmin, Double ymin, Double xmax,
			Double ymax) {
		StringBuilder sb = new StringBuilder();
		sb.append(column);
		sb.append(".STIntersects(");
		sb.append("geometry::STGeomFromText('LINESTRING(" + xmin + " " + ymin + "," + xmax + " " + ymax + ")', " + srid
				+ ")");
		sb.append(".STEnvelope()");
		sb.append(") = 1");

		return sb.toString();
	}

	@Override
	protected String getIntersectsByPoint(String column, Integer srid, Double x, Double y) {
		StringBuilder sb = new StringBuilder();
		sb.append(column);
		sb.append(".STIntersects(");
		sb.append("geometry::STGeomFromText('POINT(" + x + ", " + y + ")'," + srid + ")");
		sb.append(") = 1");

		return sb.toString();
	}

	@Override
	public String getSqlCastToJson() {
		return "?";
	}

}
