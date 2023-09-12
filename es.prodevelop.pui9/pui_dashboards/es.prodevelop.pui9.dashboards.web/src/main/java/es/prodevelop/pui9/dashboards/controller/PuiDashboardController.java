package es.prodevelop.pui9.dashboards.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.dashboards.model.dao.interfaces.IPuiDashboardDao;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboard;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboardPk;
import es.prodevelop.pui9.dashboards.model.views.dao.interfaces.IVPuiDashboardDao;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiDashboard;
import es.prodevelop.pui9.dashboards.service.interfaces.IPuiDashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;

@PuiGenerated
@Controller
@Tag(name = "PUI Dashboards")
@RequestMapping("/puidashboard")
public class PuiDashboardController extends
		AbstractCommonController<IPuiDashboardPk, IPuiDashboard, IVPuiDashboard, IPuiDashboardDao, IVPuiDashboardDao, IPuiDashboardService> {
	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_DASHBOARD";
	}

	@PuiGenerated
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_DASHBOARD";
	}
}