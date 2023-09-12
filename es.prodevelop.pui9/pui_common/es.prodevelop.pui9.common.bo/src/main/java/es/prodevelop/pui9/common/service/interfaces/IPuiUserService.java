package es.prodevelop.pui9.common.service.interfaces;

import java.time.Instant;

import org.springframework.transaction.annotation.Transactional;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.exceptions.PuiCommonInvalidPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonSamePasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserResetTokenException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiUserDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.login.PasswordValidity;
import es.prodevelop.pui9.service.interfaces.IService;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectUserPasswordException;

@PuiGenerated
public interface IPuiUserService extends IService<IPuiUserPk, IPuiUser, IVPuiUser, IPuiUserDao, IVPuiUserDao> {

	IPuiUser getUserLite(String user) throws PuiCommonUserNotExistsException;

	/**
	 * Drop the user from the database, instead of disabling it (like the delete
	 * function does)
	 * 
	 * @param pk The user
	 * @throws PuiServiceDeleteException If any exception deleting the user is
	 *                                   thrown
	 */
	@Transactional(rollbackFor = PuiException.class)
	void dropUser(IPuiUserPk pk) throws PuiServiceDeleteException;

	/**
	 * Check the validity of the password of the given user. It checks if it's still
	 * available or if the user should be change it
	 * 
	 * @param pk The user name
	 * @return Information about the password validity
	 */
	PasswordValidity checkPasswordValidity(String user) throws PuiCommonUserNotExistsException;

	/**
	 * Check the validity of the password of the given user. It checks if it's still
	 * available or if the user should be change it
	 * 
	 * @param user The user object
	 * @return Information about the password validity
	 */
	PasswordValidity checkPasswordValidity(IPuiUser user);

	/**
	 * Enable the given user. This way, this user can login the application
	 * 
	 * @param usr The user
	 * @throws PuiCommonUserNotExistsException If the user doesn't exist
	 */
	@Transactional(rollbackFor = PuiException.class)
	void enableUser(String usr) throws PuiCommonUserNotExistsException;

	/**
	 * Disable the given user. This way, this user cannot login the application
	 * 
	 * @param usr The user
	 * @throws PuiCommonUserNotExistsException If the user doesn't exist
	 */
	@Transactional(rollbackFor = PuiException.class)
	void disableUser(String usr) throws PuiCommonUserNotExistsException;

	/**
	 * Set the last access information for the given user
	 * 
	 * @param pk        The user
	 * @param loginTime The login datetime
	 * @param loginIp   The login ip
	 * @throws PuiCommonUserNotExistsException If the user doesn't exist
	 */
	@Transactional(rollbackFor = PuiException.class)
	void setLastAccess(IPuiUserPk pk, Instant loginTime, String loginIp) throws PuiCommonUserNotExistsException;

	/**
	 * Register a wrong login for the given user due to bad credentials
	 * 
	 * @param user The user
	 * @return true if the user was disabled due to maximum login attempts
	 */
	@Transactional(rollbackFor = PuiException.class)
	boolean setWrongLogin(IPuiUserPk pk);

	/**
	 * Request for reseting the password of the user. The request will be available
	 * for 30 minutes. A reset token will be generated and the user will be marked
	 * as pending of new password
	 * 
	 * @param usrEmail The user/email to reset the password
	 * @return True if the request finished correctly; false if the user doesn't
	 *         exist
	 */
	@Transactional(rollbackFor = PuiException.class)
	void requestResetPassword(String usrEmail);

	/**
	 * Do the password reset.
	 * 
	 * @param resetToken  The token to ensure that the user want to reset the
	 *                    password
	 * @param newPassword The new password to be set (in plain)
	 * @throws PuiCommonUserResetTokenException  If the provided token doesn't exist
	 * @throws PuiServiceUpdateException         If fails while updating the
	 *                                           registry in the database
	 * @throws PuiCommonInvalidPasswordException If the password doesn't fit the
	 *                                           security requirements
	 * @throws PuiCommonSamePasswordException    If the current password and the new
	 *                                           one are the same
	 */
	@Transactional(rollbackFor = PuiException.class)
	void doResetPassword(String resetToken, String newPassword) throws PuiCommonUserResetTokenException,
			PuiServiceUpdateException, PuiCommonInvalidPasswordException, PuiCommonSamePasswordException;

	/**
	 * Change the password of the given user, checking that the old password is the
	 * same than the provided one
	 * 
	 * @param pk          The user
	 * @param oldPassword The old password
	 * @param newPassword The new password
	 * @param force       Force changing the password without the need of the old
	 *                    password
	 * @throws PuiServiceIncorrectUserPasswordException If the current password
	 *                                                  doesn't fit
	 * @throws PuiCommonUserNotExistsException          If the user doesn't exist
	 * @throws PuiServiceUpdateException                If an error updating the
	 *                                                  user occurred
	 * @throws PuiCommonInvalidPasswordException        If the new password doesn't
	 *                                                  accomplish the needs
	 * @throws PuiCommonSamePasswordException           If the current password and
	 *                                                  the new one are the same
	 */
	@Transactional(rollbackFor = PuiException.class)
	void changeUserPassword(IPuiUserPk pk, String oldPassword, String newPassword, boolean force)
			throws PuiServiceIncorrectUserPasswordException, PuiCommonUserNotExistsException, PuiServiceUpdateException,
			PuiCommonInvalidPasswordException, PuiCommonSamePasswordException;

	/**
	 * Efectively changes the password of the user with the given password, whitout
	 * checking the old password
	 * 
	 * @param user            The whole user object
	 * @param newPassword     The new password
	 * @param notifyWithEvent If the event for changing the password should be fired
	 *                        or not
	 * @throws PuiServiceUpdateException         If an error updating the user
	 *                                           occurred
	 * @throws PuiCommonInvalidPasswordException If the new password doesn't
	 *                                           accomplish the needs
	 * @throws PuiCommonSamePasswordException    If the current password and the new
	 *                                           one are the same
	 */
	@Transactional(rollbackFor = PuiException.class)
	void doSetPassword(IPuiUser user, String newPassword, boolean notifyWithEvent)
			throws PuiServiceUpdateException, PuiCommonInvalidPasswordException, PuiCommonSamePasswordException;

}