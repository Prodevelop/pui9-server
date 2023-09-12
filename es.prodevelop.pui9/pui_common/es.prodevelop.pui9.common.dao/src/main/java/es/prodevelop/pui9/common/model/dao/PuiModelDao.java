package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiModelDao extends AbstractTableDao<IPuiModelPk, IPuiModel> implements IPuiModelDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiModel> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiModelPk.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiModel> findByEntity(String entity) throws PuiDaoFindException {
		return super.findByColumn(IPuiModel.ENTITY_FIELD, entity);
	}

}
