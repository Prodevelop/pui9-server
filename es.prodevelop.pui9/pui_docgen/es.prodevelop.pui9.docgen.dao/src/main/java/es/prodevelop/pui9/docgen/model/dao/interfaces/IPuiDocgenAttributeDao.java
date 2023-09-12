package es.prodevelop.pui9.docgen.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttribute;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenAttributePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiDocgenAttributeDao extends ITableDao<IPuiDocgenAttributePk, IPuiDocgenAttribute> {
	@PuiGenerated
	java.util.List<IPuiDocgenAttribute> findById(String id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocgenAttribute> findByLabel(String label) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocgenAttribute> findByValue(String value) throws PuiDaoFindException;
}
