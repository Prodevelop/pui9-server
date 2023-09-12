package es.prodevelop.pui9.notifications.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.notifications.model.dao.interfaces.IPuiUserFcmDao;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcmPk;
import es.prodevelop.pui9.service.interfaces.IService;

@PuiGenerated
public interface IPuiUserFcmService
		extends IService<IPuiUserFcmPk, IPuiUserFcm, INullView, IPuiUserFcmDao, INullViewDao> {

	/**
	 * Register the given token of the user
	 * 
	 * @param userFcm The pair user-token to be registered
	 */
	@Transactional(rollbackFor = PuiException.class)
	void registerUserFcmToken(IPuiUserFcm userFcm) throws PuiServiceException;

	/**
	 * Unregister the given token of the user
	 * 
	 * @param userFcmPk The token to be unregistered
	 */
	@Transactional(rollbackFor = PuiException.class)
	void unregisterUserFcmToken(IPuiUserFcmPk userFcmPk) throws PuiServiceException;

}
