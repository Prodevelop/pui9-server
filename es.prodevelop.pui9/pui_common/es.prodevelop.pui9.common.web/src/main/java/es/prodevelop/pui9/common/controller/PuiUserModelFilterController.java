package es.prodevelop.pui9.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserModelFilterDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilterPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserModelFilterService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller is used to manage the model filters of the users
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiGenerated
@Controller
@Tag(name = "PUI User Model Filters")
@RequestMapping("/puiusermodelfilter")
public class PuiUserModelFilterController extends
		AbstractCommonController<IPuiUserModelFilterPk, IPuiUserModelFilter, INullView, IPuiUserModelFilterDao, INullViewDao, IPuiUserModelFilterService> {

	@Override
	public boolean allowGet() {
		return false;
	}

}