package es.prodevelop.pui9.geo.helpers;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.geo.dao.helpers.AbstractDatabaseGeoHelper;

@Component
public class PostgreSqlDatabaseGeoHelper extends AbstractDatabaseGeoHelper {

	@Override
	public String getSqlCastToGeometry(String tableName) {
		return "CAST(? AS geometry)";
	}

}
