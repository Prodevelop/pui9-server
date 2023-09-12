package es.prodevelop.pui9.db.helpers;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectJoinStep;
import org.jooq.Table;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import es.prodevelop.codegen.pui9.model.DatabaseType;
import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * Database Helper for Oracle. Concret implementation of Abstract Database
 * Helper
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class OracleDatabaseHelper extends AbstractDatabaseHelper {

	private static final String DATE_TIME_FORMAT = DATE_FORMAT + " HH24:MI:SS";
	private static final String ORACLE_DATE_TIME_FORMAT = "YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"";
	private static final String ORACLE_DATE_TIME_MILLIS_FORMAT = "YYYY-MM-DD\"T\"HH24:MI:SS.FF3\"Z\"";
	private static final String ACCENTS = "àáâãäåçèéêëìíîïñòóôõöøšùúûüýÿž";
	private static final String NO_ACCENTS = "aaaaaaceeeeiiiinoooooosuuuuyyz";
	private static final Integer GEOMETRY_TYPE_2D_POINT_CODE = 2001;
	private static final Integer GEOMETRY_TYPE_2D_POLYGON_CODE = 2003;
	private static final String GEOMETRY_SDO_ELEM_INFO_UNIC_POLYGON_WITHOUT_HOLES = "1,1003,3";

	@Override
	public DatabaseType getDatabaseType() {
		return DatabaseType.ORACLE;
	}

	@Override
	protected DSLContext initializeDSLContext() {
		return DSL.using(SQLDialect.DEFAULT,
				new Settings().withRenderFormatted(true).withRenderQuotedNames(RenderQuotedNames.NEVER));
	}

	@Override
	protected String getSqlCastToString() {
		return "TO_CHAR(" + COLUMNNAME + ")";
	}

	@Override
	protected String getSqlConvertDateIntoString(ZoneId zoneId) {
		if (TimeZone.getDefault().equals(TimeZone.getTimeZone(PuiDateUtil.utcZone))) {
			// it is supposed that datetime columns are of type 'timezone'
			return "TO_CHAR(" + COLUMNNAME + " at time zone '" + TIMEZONE + "', '"
					+ adaptDateFormatToUser(DATE_TIME_FORMAT) + "')";
		} else {
			// it is supposed that datetime columns are of type 'date' (never should occur).
			// Please, always avoid this case
			return "TO_CHAR(CAST(" + COLUMNNAME + " AS timestamp) at time zone '" + TIMEZONE + "', '"
					+ adaptDateFormatToUser(DATE_TIME_FORMAT) + "')";
		}
	}

	@Override
	protected String getSqlConvertStringIntoDate(boolean hasMillis) {
		return "TO_TIMESTAMP('" + VALUE + "', '"
				+ (hasMillis ? ORACLE_DATE_TIME_MILLIS_FORMAT : ORACLE_DATE_TIME_FORMAT) + "')";
	}

	@Override
	protected String getSqlTextOperation(boolean caseSensitiveAndAccents, boolean isLargeStringField,
			boolean dataIsColumn) {
		if (dataIsColumn) {
			return COLUMNNAME + OP + "'" + BEGINNING + "' || " + VALUE + " || '" + END + "'";
		} else {
			if (caseSensitiveAndAccents) {
				if (!isLargeStringField) {
					return COLUMNNAME + OP + "'" + BEGINNING + "' || '" + VALUE + "' || '" + END + "'";
				} else {
					return "dbms_lob.compare(" + COLUMNNAME + ", '" + VALUE + "') " + OP + " 0";
				}
			} else {
				if (!isLargeStringField) {
					return "TRANSLATE(LOWER(" + COLUMNNAME + "), '" + ACCENTS + "', '" + NO_ACCENTS + "')" + OP + "'"
							+ BEGINNING + "' || TRANSLATE(LOWER('" + VALUE + "'),'" + ACCENTS + "','" + NO_ACCENTS
							+ "') || '" + END + "'";
				} else {
					return "LOWER(" + COLUMNNAME + ")" + OP + "'" + BEGINNING + "' || TRANSLATE(LOWER('" + VALUE
							+ "'),'" + ACCENTS + "','" + NO_ACCENTS + "') || '" + END + "'";
				}
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends SelectJoinStep<Record>> S getSqlForPagination(int page, int size, S select) {
		int from = page * size + 1;
		int to = from + size - 1;

		Table<Record> realSelect = select.asTable("a");

		Select<Record> innerSelect = getDSLContext().select(DSL.field("rownum").as("rnum"), DSL.table("a").asterisk())
				.from(realSelect).where(DSL.field("rownum").lessOrEqual(DSL.inline(to)));

		return (S) getDSLContext().select(DSL.asterisk()).from(innerSelect)
				.where(DSL.field("rnum").greaterOrEqual(DSL.inline(from)));
	}

	@Override
	public Select<Record> getViewsSql(Collection<String> viewNames) {
		List<Field<?>> fields = new ArrayList<>();
		fields.add(DSL.field("view_name").as("name"));
		fields.add(DSL.field("text").as("definition"));

		List<Condition> conditions = new ArrayList<>();
		conditions.add(DSL.function(DSL.unquotedName("lower"), Object.class, DSL.field("view_name"))
				.in(viewNames.stream().map(DSL::inline).collect(Collectors.toList())));

		return getDSLContext().select(fields).from(DSL.unquotedName("SYS", "USER_VIEWS")).where(conditions);
	}

	@Override
	protected String getBoundingBoxSql(String column, Integer srid, Double xmin, Double ymin, Double xmax,
			Double ymax) {
		StringBuilder sb = new StringBuilder();
		sb.append("lower(SDO_ANYINTERACT(");
		sb.append(column);
		sb.append(", MDSYS.SDO_GEOMETRY(");
		sb.append(GEOMETRY_TYPE_2D_POLYGON_CODE);
		sb.append(",");
		sb.append(srid);
		sb.append(", NULL, MDSYS.SDO_ELEM_INFO_ARRAY(");
		sb.append(GEOMETRY_SDO_ELEM_INFO_UNIC_POLYGON_WITHOUT_HOLES);
		sb.append("), MDSYS.SDO_ORDINATE_ARRAY(" + xmin + "," + ymin + "," + xmax + "," + ymax + ")))) = 'true'");

		return sb.toString();
	}

	@Override
	protected String getIntersectsByPoint(String column, Integer srid, Double x, Double y) {
		StringBuilder sb = new StringBuilder();
		sb.append("lower(SDO_ANYINTERACT(");
		sb.append(column);
		sb.append(", MDSYS.SDO_GEOMETRY(");
		sb.append(GEOMETRY_TYPE_2D_POINT_CODE);
		sb.append(",");
		sb.append(srid);
		sb.append(", MDSYS.SDO_POINT_TYPE(" + x + ", " + y + ", NULL), NULL, NULL))) = 'true'");

		return sb.toString();
	}
	
	@Override
	public String getSqlCastToJson() {
		return "?";
	}

}
