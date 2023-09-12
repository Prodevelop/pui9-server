package es.prodevelop.pui9.docgen.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiDocgenTemplateDao extends ITableDao<IPuiDocgenTemplatePk, IPuiDocgenTemplate> {
	@PuiGenerated
	java.util.List<IPuiDocgenTemplate> findById(Integer id) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocgenTemplate> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocgenTemplate> findByDescription(String description) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocgenTemplate> findByMainmodel(String mainmodel) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocgenTemplate> findByFilename(String filename) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiDocgenTemplate> findByColumnfilename(String columnfilename) throws PuiDaoFindException;
}
