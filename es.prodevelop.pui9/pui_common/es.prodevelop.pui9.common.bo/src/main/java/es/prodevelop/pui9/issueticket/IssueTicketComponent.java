package es.prodevelop.pui9.issueticket;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.dto.issue.IssueTicket;
import es.prodevelop.pui9.common.dto.issue.IssueUrgencyEnum;
import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.messages.PuiCommonResourceBundle;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.mail.PuiMailAttachment;
import es.prodevelop.pui9.mail.PuiMailConfiguration;
import es.prodevelop.pui9.mail.PuiMailSender;
import es.prodevelop.pui9.messages.PuiMessagesRegistry;
import es.prodevelop.pui9.utils.PuiLanguage;

@Component
public class IssueTicketComponent {

	@Autowired
	private IPuiVariableService variableService;

	public void sendIssueTicket(IssueTicket issueTicket) throws PuiServiceException {
		if (!variableService.getVariable(Boolean.class, PuiVariableValues.ISSUE_TICKET_SERVICE_ENABLED.name())
				.booleanValue()) {
			return;
		}

		String to = variableService.getVariable(PuiVariableValues.ISSUE_TICKET_SERVICE_EMAIL.name());
		if (to == null) {
			throw new PuiServiceException(new PuiException("No service ticket email set in pui_variables"));
		}

		PuiLanguage lang = PuiUserSession.getSessionLanguage();
		String fieldNames = PuiMessagesRegistry.getSingleton().getString(lang,
				PuiCommonResourceBundle.issueTicketEmailFields);
		List<String> fieldNamesList = Arrays.asList(fieldNames.split(","));
		String urgencyValues = PuiMessagesRegistry.getSingleton().getString(lang,
				PuiCommonResourceBundle.issueTicketUrgency);
		Map<IssueUrgencyEnum, String> urgencyValuesMap = GsonSingleton.getSingleton().getGson().fromJson(urgencyValues,
				new ParameterizedTypeReference<Map<IssueUrgencyEnum, String>>() {
				}.getType());
		String urgencyLiteral = urgencyValuesMap.get(issueTicket.getUrgency());

		StringBuilder sb = new StringBuilder();
		sb.append("<p><b>" + fieldNamesList.get(0) + "</b></p>");
		sb.append("<p>" + issueTicket.getContent() + "</p>");
		sb.append("<p><b>" + fieldNamesList.get(1) + "</b></p>");
		sb.append("<p>" + urgencyLiteral + "</p>");
		sb.append("<p><b>" + fieldNamesList.get(2) + "</b></p>");
		sb.append("<p>" + issueTicket.getName() + "</p>");
		sb.append("<p><b>" + fieldNamesList.get(3) + "</b></p>");
		sb.append("<p>" + issueTicket.getEmail() + "</p>");
		sb.append("<p><b>" + fieldNamesList.get(4) + "</b></p>");
		sb.append("<p>" + issueTicket.getPhone() + "</p>");

		PuiMailConfiguration config = PuiMailConfiguration.builder().withSubject(issueTicket.getSubject())
				.withContent(sb.toString()).withIsHtml(true).withTo(to)
				.withAttachment(issueTicket.getFiles().stream().map(f -> {
					try {
						return PuiMailAttachment.getFileFromInputStream(f.getInputStream(), f.getOriginalFileName());
					} catch (IOException e) {
						return null;
					}
				}).filter(Objects::nonNull).collect(Collectors.toList()));
		PuiMailSender.getSingleton().send(config);
	}

}
