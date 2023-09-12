package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiModelDao extends ITableDao<IPuiModelPk, IPuiModel> {
	@PuiGenerated
	java.util.List<IPuiModel> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiModel> findByEntity(String entity) throws PuiDaoFindException;

}
