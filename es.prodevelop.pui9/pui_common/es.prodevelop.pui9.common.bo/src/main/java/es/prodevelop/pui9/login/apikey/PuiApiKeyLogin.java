package es.prodevelop.pui9.login.apikey;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.model.dto.PuiFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKey;
import es.prodevelop.pui9.common.service.interfaces.IPuiApiKeyService;
import es.prodevelop.pui9.login.IPuiApiKeyLogin;
import es.prodevelop.pui9.mvc.configuration.IPuiRequestMappingHandlerMapping;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoApiKeyException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNotAllowedException;
import es.prodevelop.pui9.utils.PuiConstants;

@Component
public class PuiApiKeyLogin implements IPuiApiKeyLogin {

	@Autowired
	private IPuiRequestMappingHandlerMapping puiRequestMappingHandlerMapping;

	@Autowired
	private IPuiApiKeyService apiKeyService;

	@Override
	public boolean isApiKeyRequest(HttpServletRequest request, Object handler) {
		String apiKey = getApiKey(request);
		return puiRequestMappingHandlerMapping.isWebServiceApiKeySupported(handler) && !ObjectUtils.isEmpty(apiKey);
	}

	@Override
	public void validateApiKey(HttpServletRequest request, Object handler)
			throws PuiServiceNoApiKeyException, PuiServiceNotAllowedException {
		String apiKey = getApiKey(request);
		if (ObjectUtils.isEmpty(apiKey)) {
			throw new PuiServiceNoApiKeyException();
		}

		IPuiApiKey puiApiKey = apiKeyService.getApiKey(apiKey);
		if (puiApiKey == null) {
			throw new PuiServiceNoApiKeyException();
		}

		checkApiKeyPermission(handler, puiApiKey);
	}

	/**
	 * Check if the apiKeuy has permission to execute the given Web Service. Any Web
	 * Service that requires permission, should declare the {@link PuiFunctionality}
	 * annotation. This annotation is used to extract the name of the funcionality
	 * that the apiKey should have to consume it
	 * 
	 * @param handler         The handler that represents the Web Service
	 * @param functionalities The list of functionalities of logged apikey
	 * @throws PuiCommonNotAllowedException If the apiKey has no permission to
	 *                                      execute it
	 */
	private void checkApiKeyPermission(Object handler, IPuiApiKey puiApiKey) throws PuiServiceNotAllowedException {
		String functionality = puiRequestMappingHandlerMapping.getWebServiceApiKeyFunctionality(handler);
		boolean hasFunctionality = false;

		if (ObjectUtils.isEmpty(functionality)) {
			hasFunctionality = true;
		} else {
			hasFunctionality = puiApiKey.getFunctionalities().stream().filter(func -> func.equals(functionality))
					.count() > 0;
		}

		if (!hasFunctionality) {
			throw new PuiServiceNotAllowedException();
		}
	}

	private String getApiKey(HttpServletRequest request) {
		return request.getHeader(PuiConstants.HEADER_API_KEY);
	}

}
