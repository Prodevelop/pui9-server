package es.prodevelop.pui9.common.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.pui9.annotations.PuiApiKey;
import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.common.exceptions.PuiCommonInvalidPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonSamePasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserResetTokenException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserDao;
import es.prodevelop.pui9.common.model.dto.PuiUserPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiUserDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectUserPasswordException;
import es.prodevelop.pui9.services.exceptions.PuiServiceNotAllowedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * A controller to managet the users of the application
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiGenerated
@Controller
@Tag(name = "PUI Users")
@RequestMapping("/puiuser")
public class PuiUserController
		extends AbstractCommonController<IPuiUserPk, IPuiUser, IVPuiUser, IPuiUserDao, IVPuiUserDao, IPuiUserService> {

	private static final String CHANGE_USER_PASSWORDS = "CHANGE_USER_PASSWORDS";
	private static final String UPDATE_CURRENT_USER = "UPDATE_CURRENT_USER";

	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_USER";
	}

	@PuiGenerated
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_USER";
	}

	@Operation
	@PuiNoSessionRequired
	@GetMapping(value = "/requestResetPassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public void requestResetPassword(@RequestParam String usrEmail) {
		getService().requestResetPassword(usrEmail);
	}

	@Operation
	@PuiNoSessionRequired
	@GetMapping(value = "/doResetPassword")
	public void doResetPassword(@RequestParam String resetToken, @RequestParam String newPassword)
			throws PuiCommonUserResetTokenException, PuiServiceUpdateException, PuiCommonInvalidPasswordException,
			PuiCommonSamePasswordException {
		getService().doResetPassword(resetToken, newPassword);
	}

	@Operation
	@PuiFunctionality(id = "setPassword", value = CHANGE_USER_PASSWORDS)
	@GetMapping(value = "/setPassword")
	public void setPassword(@Parameter(required = true) IPuiUserPk pk,
			@Parameter(required = true) @RequestParam String newPassword)
			throws PuiServiceUpdateException, PuiCommonInvalidPasswordException,
			PuiServiceIncorrectUserPasswordException, PuiCommonUserNotExistsException, PuiCommonSamePasswordException {
		getService().changeUserPassword(pk, null, newPassword, true);
	}

	@Operation
	@GetMapping(value = "/changeUserPassword")
	public void changeUserPassword(@Parameter(required = true) @RequestParam String oldPassword,
			@Parameter(required = true) @RequestParam String newPassword)
			throws PuiServiceIncorrectUserPasswordException, PuiCommonUserNotExistsException, PuiServiceUpdateException,
			PuiCommonInvalidPasswordException, PuiCommonSamePasswordException {
		getService().changeUserPassword(new PuiUserPk(PuiUserSession.getCurrentSession().getUsr()), oldPassword,
				newPassword, false);
	}

	@PuiApiKey
	@Operation
	@PuiFunctionality(id = ID_FUNCTIONALITY_UPDATE, value = METHOD_FUNCTIONALITY_UPDATE)
	@GetMapping(value = "/disableUser")
	public void disableUser(@Parameter(required = true) String usr) throws PuiCommonUserNotExistsException {
		getService().disableUser(usr);
	}

	@PuiApiKey
	@Operation
	@PuiFunctionality(id = ID_FUNCTIONALITY_UPDATE, value = METHOD_FUNCTIONALITY_UPDATE)
	@GetMapping(value = "/enableUser")
	public void enableUser(@Parameter(required = true) String usr) throws PuiCommonUserNotExistsException {
		getService().enableUser(usr);
	}

	@Operation
	@PuiFunctionality(id = "patchUser", value = UPDATE_CURRENT_USER)
	@PatchMapping(value = "/patchUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void patchUser(
			@Parameter(description = "The PK of the element", required = true, in = ParameterIn.QUERY) IPuiUserPk dtoPk,
			@Parameter(required = true) @RequestBody Map<String, Object> properties)
			throws PuiServiceNotAllowedException, PuiServiceGetException, PuiServiceUpdateException {
		if (!PuiUserSession.getCurrentSession().getUsr().equals(dtoPk.getUsr())) {
			throw new PuiServiceNotAllowedException();
		}
		IPuiUser oldDto = getService().getByPk(dtoPk, PuiUserSession.getSessionLanguage());

		getService().patch(dtoPk, properties);
		fireEventPatch(dtoPk, oldDto, properties);
	}

}