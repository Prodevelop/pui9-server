package es.prodevelop.pui9.alerts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.alerts.model.dao.interfaces.IPuiAlertConfigurationDao;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfiguration;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfigurationPk;
import es.prodevelop.pui9.alerts.model.views.dao.interfaces.IVPuiAlertConfigurationDao;
import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlertConfiguration;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertConfigurationService;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.controller.AbstractCommonController;
import io.swagger.v3.oas.annotations.tags.Tag;

@PuiGenerated
@Controller
@Tag(name = "PUI Alert Configuration")
@RequestMapping("/puialertconfiguration")
public class PuiAlertConfigurationController extends
		AbstractCommonController<IPuiAlertConfigurationPk, IPuiAlertConfiguration, IVPuiAlertConfiguration, IPuiAlertConfigurationDao, IVPuiAlertConfigurationDao, IPuiAlertConfigurationService> {
	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_ALERT";
	}

	@PuiGenerated
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_ALERT";
	}
}