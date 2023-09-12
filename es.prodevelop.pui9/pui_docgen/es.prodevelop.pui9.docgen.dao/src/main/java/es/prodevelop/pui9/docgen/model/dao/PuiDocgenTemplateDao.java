package es.prodevelop.pui9.docgen.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.dao.interfaces.IPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiDocgenTemplateDao extends AbstractTableDao<IPuiDocgenTemplatePk, IPuiDocgenTemplate>
		implements IPuiDocgenTemplateDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenTemplate> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplatePk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenTemplate> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenTemplate> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.DESCRIPTION_FIELD, description);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenTemplate> findByMainmodel(String mainmodel) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.MAIN_MODEL_FIELD, mainmodel);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenTemplate> findByFilename(String filename) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.FILENAME_FIELD, filename);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiDocgenTemplate> findByColumnfilename(String columnfilename) throws PuiDaoFindException {
		return super.findByColumn(IPuiDocgenTemplate.COLUMN_FILENAME_FIELD, columnfilename);
	}
}
