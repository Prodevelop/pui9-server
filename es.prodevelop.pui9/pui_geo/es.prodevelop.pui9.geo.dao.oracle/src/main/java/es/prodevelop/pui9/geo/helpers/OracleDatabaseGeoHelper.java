package es.prodevelop.pui9.geo.helpers;

import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.geo.dao.helpers.AbstractDatabaseGeoHelper;

@Component
public class OracleDatabaseGeoHelper extends AbstractDatabaseGeoHelper {

	@Override
	public String getSqlCastToGeometry(String tableName) {
		return "sde.st_geometry(?, " + getSrid(tableName) + ")";
	}

	private Integer getSrid(String tableName) {
		if (dbSrid == null) {
			SelectConditionStep<Record1<Object>> select = dbHelper.getDSLContext().select(DSL.field("srid"))
					.from(DSL.table("user_sdo_geom_metadata"))
					.where(DSL.lower("table_name").eq(tableName.toLowerCase()));

			dbSrid = jdbcTemplate.queryForObject(select.getSQL(), Integer.class);
		}
		return dbSrid;
	}

}
