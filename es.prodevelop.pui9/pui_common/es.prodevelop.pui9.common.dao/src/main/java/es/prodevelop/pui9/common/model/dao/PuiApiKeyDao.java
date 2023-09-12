package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiApiKeyDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKey;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKeyPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

@Repository
@PuiGenerated
public class PuiApiKeyDao extends AbstractTableDao<IPuiApiKeyPk, IPuiApiKey> implements IPuiApiKeyDao {
	@PuiGenerated
	@Override
	public java.util.List<IPuiApiKey> findByApikey(String apikey) throws PuiDaoFindException {
		return super.findByColumn(IPuiApiKeyPk.API_KEY_FIELD, apikey);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiApiKey> findByName(String name) throws PuiDaoFindException {
		return super.findByColumn(IPuiApiKey.NAME_FIELD, name);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiApiKey> findByDescription(String description) throws PuiDaoFindException {
		return super.findByColumn(IPuiApiKey.DESCRIPTION_FIELD, description);
	}

	@PuiGenerated
	@Override
	public java.util.List<IPuiApiKey> findByProfile(String profile) throws PuiDaoFindException {
		return super.findByColumn(IPuiApiKey.PROFILE_FIELD, profile);
	}
}
