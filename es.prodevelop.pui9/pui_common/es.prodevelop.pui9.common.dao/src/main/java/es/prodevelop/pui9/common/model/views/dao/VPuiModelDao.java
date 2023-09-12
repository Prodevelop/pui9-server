package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiModelDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiModel;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiModelDao extends AbstractViewDao<IVPuiModel> implements IVPuiModelDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiModel> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IVPuiModel.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiModel> findByEntity(String entity) throws PuiDaoFindException {
		return super.findByColumn(IVPuiModel.ENTITY_FIELD, entity);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiModel> findByFilter(String filter) throws PuiDaoFindException {
		return super.findByColumn(IVPuiModel.FILTER_FIELD, filter);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiModel> findByConfiguration(String configuration) throws PuiDaoFindException {
		return super.findByColumn(IVPuiModel.CONFIGURATION_FIELD, configuration);
	}
}