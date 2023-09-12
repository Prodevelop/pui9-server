package es.prodevelop.pui9.common.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiApiKey;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiVariableDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariablePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiVariableDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiVariable;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller allows to manage the variables of the application
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiGenerated
@Controller
@Tag(name = "PUI Variables")
@RequestMapping("/puivariable")
public class PuiVariableController extends
		AbstractCommonController<IPuiVariablePk, IPuiVariable, IVPuiVariable, IPuiVariableDao, IVPuiVariableDao, IPuiVariableService> {

	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_VARIABLE";
	}

	@PuiGenerated
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_VARIABLE";
	}

	@Override
	public boolean allowInsert() {
		return false;
	}

	@Override
	public boolean allowDelete() {
		return false;
	}

	@PuiApiKey
	@Operation(summary = "Get all the variables", description = "Get all the variables.")
	@GetMapping(value = "/getAllVariables", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IPuiVariable> getAllVariables() throws PuiServiceGetException {
		return getService().getAll();
	}

	@PuiApiKey
	@Operation(summary = "Get the value of this variable", description = "Get the value of the given variable as String value.")
	@GetMapping(value = "/getVariable/{variable}")
	public String getVariable(
			@Parameter(description = "The name of the variable to get", required = true) @PathVariable String variable) {
		return getService().getVariable(variable);
	}

	@PuiApiKey
	@Operation(summary = "Force a reload of all the variables", description = "Force a reload of all the variables")
	@GetMapping(value = "/reload")
	public void reload() {
		getService().reloadVariables();
	}

	@PuiApiKey
	@Operation(summary = "Get the Application Legal Text", description = "Get the Application Legal Text")
	@GetMapping(value = "/getApplicationLegalText")
	public String getApplicationLegalText() {
		return getService().getVariable(PuiVariableValues.APPLICATION_LEGAL_TEXT.name());
	}

	@PuiNoSessionRequired
	@Operation(summary = "Check if the environment if for development", description = "Check if the environment if for development")
	@GetMapping(value = "/isDevelopmentEnvironment")
	public Boolean isDevelopmentEnvironment() {
		return getService().getVariable(Boolean.class, PuiVariableValues.DEVELOPMENT_ENVIRONMENT.name());
	}

	@PuiNoSessionRequired
	@Operation(summary = "Check if LDAP is active or not", description = "Check if LDAP is active or not")
	@GetMapping(value = "/isLdapActive")
	public Boolean isLdapActive() {
		return getService().getVariable(Boolean.class, PuiVariableValues.LDAP_ACTIVE.name());
	}

}