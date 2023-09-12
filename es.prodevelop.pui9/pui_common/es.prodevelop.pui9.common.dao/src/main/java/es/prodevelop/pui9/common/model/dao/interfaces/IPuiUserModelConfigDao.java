package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfig;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfigPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiUserModelConfigDao extends ITableDao<IPuiUserModelConfigPk, IPuiUserModelConfig> {
	@PuiGenerated
	java.util.List<IPuiUserModelConfig> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUserModelConfig> findByUsr(String usr) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUserModelConfig> findByModel(String model) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUserModelConfig> findByConfiguration(String configuration) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiUserModelConfig> findByType(String type) throws PuiDaoFindException;
}
