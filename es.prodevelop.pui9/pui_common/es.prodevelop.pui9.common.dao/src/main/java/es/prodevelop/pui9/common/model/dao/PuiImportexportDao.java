package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiImportexportDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@PuiGenerated
@Repository
public class PuiImportexportDao extends AbstractTableDao<IPuiImportexportPk, IPuiImportexport>
		implements IPuiImportexportDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiImportexport> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiImportexportPk.ID_FIELD, id);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiImportexport> findByModel(String model) throws PuiDaoFindException {
		return super.findByColumn(IPuiImportexport.MODEL_FIELD, model);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiImportexport> findByUsr(String usr) throws PuiDaoFindException {
		return super.findByColumn(IPuiImportexport.USR_FIELD, usr);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiImportexport> findByDatetime(java.time.Instant datetime) throws PuiDaoFindException {
		return super.findByColumn(IPuiImportexport.DATETIME_FIELD, datetime);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiImportexport> findByFilenamecsv(String filenamecsv) throws PuiDaoFindException {
		return super.findByColumn(IPuiImportexport.FILENAME_CSV_FIELD, filenamecsv);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiImportexport> findByFilenamejson(String filenamejson) throws PuiDaoFindException {
		return super.findByColumn(IPuiImportexport.FILENAME_JSON_FIELD, filenamejson);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiImportexport> findByExecuted(Integer executed) throws PuiDaoFindException {
		return super.findByColumn(IPuiImportexport.EXECUTED_FIELD, executed);
	}
}
