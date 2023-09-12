package es.prodevelop.pui9.dashboards.service;

import org.springframework.stereotype.Service;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.dao.interfaces.IPuiDashboardDao;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboard;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboardPk;
import es.prodevelop.pui9.dashboards.model.views.dao.interfaces.IVPuiDashboardDao;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiDashboard;
import es.prodevelop.pui9.dashboards.service.interfaces.IPuiDashboardService;
import es.prodevelop.pui9.service.AbstractService;

@PuiGenerated
@Service
public class PuiDashboardService
		extends AbstractService<IPuiDashboardPk, IPuiDashboard, IVPuiDashboard, IPuiDashboardDao, IVPuiDashboardDao>
		implements IPuiDashboardService {
}