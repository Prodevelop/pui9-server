package es.prodevelop.pui9.common.service.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiApiKeyDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKey;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKeyPk;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.service.interfaces.IService;

@PuiGenerated
public interface IPuiApiKeyService extends IService<IPuiApiKeyPk, IPuiApiKey, INullView, IPuiApiKeyDao, INullViewDao> {

	IPuiApiKey getApiKey(String apiKey);

	String getApiKeyByName(String name);

}