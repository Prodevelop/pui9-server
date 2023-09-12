package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityTra;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityTraPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiFunctionalityTraDao extends ITableDao<IPuiFunctionalityTraPk, IPuiFunctionalityTra> {
	@PuiGenerated
	java.util.List<IPuiFunctionalityTra> findByFunctionality(String functionality) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiFunctionalityTra> findByLang(String lang) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiFunctionalityTra> findByLangstatus(Integer langstatus) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiFunctionalityTra> findByName(String name) throws PuiDaoFindException;
}
