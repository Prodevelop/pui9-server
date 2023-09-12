package es.prodevelop.pui9.alerts.service;

import java.util.Collections;

import org.springframework.stereotype.Service;

import es.prodevelop.pui9.alerts.model.dao.interfaces.IPuiAlertDao;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertPk;
import es.prodevelop.pui9.alerts.model.views.dao.interfaces.IVPuiAlertDao;
import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlert;
import es.prodevelop.pui9.alerts.service.interfaces.IPuiAlertService;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.service.AbstractService;

@PuiGenerated
@Service
public class PuiAlertService extends AbstractService<IPuiAlertPk, IPuiAlert, IVPuiAlert, IPuiAlertDao, IVPuiAlertDao>
		implements IPuiAlertService {

	@Override
	public void markAsRead(IPuiAlertPk pk) {
		try {
			patch(pk, Collections.singletonMap(IPuiAlert.READ_FIELD, true));
		} catch (PuiServiceUpdateException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
