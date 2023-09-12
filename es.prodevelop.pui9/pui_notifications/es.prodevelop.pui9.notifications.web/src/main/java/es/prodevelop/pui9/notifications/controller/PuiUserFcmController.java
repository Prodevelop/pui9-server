package es.prodevelop.pui9.notifications.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.notifications.model.dao.interfaces.IPuiUserFcmDao;
import es.prodevelop.pui9.notifications.model.dto.PuiUserFcm;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcmPk;
import es.prodevelop.pui9.notifications.service.interfaces.IPuiUserFcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@PuiGenerated
@Controller
@Tag(name = "PUI User FCM Notifications")
@RequestMapping("/puiuserfcm")
public class PuiUserFcmController extends
		AbstractCommonController<IPuiUserFcmPk, IPuiUserFcm, INullView, IPuiUserFcmDao, INullViewDao, IPuiUserFcmService> {
	@PuiGenerated
	@Override
	protected String getReadFunctionality() {
		return null;
	}

	@PuiGenerated
	@Override
	protected String getWriteFunctionality() {
		return null;
	}

	@Override
	public boolean allowList() {
		return false;
	}

	@Override
	public boolean allowGet() {
		return false;
	}

	@Override
	public boolean allowInsert() {
		return false;
	}

	@Override
	public boolean allowUpdate() {
		return false;
	}

	@Override
	public boolean allowDelete() {
		return false;
	}

	@GetMapping(value = "/registerFcmToken")
	@Operation(summary = "Register the given token to the logged user", description = "Register the given token to the logged user")
	public void registerFcmToken(@RequestParam String fcmToken) throws PuiServiceException {
		IPuiUserFcm userFcm = new PuiUserFcm();
		userFcm.setUsr(PuiUserSession.getCurrentSession().getUsr());
		userFcm.setToken(fcmToken);

		getService().registerUserFcmToken(userFcm);
	}

	@GetMapping(value = "/unregisterFcmToken")
	@Operation(summary = "Unregister the given from the logged user", description = "Unregister the given from the logged user")
	public void unregisterFcmToken(@RequestParam String fcmToken) throws PuiServiceException {
		IPuiUserFcm userFcm = new PuiUserFcm();
		userFcm.setUsr(PuiUserSession.getCurrentSession().getUsr());
		userFcm.setToken(fcmToken);

		getService().unregisterUserFcmToken(userFcm);
	}

}