package es.prodevelop.pui9.eventlistener.listener;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.eventlistener.event.LoginEvent;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.login.LoginEventData;

/**
 * Listener for Login into the application: success or error
 */
@Component
public class LoginListener extends AbstractAuditListener<LoginEvent> {

	@Override
	protected String getType() {
		return "";
	}

	@Override
	protected boolean fillAudit(LoginEvent event, IPuiAudit puiAudit) {
		LoginEventData led = event.getSource();
		puiAudit.setModel("login");
		puiAudit.setPk(led.getUsr());
		puiAudit.setUsr(led.getUsr());
		puiAudit.setIp(led.getIp());
		puiAudit.setClient(led.getClient());

		if (led.getPuiUserSession() != null) {
			puiAudit.setType("login_success");
			String json = GsonSingleton.getSingleton().getGson().toJson(led.getPuiUserSession());
			puiAudit.setContent(json);
		} else {
			puiAudit.setType("login_error");
			puiAudit.setContent(led.getError());
		}

		return true;
	}

}
