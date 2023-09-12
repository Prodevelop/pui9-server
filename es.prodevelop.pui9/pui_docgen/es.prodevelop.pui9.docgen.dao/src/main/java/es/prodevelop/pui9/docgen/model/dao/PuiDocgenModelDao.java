package es.prodevelop.pui9.docgen.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenModelDao;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenModel;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenModelPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiDocgenModelDao extends AbstractTableDao<IPuiDocgenModelPk, IPuiDocgenModel>
		implements IPuiDocgenModelDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenModel> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenModelPk.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenModel> findByLabel(String label) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenModel.LABEL_FIELD, label);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenModel> findByIdentityfields(String identityfields) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenModel.IDENTITY_FIELDS_FIELD, identityfields);
	}
}
