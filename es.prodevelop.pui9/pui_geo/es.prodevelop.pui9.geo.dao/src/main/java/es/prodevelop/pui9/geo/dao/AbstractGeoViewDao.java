package es.prodevelop.pui9.geo.dao;

import es.prodevelop.pui9.geo.utils.DtoGeoUtils;
import es.prodevelop.pui9.model.dao.AbstractViewDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

public abstract class AbstractGeoViewDao<T extends IViewDto> extends AbstractViewDao<T> implements IViewDao<T> {

	@Override
	protected void customizeDto(T dto) {
		DtoGeoUtils.fillGeometryValue(dto);
	}

}
