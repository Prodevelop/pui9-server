package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKey;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKeyPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

@PuiGenerated
public interface IPuiApiKeyDao extends ITableDao<IPuiApiKeyPk, IPuiApiKey> {
	@PuiGenerated
	java.util.List<IPuiApiKey> findByApikey(String apikey) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiApiKey> findByName(String name) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiApiKey> findByDescription(String description) throws PuiDaoFindException;

	@PuiGenerated
	java.util.List<IPuiApiKey> findByProfile(String profile) throws PuiDaoFindException;
}
