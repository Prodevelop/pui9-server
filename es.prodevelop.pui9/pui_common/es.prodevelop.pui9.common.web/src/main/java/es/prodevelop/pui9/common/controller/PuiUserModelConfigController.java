package es.prodevelop.pui9.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserModelConfigDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfig;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfigPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserModelConfigService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * A controller to manage the configuration of the models. Basically this
 * configuration is about the grid
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiGenerated
@Controller
@Tag(name = "PUI User Model Configurations")
@RequestMapping("/puiusermodelconfig")
public class PuiUserModelConfigController extends
		AbstractCommonController<IPuiUserModelConfigPk, IPuiUserModelConfig, INullView, IPuiUserModelConfigDao, INullViewDao, IPuiUserModelConfigService> {

	@Override
	public boolean allowGet() {
		return false;
	}

}