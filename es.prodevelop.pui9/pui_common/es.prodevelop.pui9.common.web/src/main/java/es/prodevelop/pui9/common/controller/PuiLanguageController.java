package es.prodevelop.pui9.common.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiLanguageDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguage;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiLanguagePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiLanguageDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLanguage;
import es.prodevelop.pui9.common.service.interfaces.IPuiLanguageService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller adds support to manage the available languages of the
 * application. For a correct language support, each translatable entity in the
 * database should have, for each registry, so much translated registries as
 * available languages has the application
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiGenerated
@Controller
@Tag(name = "PUI Languages")
@RequestMapping("/puilanguage")
public class PuiLanguageController extends
		AbstractCommonController<IPuiLanguagePk, IPuiLanguage, IVPuiLanguage, IPuiLanguageDao, IVPuiLanguageDao, IPuiLanguageService> {

	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_LANGUAGE";
	}

	@PuiGenerated
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_LANGUAGE";
	}

	/**
	 * Retrieves all the available languages defined in the database of the
	 * application
	 * 
	 * @return A list of all the available languages of the application
	 * @throws PuiServiceGetException
	 */
	@PuiNoSessionRequired
	@Operation(summary = "Get all languages", description = "Get all available languages for the application")
	@GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IVPuiLanguage> getAll() throws PuiServiceGetException {
		return getService().getAllView();
	}

}