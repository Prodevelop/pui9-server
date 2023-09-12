package es.prodevelop.pui9.common.model.views.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiModel;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;

@PuiGenerated
public interface IVPuiModelDao extends IViewDao<IVPuiModel> {
	@PuiGenerated
	java.util.List<IVPuiModel> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiModel> findByEntity(String entity) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiModel> findByFilter(String filter) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IVPuiModel> findByConfiguration(String configuration) throws PuiDaoFindException;
}