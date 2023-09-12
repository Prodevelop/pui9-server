package es.prodevelop.pui9.eventlistener.listener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.eventlistener.event.VariableUpdatedEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.login.ldap.PuiLdapSpringSecurityContextSource;

/**
 * Listener fired when the variables related to LDAP are updated
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class UpdateLdapVariablesListener extends PuiListener<VariableUpdatedEvent> {

	@Autowired
	private PuiLdapSpringSecurityContextSource ldapContextSource;

	private List<String> variables;

	@PostConstruct
	private void postConstruct() {
		variables = new ArrayList<>();
		variables.add(PuiVariableValues.LDAP_URL.name());
		variables.add(PuiVariableValues.LDAP_DOMAIN.name());
		variables.add(PuiVariableValues.LDAP_USER.name());
		variables.add(PuiVariableValues.LDAP_PASSWORD.name());
	}

	@Override
	protected boolean passFilter(VariableUpdatedEvent event) {
		return variables.contains(event.getSource().getVariable())
				&& !event.getOldValue().equals(event.getSource().getValue());
	}

	@Override
	protected void process(VariableUpdatedEvent event) throws PuiException {
		ldapContextSource.afterPropertiesSet();
	}

}
