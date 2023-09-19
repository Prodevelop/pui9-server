package es.prodevelop.pui9.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.common.dto.issue.IssueTicket;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.issueticket.IssueTicketComponent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller allows to send an email to the CAU service to create a new
 * issue ticket in the project
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Tag(name = "PUI Issue Ticket")
@RequestMapping("/issueTicket")
public class IssueTicketController extends AbstractPuiController {

	@Autowired
	private IssueTicketComponent issueTicket;

	/**
	 * Send a new issue ticket to the CAU
	 */
	@PuiNoSessionRequired
	@Operation(summary = "Send a new issue ticket", description = "Create a new issue ticket and send it to the CAU")
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void createIssueTicket(
			@Parameter(description = "The issue ticket information") @RequestParam MultipartFile[] files,
			IssueTicket ticket) throws PuiServiceException {
		issueTicket.sendIssueTicket(ticket);
	}

}
