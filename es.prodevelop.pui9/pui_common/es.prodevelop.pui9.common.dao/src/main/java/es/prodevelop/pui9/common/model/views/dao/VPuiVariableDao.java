package es.prodevelop.pui9.common.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiVariableDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiVariable;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiVariableDao extends AbstractViewDao<IVPuiVariable> implements IVPuiVariableDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiVariable> findByVariable(String variable) throws PuiDaoFindException {
		return super.findByColumn(IVPuiVariable.VARIABLE_FIELD, variable);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiVariable> findByValue(String value) throws PuiDaoFindException {
		return super.findByColumn(IVPuiVariable.VALUE_FIELD, value);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiVariable> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IVPuiVariable.DESCRIPTION_FIELD, description);
	}
}
