package es.prodevelop.pui9.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiFunctionalityDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityPk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiFunctionalityDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiFunctionality;
import es.prodevelop.pui9.common.service.interfaces.IPuiFunctionalityService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller adds support to manage the functionalities of the application
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiGenerated
@Controller
@Tag(name = "PUI Functionalities")
@RequestMapping("/puifunctionality")
public class PuiFunctionalityController extends
		AbstractCommonController<IPuiFunctionalityPk, IPuiFunctionality, IVPuiFunctionality, IPuiFunctionalityDao, IVPuiFunctionalityDao, IPuiFunctionalityService> {

	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_FUNCTIONALITY";
	}

	@Override
	public boolean allowDelete() {
		return false;
	}

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
}