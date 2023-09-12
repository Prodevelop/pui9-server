package es.prodevelop.pui9.alerts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.alerts.model.dao.interfaces.IPuiAlertDao;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertPk;
import es.prodevelop.pui9.alerts.model.views.dao.interfaces.IVPuiAlertDao;
import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlert;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertService;
import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.controller.AbstractCommonController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@PuiGenerated
@Controller
@Tag(name = "PUI Alerts")
@RequestMapping("/puialert")
public class PuiAlertController extends
		AbstractCommonController<IPuiAlertPk, IPuiAlert, IVPuiAlert, IPuiAlertDao, IVPuiAlertDao, IPuiAlertService> {
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
	public boolean allowPatch() {
		return false;
	}

	@Override
	public boolean allowDelete() {
		return false;
	}

	@Operation
	@PuiFunctionality(id = "markAsRead", value = "getWriteFunctionality")
	@GetMapping(value = "/markAsRead")
	public void markAsRead(@Parameter(required = true) IPuiAlertPk pk) {
		getService().markAsRead(pk);
	}

}