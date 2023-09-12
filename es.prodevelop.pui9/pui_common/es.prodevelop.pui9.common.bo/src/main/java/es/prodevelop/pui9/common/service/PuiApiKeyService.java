package es.prodevelop.pui9.common.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiApiKeyDao;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiProfileFunctionalityDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKey;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKeyPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileFunctionality;
import es.prodevelop.pui9.common.service.interfaces.IPuiApiKeyService;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;

@PuiGenerated
@Service
public class PuiApiKeyService extends AbstractService<IPuiApiKeyPk, IPuiApiKey, INullView, IPuiApiKeyDao, INullViewDao>
		implements IPuiApiKeyService {

	@Autowired
	private IPuiProfileFunctionalityDao profileFunctionalityDao;

	private Map<String, IPuiApiKey> cache;

	@PostConstruct
	private void postConstructVariableService() {
		cache = new LinkedHashMap<>();

		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ReloadApiKeys", true, 0, 5, TimeUnit.MINUTES,
				this::reloadApiKeys);
	}

	@Override
	public IPuiApiKey getApiKey(String apiKey) {
		IPuiApiKey puiApiKey;
		synchronized (cache) {
			puiApiKey = cache.get(apiKey);
		}
		return puiApiKey;
	}

	@Override
	public String getApiKeyByName(String name) {
		synchronized (cache) {
			for (IPuiApiKey apiKey : cache.values()) {
				if (apiKey.getName().equals(name)) {
					return apiKey.getApikey();
				}
			}
		}
		return null;
	}

	private void reloadApiKeys() {
		synchronized (cache) {
			List<IPuiApiKey> list;
			try {
				list = getTableDao().findAll();
			} catch (PuiDaoFindException e) {
				list = Collections.emptyList();
			}

			cache.clear();

			list.forEach(ak -> {
				fillFunctionalities(ak);
				cache.put(ak.getApikey(), ak);
			});
		}
	}

	private void fillFunctionalities(IPuiApiKey puiApiKey) {
		Set<String> functionalities;
		if (ObjectUtils.isEmpty(puiApiKey.getProfile())) {
			functionalities = Collections.emptySet();
		} else {
			try {
				functionalities = new LinkedHashSet<>(profileFunctionalityDao.findByProfile(puiApiKey.getProfile())
						.stream().map(IPuiProfileFunctionality::getFunctionality).collect(Collectors.toSet()));
			} catch (PuiDaoFindException e) {
				functionalities = Collections.emptySet();
			}
		}

		puiApiKey.setFunctionalities(functionalities);
	}

}