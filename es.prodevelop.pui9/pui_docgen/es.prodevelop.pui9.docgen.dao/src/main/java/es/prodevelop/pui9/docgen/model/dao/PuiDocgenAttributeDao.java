package es.prodevelop.pui9.docgen.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenAttributeDao;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttribute;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttributePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiDocgenAttributeDao extends AbstractTableDao<IPuiDocgenAttributePk, IPuiDocgenAttribute>
		implements IPuiDocgenAttributeDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenAttribute> findById(String id) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenAttributePk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenAttribute> findByLabel(String label) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenAttribute.LABEL_FIELD, label);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenAttribute> findByValue(String value) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenAttribute.VALUE_FIELD, value);
	}
}
