package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguage;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguagePk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiLanguageDao extends ITableDao<IPuiLanguagePk, IPuiLanguage> {
	@PuiGenerated
	java.util.List<IPuiLanguage> findByIsocode(String isocode) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiLanguage> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiLanguage> findByIsdefault(Integer isdefault) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiLanguage> findByEnabled(Integer enabled) throws PuiDaoFindException;
}
