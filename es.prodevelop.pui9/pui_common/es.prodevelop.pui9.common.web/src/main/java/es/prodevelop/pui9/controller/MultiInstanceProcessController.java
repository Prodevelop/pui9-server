package es.prodevelop.pui9.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.threads.PuiMultiInstanceProcessBackgroundExecutors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Tag(name = "PUI Multi Instance Process")
@RequestMapping("/multiinstanceprocess")
public class MultiInstanceProcessController extends AbstractPuiController {

	@Autowired
	private PuiMultiInstanceProcessBackgroundExecutors multiInstanceProcessBackExec;

	@PuiNoSessionRequired
	@Operation(summary = "Get the UUID of this application", description = "Get the UUID of this application")
	@GetMapping(value = "/getUuid")
	public String getUuid() {
		return multiInstanceProcessBackExec.getUuid();
	}

	@PuiNoSessionRequired
	@Operation(summary = "Get all the processes ID that belongs to this application", description = "Get all the processes ID that belongs to this application")
	@GetMapping(value = "/getAllProcessId")
	public Set<String> getAllProcessId() {
		return multiInstanceProcessBackExec.getAllProcessId();
	}

}