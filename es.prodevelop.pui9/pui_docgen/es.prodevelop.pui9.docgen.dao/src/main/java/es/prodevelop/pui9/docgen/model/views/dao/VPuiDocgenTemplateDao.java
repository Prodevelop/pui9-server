package es.prodevelop.pui9.docgen.model.views.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.docgen.model.views.dao.interfaces.IVPuiDocgenTemplateDao;
import es.prodevelop.pui9.docgen.model.views.dto.interfaces.IVPuiDocgenTemplate;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractViewDao;

@PuiGenerated
@Repository
public class VPuiDocgenTemplateDao extends AbstractViewDao<IVPuiDocgenTemplate> implements IVPuiDocgenTemplateDao {
	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocgenTemplate> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenTemplate.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocgenTemplate> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenTemplate.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocgenTemplate> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenTemplate.DESCRIPTION_FIELD, description);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocgenTemplate> findByMainmodel(String mainmodel) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenTemplate.MAIN_MODEL_FIELD, mainmodel);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocgenTemplate> findByModels(String models) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenTemplate.MODELS_FIELD, models);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocgenTemplate> findByFilename(String filename) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenTemplate.FILENAME_FIELD, filename);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocgenTemplate> findByColumnfilename(String columnfilename) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenTemplate.COLUMN_FILENAME_FIELD, columnfilename);
	}

	@PuiGenerated
	@Override
	public java.util.List<IVPuiDocgenTemplate> findByLabel(String label) throws PuiDaoFindException {
		return super.findByColumn(IVPuiDocgenTemplate.LABEL_FIELD, label);
	}
}
