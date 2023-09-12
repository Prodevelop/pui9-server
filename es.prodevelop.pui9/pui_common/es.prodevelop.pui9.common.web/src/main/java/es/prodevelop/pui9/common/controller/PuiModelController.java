package es.prodevelop.pui9.common.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiApiKey;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.configuration.PuiModelConfiguration;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiModelDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiModel;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller allows to retrieve the information about the models of the
 * application (views structure)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiGenerated
@Controller
@Tag(name = "PUI Models")
@RequestMapping("/puimodel")
public class PuiModelController extends
		AbstractCommonController<IPuiModelPk, IPuiModel, IVPuiModel, IPuiModelDao, IVPuiModelDao, IPuiModelService> {

	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_MODEL";
	}

	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_MODEL";
	}

	/**
	 * Reload the cache of models from the database
	 */
	@PuiApiKey
	@Operation(summary = "Force a reload of all the models", description = "Force a reload of all the models")
	@GetMapping(value = "/reload")
	public void reload() {
		getService().reloadModels(true);
	}

	/**
	 * Get all the model configuration customized for the logged user, with all of
	 * the grid filters and configurations
	 * 
	 * @return A Map with all of the filters and configurations of the models for
	 *         logged user
	 */
	@Operation(summary = "All the model configuration", description = "Get the configuration of all the models")
	@GetMapping(value = "/getModelConfigurations", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, PuiModelConfiguration> getModelConfigurations() {
		return getService().getPuiModelConfigurations();
	}

	/**
	 * Get all the models available on the application
	 * 
	 * @return A List with all of the models
	 */
	@PuiApiKey
	@Operation(summary = "All the models", description = "Get all the models")
	@GetMapping(value = "/getAllModels", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getAllModels() {
		return getService().getAllModels();
	}

}