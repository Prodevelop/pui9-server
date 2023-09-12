package es.prodevelop.pui9.alerts.service.interfaces;

import es.prodevelop.pui9.alerts.model.dao.interfaces.IPuiAlertDao;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertPk;
import es.prodevelop.pui9.alerts.model.views.dao.interfaces.IVPuiAlertDao;
import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlert;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.service.interfaces.IService;

@PuiGenerated
public interface IPuiAlertService extends IService<IPuiAlertPk, IPuiAlert, IVPuiAlert, IPuiAlertDao, IVPuiAlertDao> {

	void markAsRead(IPuiAlertPk pk);

}
