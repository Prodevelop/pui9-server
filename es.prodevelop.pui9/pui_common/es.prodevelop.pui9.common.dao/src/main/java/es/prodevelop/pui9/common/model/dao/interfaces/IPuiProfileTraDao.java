package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileTra;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileTraPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiProfileTraDao extends ITableDao<IPuiProfileTraPk, IPuiProfileTra> {
	@PuiGenerated
	java.util.List<IPuiProfileTra> findByProfile(String profile) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiProfileTra> findByLang(String lang) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiProfileTra> findByLangstatus(Integer langstatus) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiProfileTra> findByName(String name) throws PuiDaoFindException;
}
