package es.prodevelop.pui9.geo.dao;

import org.springframework.beans.factory.annotation.Autowired;

import es.prodevelop.pui9.geo.dao.helpers.IDatabaseGeoHelper;
import es.prodevelop.pui9.geo.utils.DtoGeoUtils;
import es.prodevelop.pui9.model.dao.AbstractTableDao;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

public abstract class AbstractGeoTableDao<TPK extends ITableDto, T extends TPK> extends AbstractTableDao<TPK, T>
		implements ITableDao<TPK, T> {

	@Autowired
	private IDatabaseGeoHelper sqlAdapter;

	@Override
	protected String getParameterForSql(String columnName) {
		if (DtoRegistry.getGeomColumns(getDtoClass()).contains(columnName)) {
			return sqlAdapter.getSqlCastToGeometry(getEntityName());
		} else {
			return super.getParameterForSql(columnName);
		}
	}

	@Override
	protected void customizeDto(T dto) {
		DtoGeoUtils.fillGeometryValue(dto);
	}

}
