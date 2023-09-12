package es.prodevelop.pui9.eventlistener.listener;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.eventlistener.event.VariableUpdatedEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.mail.PuiMailSender;

/**
 * Listener fired when a variable that affects to the configuration of the Email
 * is updated
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class UpdateMailVariablesListener extends PuiListener<VariableUpdatedEvent> {

	@Override
	protected boolean passFilter(VariableUpdatedEvent event) {
		if (event.getOldValue().equals(event.getSource().getValue())) {
			return false;
		}

		PuiVariableValues pvv = PuiVariableValues.getByName(event.getSource().getVariable());
		if (pvv == null) {
			return false;
		}

		switch (pvv) {
		case MAIL_FROM:
		case MAIL_SMTP_HOST:
		case MAIL_SMTP_PORT:
		case MAIL_SMTP_USER:
		case MAIL_SMTP_PASS:
		case MAIL_SMTP_AUTH:
		case MAIL_SMTP_STARTTLS_ENABLE:
			return true;
		default:
			return false;
		}
	}

	@Override
	protected void process(VariableUpdatedEvent event) throws PuiException {
		PuiMailSender.getSingleton().configureSender();
	}

}
