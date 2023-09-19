package es.prodevelop.pui9.issueticket;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.dto.issue.IssueTicket;
import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.mail.PuiMailAttachment;
import es.prodevelop.pui9.mail.PuiMailConfiguration;
import es.prodevelop.pui9.mail.PuiMailSender;

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
			throw new PuiServiceException(new PuiException("No service ticket set in pui_variables"));
		}
		to = "helpdesk@prodevelop.es";

		StringBuilder sb = new StringBuilder();
		sb.append("<p><b>Descripción</b></p>");
		sb.append("<p>" + issueTicket.getContent() + "</p>");
		sb.append("<p><b>Urgencia</b></p>");
		sb.append("<p>" + issueTicket.getUrgency() + "</p>");
		sb.append("<p><b>Nombre</b></p>");
		sb.append("<p>" + issueTicket.getName() + "</p>");
		sb.append("<p><b>Email</b></p>");
		sb.append("<p>" + issueTicket.getEmail() + "</p>");
		if (!ObjectUtils.isEmpty(issueTicket.getPhone())) {
			sb.append("<p><b>Teléfono</b></p>");
			sb.append("<p>" + issueTicket.getPhone() + "</p>");
		}

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
