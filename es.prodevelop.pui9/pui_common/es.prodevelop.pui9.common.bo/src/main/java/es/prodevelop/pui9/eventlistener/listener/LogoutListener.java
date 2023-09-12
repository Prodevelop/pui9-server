package es.prodevelop.pui9.eventlistener.listener;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.eventlistener.event.LogoutEvent;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.login.LoginEventData;

/**
 * Listener for Login into the application: success or error
 */
@Component
public class LogoutListener extends AbstractAuditListener<LogoutEvent> {

	@Override
	protected String getType() {
		return "";
	}

	@Override
	protected boolean fillAudit(LogoutEvent event, IPuiAudit puiAudit) {
		LoginEventData led = event.getSource();
		puiAudit.setModel("logout");
		puiAudit.setPk(led.getUsr());
		puiAudit.setUsr(led.getUsr());
		puiAudit.setIp(led.getIp());
		puiAudit.setClient(led.getClient());

		puiAudit.setType("logout_success");
		String json = GsonSingleton.getSingleton().getGson().toJson(led.getPuiUserSession());
		puiAudit.setContent(json);

		return true;
	}

}
