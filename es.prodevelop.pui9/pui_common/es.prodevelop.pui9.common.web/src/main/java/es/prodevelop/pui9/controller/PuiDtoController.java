package es.prodevelop.pui9.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller allows to list all the DTOs available in the application and
 * define all of them, giving information of all of its attributes
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@PuiNoSessionRequired
@Tag(name = "PUI DTO Information")
@RequestMapping("/dtos")
public class PuiDtoController extends AbstractPuiController {

	/**
	 * List all the available DTOs registered in the application
	 * 
	 * @return The list of IDs of the DTOs
	 */
	@Operation(summary = "List all DTO", description = "List all available DTO identifiers. Use one of them to decribe it")
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> list() {
		return DtoRegistry.getAllRegisteredDtos().stream().map(DtoRegistry::getEntityFromDto).filter(Objects::nonNull)
				.filter(item -> !ObjectUtils.isEmpty(item)).sorted().distinct().collect(Collectors.toList());
	}

	/**
	 * List all the available DTOs registered in the application
	 * 
	 * @return The list of IDs of the DTOs
	 */
	@Operation(summary = "List all DTO", description = "List all available DTO identifiers. Use one of them to decribe it", responses = @ApiResponse(ref = "class"))
	@GetMapping(value = "/full", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Class<? extends IDto>> full() {
		return DtoRegistry.getAllRegisteredDtos().stream()
				.sorted((d1, d2) -> d1.getClass().getSimpleName().compareTo(d2.getClass().getSimpleName()))
				.collect(Collectors.toList());
	}

	/**
	 * Define the given DTO
	 * 
	 * @param id The name of the DTO (the {@link #list()} method will retrieve all
	 *           the DTO names)
	 * @return The definition of the DTO
	 */
	@Operation(summary = "Define a DTO", description = "Get the full definition of a DTO with the given identifier, including the type of the attributes and some useful information.", responses = @ApiResponse(ref = "class"))
	@GetMapping(value = "/describe/{entity}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Class<? extends IDto> describe(
			@Parameter(description = "The identifier name of the DTO", required = true) @PathVariable String entity) {
		return DtoRegistry.getDtoFromEntity(entity);
	}
}
