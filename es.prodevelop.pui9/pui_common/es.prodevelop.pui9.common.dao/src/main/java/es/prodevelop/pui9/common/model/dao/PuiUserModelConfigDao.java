package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserModelConfigDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfig;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfigPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiUserModelConfigDao extends AbstractTableDao<IPuiUserModelConfigPk, IPuiUserModelConfig>
		implements IPuiUserModelConfigDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiUserModelConfig> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserModelConfigPk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserModelConfig> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserModelConfig.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserModelConfig> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserModelConfig.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserModelConfig> findByConfiguration(String configuration) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserModelConfig.CONFIGURATION_FIELD, configuration);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserModelConfig> findByType(String type) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserModelConfig.TYPE_FIELD, type);
	}
}
