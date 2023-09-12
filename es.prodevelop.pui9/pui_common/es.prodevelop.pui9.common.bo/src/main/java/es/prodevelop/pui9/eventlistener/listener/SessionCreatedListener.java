package es.prodevelop.pui9.eventlistener.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.model.dto.PuiUserPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.eventlistener.event.SessionCreatedEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.login.PuiUserSession;

/**
 * Listener for Login into the application: success or error
 */
@Component
public class SessionCreatedListener extends PuiListener<SessionCreatedEvent> {

	@Autowired
	private IPuiUserService userService;

	@Override
	protected void process(SessionCreatedEvent event) throws PuiException {
		PuiUserSession pus = event.getSource();
		try {
			userService.setLastAccess(new PuiUserPk(pus.getUsr()), pus.getCreation(), pus.getIp());
		} catch (PuiCommonUserNotExistsException e) {
			// do nothing
		}
	}

}
