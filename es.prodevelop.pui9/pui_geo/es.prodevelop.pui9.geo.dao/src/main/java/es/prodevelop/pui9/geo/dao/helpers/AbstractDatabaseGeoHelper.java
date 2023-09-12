package es.prodevelop.pui9.geo.dao.helpers;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import es.prodevelop.pui9.db.helpers.IDatabaseHelper;

public abstract class AbstractDatabaseGeoHelper implements IDatabaseGeoHelper {

	protected static final String SRID_COLUMN = "srid";

	@Autowired
	protected IDatabaseHelper dbHelper;

	protected JdbcTemplate jdbcTemplate;
	protected Integer dbSrid;

	@Autowired
	private void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
