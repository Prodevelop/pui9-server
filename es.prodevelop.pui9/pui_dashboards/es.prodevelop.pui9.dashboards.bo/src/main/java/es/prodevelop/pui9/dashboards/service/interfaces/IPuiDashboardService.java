package es.prodevelop.pui9.dashboards.service.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.dashboards.model.dao.interfaces.IPuiDashboardDao;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboard;
import es.prodevelop.pui9.dashboards.model.dto.interfaces.IPuiDashboardPk;
import es.prodevelop.pui9.dashboards.model.views.dao.interfaces.IVPuiDashboardDao;
import es.prodevelop.pui9.dashboards.model.views.dto.interfaces.IVPuiDashboard;
import es.prodevelop.pui9.service.interfaces.IService;

@PuiGenerated
public interface IPuiDashboardService
		extends IService<IPuiDashboardPk, IPuiDashboard, IVPuiDashboard, IPuiDashboardDao, IVPuiDashboardDao> {
}