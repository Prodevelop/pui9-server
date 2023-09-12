package es.prodevelop.pui9.login;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserProfileDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfile;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiUserFunctionalityDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUserFunctionality;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.login.PuiUserSession.PuiUserSessionFunctionality;
import es.prodevelop.pui9.login.PuiUserSession.PuiUserSessionProfile;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * This class is used to controll the login of the users within the application
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiUserDetailsService implements UserDetailsService {

	@Autowired
	private IPuiUserService puiUserService;

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private IVPuiUserFunctionalityDao userFunctionalityDao;

	@Autowired
	private IPuiUserProfileDao userProfileDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		IPuiUser puiUser;
		try {
			puiUser = loadUser(username);
		} catch (PuiCommonUserNotExistsException e) {
			throw new UsernameNotFoundException(username);
		}

		return buildUserSession(puiUser);
	}

	/**
	 * Fill all the profiles and functionalities for the given session
	 * 
	 * @param userSession The user session to be populated
	 */
	public void fillAuthorities(PuiUserSession userSession) {
		List<PuiUserSessionProfile> profiles = getUserProfiles(userSession.getUsr());
		List<PuiUserSessionFunctionality> functionalities = getUserFunctionalities(userSession.getUsr());

		userSession.setFullProfiles(profiles);
		userSession.setFullFunctionalities(functionalities);
	}

	/**
	 * Find the given user and return a {@link IPuiUser} object
	 * 
	 * @param username The user to find
	 * @return The {@link IPuiUser} object filled the most possible
	 * @throws PuiCommonUserNotExistsException If the user doesn't exist
	 */
	private IPuiUser loadUser(String username) throws PuiCommonUserNotExistsException {
		return puiUserService.getUserLite(username);
	}

	/**
	 * Build the object {@link PuiUserSession} with the given information
	 * 
	 * @param puiUser  The user
	 * @param profiles The profiles
	 */
	protected PuiUserSession buildUserSession(IPuiUser puiUser) {
		PasswordValidity passwordValidity = puiUserService.checkPasswordValidity(puiUser);
		boolean credentialsExpired = !passwordValidity.isValid();
		boolean use2fa = is2faEnabled(puiUser);

		check2faSecretKey(puiUser, use2fa);

		return PuiUserSession.createNew(puiUser.getUsr()).withName(puiUser.getName())
				.withPassword(puiUser.getPassword()).withLanguage(new PuiLanguage(puiUser.getLanguage()))
				.withEmail(puiUser.getEmail()).withDateFormat(puiUser.getDateformat())
				.withDisabled(puiUser.getDisabled().equals(PuiConstants.TRUE_INT)).withAccountExpired(false)
				.withCredentialsExpired(credentialsExpired).withLastLoginTime(puiUser.getLastaccesstime())
				.withLastLoginIp(puiUser.getLastaccessip()).withPasswordValidity(passwordValidity).withUse2fa(use2fa)
				.withSecret2fa(puiUser.getSecret2fa()).withAuthenticated(!use2fa);
	}

	/**
	 * Check if 2FA is enabled or not
	 * 
	 * @return True or False
	 */
	private boolean is2faEnabled(IPuiUser puiUser) {
		return !ObjectUtils.isEmpty(puiUser.getPassword())
				&& variableService.getVariable(Boolean.class, PuiVariableValues.LOGIN_ENABLE_2FA.name());
	}

	/**
	 * If the user has not a secret key, generate a new one for him
	 * 
	 * @param puiUser The user to assign the secret key
	 */
	private void check2faSecretKey(IPuiUser puiUser, boolean use2fa) {
		if (!use2fa || puiUser.getSecret2fa() != null) {
			return;
		}

		puiUser.setSecret2fa(getRandomSecretKey());
		try {
			puiUserService.getTableDao().patch(puiUser.createPk(),
					Collections.singletonMap(IPuiUser.SECRET_2FA_COLUMN, puiUser.getSecret2fa()));
		} catch (PuiDaoSaveException e) {
			// do nothing
		}
	}

	/**
	 * Generates a new secret key for 2FA
	 * 
	 * @return a secret key
	 */
	private String getRandomSecretKey() {
		byte[] bytes = new byte[10];
		new SecureRandom().nextBytes(bytes);
		return new Base32().encodeToString(bytes);
	}

	/**
	 * Get the list of profiles assigned to the user
	 * 
	 * @param user The user
	 * @return The list of profiles of the user
	 */
	private List<PuiUserSessionProfile> getUserProfiles(String user) {
		List<IPuiUserProfile> profiles;
		try {
			profiles = userProfileDao.findByUsr(user);
		} catch (PuiDaoFindException e) {
			profiles = Collections.emptyList();
		}

		List<PuiUserSessionProfile> fullProfiles = new ArrayList<>();
		profiles.forEach(profile -> fullProfiles.add(PuiUserSessionProfile.of(profile.getProfile())));

		return fullProfiles;
	}

	/**
	 * Get the list of functionalities assigned to the user
	 * 
	 * @param user The user
	 * @return The list of functionalities of the user
	 */
	private List<PuiUserSessionFunctionality> getUserFunctionalities(String user) {
		List<IVPuiUserFunctionality> functionalities;
		try {
			functionalities = userFunctionalityDao.findByUsr(user);
		} catch (PuiDaoFindException e) {
			functionalities = Collections.emptyList();
		}

		List<PuiUserSessionFunctionality> fullFunctionalities = new ArrayList<>();
		functionalities.forEach(functionality -> fullFunctionalities
				.add(PuiUserSessionFunctionality.of(functionality.getProfile(), functionality.getFunctionality())));

		return fullFunctionalities;
	}

}
