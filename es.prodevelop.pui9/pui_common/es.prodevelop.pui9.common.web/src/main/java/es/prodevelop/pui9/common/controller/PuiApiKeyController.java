package es.prodevelop.pui9.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiApiKey;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiApiKeyDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKey;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiApiKeyPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiApiKeyService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiGenerated
@Controller
@Tag(name = "PUI Api Key")
@RequestMapping("/puiapikey")
public class PuiApiKeyController extends
		AbstractCommonController<IPuiApiKeyPk, IPuiApiKey, INullView, IPuiApiKeyDao, INullViewDao, IPuiApiKeyService> {

	@Override
	public boolean allowGet() {
		return false;
	}

	@Override
	public boolean allowInsert() {
		return false;
	}

	@Override
	public boolean allowUpdate() {
		return false;
	}

	@Override
	public boolean allowDelete() {
		return false;
	}

	@Override
	public boolean allowList() {
		return false;
	}

	@PuiApiKey
	@Operation(summary = "Get Api Key definition", description = "Get Api Key definition")
	@GetMapping(value = "/getApiKey/{name}")
	public String getApiKey(
			@Parameter(description = "The unique name of the ApiKey to get", required = true) @PathVariable String name) {
		return getService().getApiKeyByName(name);
	}

}