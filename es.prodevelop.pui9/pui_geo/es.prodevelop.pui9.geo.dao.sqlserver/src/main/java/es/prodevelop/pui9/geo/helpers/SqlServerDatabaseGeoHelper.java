package es.prodevelop.pui9.geo.helpers;

import java.util.List;
import java.util.Map;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.geo.dao.helpers.AbstractDatabaseGeoHelper;

@Component
public class SqlServerDatabaseGeoHelper extends AbstractDatabaseGeoHelper {

	@Override
	public String getSqlCastToGeometry(String tableName) {
		return "STGeomFromText(?, " + getSrid() + ")";
	}

	private Integer getSrid() {
		if (dbSrid == null) {
			SelectConditionStep<Record2<Object, Object>> select = dbHelper.getDSLContext()
					.select(DSL.field(DSL.unquotedName("t", "name")).as("entity"),
							DSL.field(DSL.unquotedName("c", "name")).as("columnname"))
					.from(DSL.table(DSL.unquotedName("sys", "columns")).as("c"))
					.join(DSL.table(DSL.unquotedName("sys", "tables")).as("t"))
					.on(DSL.field(DSL.unquotedName("t", "object_id")).eq(DSL.field(DSL.unquotedName("c", "object_id"))))
					.where(DSL.function(DSL.unquotedName("type_name"), Object.class, DSL.field("user_type_id"))
							.eq(DSL.inline("geometry")));

			List<Map<String, Object>> res = jdbcTemplate.queryForList(select.getSQL());
			for (Map<String, Object> map : res) {
				String entity = (String) map.get("entity");
				String columnname = (String) map.get("columnname");

				Select<Record1<Object>> select2 = dbHelper.getDSLContext()
						.select(DSL.field(DSL.name(DSL.unquotedName("top 1 " + columnname), DSL.unquotedName("STSrid")))
								.as(SRID_COLUMN))
						.from(entity);

				List<Map<String, Object>> res2 = jdbcTemplate.queryForList(select2.getSQL());
				for (Map<String, Object> map2 : res2) {
					if (map2.containsKey(SRID_COLUMN)) {
						dbSrid = (Integer) map.get(SRID_COLUMN);
						if (dbSrid != null && dbSrid > 0) {
							break;
						}
					}
				}
			}
		}
		return dbSrid;
	}

}
