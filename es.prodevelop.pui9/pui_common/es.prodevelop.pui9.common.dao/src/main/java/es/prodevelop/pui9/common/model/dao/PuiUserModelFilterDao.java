package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserModelFilterDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilterPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@Repository
@PuiGenerated
public class PuiUserModelFilterDao extends AbstractTableDao<IPuiUserModelFilterPk, IPuiUserModelFilter>
		implements IPuiUserModelFilterDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiUserModelFilter> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserModelFilterPk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserModelFilter> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserModelFilter.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserModelFilter> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserModelFilter.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserModelFilter> findByLabel(String label) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserModelFilter.LABEL_FIELD, label);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiUserModelFilter> findByIsdefault(Integer isdefault) throws PuiDaoFindException {
		return super.findByColumn(IPuiUserModelFilter.ISDEFAULT_FIELD, isdefault);
	}
}
