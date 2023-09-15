package es.prodevelop.pui9.common.service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.google.gson.JsonParseException;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.exceptions.PuiCommonInvalidPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonSamePasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserEmailDuplicatedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserResetTokenException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiProfileDao;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserDao;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserProfileDao;
import es.prodevelop.pui9.common.model.dto.PuiUserPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfilePk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfilePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiUserDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.eventlistener.event.PasswordChangedEvent;
import es.prodevelop.pui9.eventlistener.event.PasswordExpirationEmailEvent;
import es.prodevelop.pui9.eventlistener.event.PasswordResetEvent;
import es.prodevelop.pui9.eventlistener.event.RequestResetPasswordEvent;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.login.PasswordValidity;
import es.prodevelop.pui9.login.PuiPasswordEncoders;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.service.MultiValuedAttribute;
import es.prodevelop.pui9.services.exceptions.PuiServiceIncorrectUserPasswordException;
import es.prodevelop.pui9.threads.PuiMultiInstanceProcessBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

@PuiGenerated
@Service
public class PuiUserService extends AbstractService<IPuiUserPk, IPuiUser, IVPuiUser, IPuiUserDao, IVPuiUserDao>
		implements IPuiUserService {

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private PuiMultiInstanceProcessBackgroundExecutors multiInstanceProcessBackExec;

	@PostConstruct
	private void postConstructUserService() {
		Long initDelay = PuiMultiInstanceProcessBackgroundExecutors.getNextExecutionDelayAsMinutes(1, 0);

		multiInstanceProcessBackExec.registerNewExecutor("PasswordValidityCheck", initDelay, TimeUnit.DAYS.toMinutes(1),
				TimeUnit.MINUTES, this::checkAllPasswordValidity);
		multiInstanceProcessBackExec.registerNewExecutor("CleanResetPasswordToken", 0, TimeUnit.MINUTES.toMinutes(30),
				TimeUnit.MINUTES, this::cleanResetPasswordToken);
	}

	@Override
	protected void addMultiValuedAttributes() {
		{
			List<Pair<String, String>> localAttributes = new ArrayList<>();
			localAttributes.add(Pair.of(IPuiUserPk.USR_FIELD, IPuiUserProfilePk.USR_FIELD));

			List<Pair<String, String>> referencedAttributes = new ArrayList<>();
			referencedAttributes.add(Pair.of(IPuiProfilePk.PROFILE_FIELD, IPuiUserProfilePk.PROFILE_FIELD));

			addMultiValuedAttribute(new MultiValuedAttribute<>(IPuiUser.PROFILES_FIELD, localAttributes,
					referencedAttributes, IPuiProfileDao.class, IPuiUserProfileDao.class));
		}
	}

	@Override
	public IPuiUserPk delete(IPuiUserPk dtoPk) throws PuiServiceDeleteException {
		// avoid to delete physically the user. Set it deleted logically
		try {
			IPuiUser user = getUserLite(dtoPk.getUsr());
			user.setDisabled(PuiConstants.TRUE_INT);
			update(user);

			return dtoPk;
		} catch (PuiCommonUserNotExistsException | PuiServiceUpdateException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	public void dropUser(IPuiUserPk pk) throws PuiServiceDeleteException {
		super.delete(pk);
	}

	@Override
	protected void beforeInsert(IPuiUser dto) throws PuiServiceException {
		checkEmailExists(dto.getEmail());

		if (!ObjectUtils.isEmpty(dto.getPassword())) {
			try {
				checkPasswordPattern(dto.getPassword());
			} catch (PuiCommonInvalidPasswordException e) {
				throw new PuiServiceInsertException(e);
			}

			String encryptedPassword = PuiPasswordEncoders.bCryptPasswordEncoder.encode(dto.getPassword());
			dto.setPassword(encryptedPassword);
		}
	}

	@Override
	protected void beforeUpdate(IPuiUser oldDto, IPuiUser dto) throws PuiServiceException {
		dto.setPassword(oldDto.getPassword());
		if (!Objects.equals(oldDto.getEmail(), dto.getEmail())) {
			checkEmailExists(dto.getEmail());
		}
	}

	@Override
	public IPuiUser getUserLite(String user) throws PuiCommonUserNotExistsException {
		if (ObjectUtils.isEmpty(user)) {
			throw new PuiCommonUserNotExistsException(user);
		}

		IPuiUser puiUser;
		try {
			puiUser = getTableDao().findOne(new PuiUserPk(user));
		} catch (PuiDaoFindException e) {
			puiUser = null;
		}

		if (puiUser == null) {
			throw new PuiCommonUserNotExistsException(user);
		}

		return puiUser;
	}

	@Override
	public PasswordValidity checkPasswordValidity(String user) throws PuiCommonUserNotExistsException {
		IPuiUser puiUser;
		try {
			puiUser = getTableDao().findOne(new PuiUserPk(user));
		} catch (PuiDaoFindException e) {
			throw new PuiCommonUserNotExistsException(user);
		}
		return checkPasswordValidity(puiUser);
	}

	@Override
	public PasswordValidity checkPasswordValidity(IPuiUser user) {
		PasswordValidity passVal = new PasswordValidity();
		passVal.setChangePasswordOnLogin(user.getChangepasswordnextlogin().equals(PuiConstants.TRUE_INT));

		Integer validDays = variableService.getVariable(Integer.class,
				PuiVariableValues.PASSWORD_EXPIRATION_DAYS.name());
		Integer rememberDays = variableService.getVariable(Integer.class,
				PuiVariableValues.PASSWORD_REMEMBER_DAYS.name());
		if (ObjectUtils.isEmpty(user.getPassword()) || user.getLastpasswordchange() == null || validDays == null
				|| rememberDays == null) {
			passVal.setValid(true);
			passVal.setNotifyExpiration(false);
			passVal.setExpireOn(null);
			return passVal;
		}

		Instant expireOn = user.getLastpasswordchange().plusSeconds(TimeUnit.DAYS.toSeconds(validDays))
				.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		passVal.setExpireOn(expireOn);

		long daysToExpiration = Duration.between(Instant.now(), expireOn).abs().toDays();
		passVal.setNotifyExpiration(daysToExpiration <= rememberDays);

		long daysFromLastChange = Duration.between(Instant.now(), user.getLastpasswordchange()).abs().toDays();
		passVal.setValid(daysFromLastChange < validDays);

		return passVal;
	}

	@Override
	public void enableUser(String usr) throws PuiCommonUserNotExistsException {
		try {
			IPuiUser user = getTableDao().findOne(new PuiUserPk(usr));
			if (user == null) {
				throw new PuiCommonUserNotExistsException(usr);
			}
			enableUser(user);
		} catch (PuiDaoFindException e) {
			// do nothing
		}
	}

	private void enableUser(IPuiUser user) {
		user.setDisabled(PuiConstants.FALSE_INT);
		user.setDisableddate(null);
		user.setLoginwrongattempts(0);

		Map<String, Object> map = new LinkedHashMap<>();
		map.put(IPuiUser.DISABLED_COLUMN, user.getDisabled());
		map.put(IPuiUser.DISABLED_DATE_COLUMN, user.getDisableddate());
		map.put(IPuiUser.LOGIN_WRONG_ATTEMPTS_COLUMN, 0);

		try {
			getTableDao().patch(user.createPk(), map);
		} catch (PuiDaoSaveException e) {
			// do nothing
		}
	}

	@Override
	public void disableUser(String usr) throws PuiCommonUserNotExistsException {
		try {
			IPuiUser user = getTableDao().findOne(new PuiUserPk(usr));
			if (user == null) {
				throw new PuiCommonUserNotExistsException(usr);
			}
			disableUser(user);
		} catch (PuiDaoFindException e) {
			// do nothing
		}
	}

	private void disableUser(IPuiUser user) {
		user.setDisabled(PuiConstants.TRUE_INT);
		user.setDisableddate(Instant.now());

		Map<String, Object> map = new LinkedHashMap<>();
		map.put(IPuiUser.DISABLED_COLUMN, user.getDisabled());
		map.put(IPuiUser.DISABLED_DATE_COLUMN, user.getDisableddate());

		try {
			getTableDao().patch(user.createPk(), map);
		} catch (PuiDaoSaveException e) {
			// do nothing
		}
	}

	@Override
	public void setLastAccess(IPuiUserPk pk, Instant loginTime, String loginIp) throws PuiCommonUserNotExistsException {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(IPuiUser.LAST_ACCESS_TIME_COLUMN, loginTime);
		map.put(IPuiUser.LAST_ACCESS_IP_COLUMN, loginIp);
		map.put(IPuiUser.LOGIN_WRONG_ATTEMPTS_COLUMN, 0);

		try {
			getTableDao().patch(pk, map);
		} catch (PuiDaoSaveException e) {
			// do nothing
		}
	}

	@Override
	public boolean setWrongLogin(IPuiUserPk pk) {
		IPuiUser user;
		try {
			user = getTableDao().findOne(pk);
		} catch (PuiDaoFindException e) {
			user = null;
		}

		if (user == null) {
			return false;
		}

		user.setLoginwrongattempts(user.getLoginwrongattempts() + 1);

		try {
			getTableDao().patch(pk,
					Collections.singletonMap(IPuiUser.LOGIN_WRONG_ATTEMPTS_COLUMN, user.getLoginwrongattempts()));
		} catch (PuiDaoSaveException e) {
			// do nothing
		}

		Integer maxAttempts = variableService.getVariable(Integer.class, PuiVariableValues.LOGIN_MAX_ATTEMPTS.name());
		if (maxAttempts != null && user.getLoginwrongattempts() >= maxAttempts) {
			disableUser(user);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void requestResetPassword(String usrEmail) {
		// try to find the user by the "usr" property
		List<IPuiUser> list;
		try {
			list = getTableDao().findByUsr(usrEmail);
		} catch (PuiDaoFindException e) {
			list = Collections.emptyList();
		}
		if (ObjectUtils.isEmpty(list)) {
			// if the value is the email, try to find the user by the "email" property
			try {
				list = getTableDao().findByEmail(usrEmail);
			} catch (PuiDaoFindException e) {
				list = Collections.emptyList();
			}
		}

		list.forEach(puiUser -> getEventLauncher().fireAsync(new RequestResetPasswordEvent(puiUser)));
	}

	@Override
	public void doResetPassword(String resetToken, String newPassword) throws PuiCommonUserResetTokenException,
			PuiServiceUpdateException, PuiCommonInvalidPasswordException, PuiCommonSamePasswordException {
		List<IPuiUser> list;
		try {
			list = getTableDao().findByResetpasswordtoken(resetToken);
		} catch (PuiDaoFindException e) {
			list = Collections.emptyList();
		}

		if (ObjectUtils.isEmpty(list)) {
			throw new PuiCommonUserResetTokenException();
		}

		for (IPuiUser puiUser : list) {
			doSetPassword(puiUser, newPassword, true);
			getEventLauncher().fireAsync(new PasswordResetEvent(puiUser));
		}
	}

	@Override
	public void changeUserPassword(IPuiUserPk pk, String oldPassword, String newPassword, boolean force)
			throws PuiServiceIncorrectUserPasswordException, PuiCommonUserNotExistsException, PuiServiceUpdateException,
			PuiCommonInvalidPasswordException, PuiCommonSamePasswordException {
		if (ObjectUtils.isEmpty(oldPassword)) {
			oldPassword = null;
		}
		if (ObjectUtils.isEmpty(newPassword)) {
			newPassword = null;
		}

		IPuiUser user = getUserLite(pk.getUsr());

		if (!force && !ObjectUtils.isEmpty(oldPassword) && !ObjectUtils.isEmpty(user.getPassword())
				&& !PuiPasswordEncoders.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			throw new PuiServiceIncorrectUserPasswordException();
		}

		doSetPassword(user, newPassword, true);
	}

	@Override
	public void doSetPassword(IPuiUser user, String newPassword, boolean notifyWithEvent)
			throws PuiServiceUpdateException, PuiCommonInvalidPasswordException, PuiCommonSamePasswordException {
		checkPassword(user, newPassword);
		String newPasswordBCrypt = newPassword != null ? PuiPasswordEncoders.bCryptPasswordEncoder.encode(newPassword)
				: null;

		Map<String, Object> map = new LinkedHashMap<>();
		map.put(IPuiUser.PASSWORD_COLUMN, newPasswordBCrypt);
		map.put(IPuiUser.LAST_PASSWORD_CHANGE_COLUMN, Instant.now());
		map.put(IPuiUser.RESET_PASSWORD_TOKEN_COLUMN, null);
		map.put(IPuiUser.RESET_PASSWORD_TOKEN_DATE_COLUMN, null);
		map.put(IPuiUser.DISABLED_COLUMN, PuiConstants.FALSE_INT);
		map.put(IPuiUser.DISABLED_DATE_COLUMN, null);
		map.put(IPuiUser.LOGIN_WRONG_ATTEMPTS_COLUMN, 0);
		map.put(IPuiUser.CHANGE_PASSWORD_NEXT_LOGIN_COLUMN, PuiConstants.FALSE_INT);

		try {
			getTableDao().patch(user, map);
		} catch (PuiDaoSaveException e) {
			throw new PuiServiceUpdateException(e);
		}

		if (notifyWithEvent) {
			getEventLauncher().fireAsync(new PasswordChangedEvent(user.getUsr(), newPassword));
		}
	}

	private void checkEmailExists(String email) throws PuiCommonUserEmailDuplicatedException {
		if (ObjectUtils.isEmpty(email)) {
			return;
		}

		List<IPuiUser> list;
		try {
			list = getTableDao().findByEmail(email);
		} catch (PuiDaoFindException e) {
			list = Collections.emptyList();
		}

		if (!ObjectUtils.isEmpty(list)) {
			throw new PuiCommonUserEmailDuplicatedException(email);
		}
	}

	private void checkAllPasswordValidity() {
		boolean emailNotify = variableService.getVariable(Boolean.class,
				PuiVariableValues.PASSWORD_EXPIRATION_MAIL_NOTIFY.name());
		SearchRequest req = new SearchRequest();
		req.setRows(1000);
		req.setOrder(Collections.singletonList(Order.newOrderAsc(IPuiUserPk.USR_COLUMN)));
		req.setFilter(FilterBuilder.newAndFilter().addEquals(IPuiUser.DISABLED_COLUMN, PuiConstants.FALSE_INT)
				.addIsNotNull(IPuiUser.PASSWORD_COLUMN).addIsNotNull(IPuiUser.EMAIL_COLUMN).asFilterGroup());

		getTableDao().executePaginagedOperation(req, user -> {
			PasswordValidity passVal = checkPasswordValidity(user);
			if (emailNotify && passVal.isNotifyExpiration() && passVal.isValid()) {
				getEventLauncher().fireAsync(new PasswordExpirationEmailEvent(user, passVal));
			}
			if (!passVal.isValid()) {
				disableUser(user);
			}
		}, null);
	}

	private void cleanResetPasswordToken() {
		Instant now = Instant.now();

		SearchRequest req = new SearchRequest();
		req.setRows(1000);
		req.setOrder(Collections.singletonList(Order.newOrderAsc(IPuiUserPk.USR_COLUMN)));
		req.setFilter(FilterBuilder.newAndFilter().addIsNotNull(IPuiUser.RESET_PASSWORD_TOKEN_COLUMN).asFilterGroup());

		getTableDao().executePaginagedOperation(req, user -> {
			if (user.getResetpasswordtokendate() == null
					|| ChronoUnit.MINUTES.between(user.getResetpasswordtokendate(), now) >= 30) {
				user.setResetpasswordtoken(null);
				user.setResetpasswordtokendate(null);

				Map<String, Object> map = new LinkedHashMap<>();
				map.put(IPuiUser.RESET_PASSWORD_TOKEN_COLUMN, null);
				map.put(IPuiUser.RESET_PASSWORD_TOKEN_DATE_COLUMN, null);
				try {
					getTableDao().patch(user.createPk(), map);
				} catch (PuiDaoSaveException e) {
					// do nothing
				}
			}
		}, null);
	}

	private void checkPassword(IPuiUser user, String newPassword)
			throws PuiCommonInvalidPasswordException, PuiCommonSamePasswordException {
		checkSamePassword(user, newPassword);
		checkPasswordPattern(newPassword);
	}

	private void checkSamePassword(IPuiUser user, String newPassword) throws PuiCommonSamePasswordException {
		if (!ObjectUtils.isEmpty(user.getPassword()) && !ObjectUtils.isEmpty(newPassword)
				&& PuiPasswordEncoders.bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
			throw new PuiCommonSamePasswordException();
		}
	}

	private void checkPasswordPattern(String newPassword) throws PuiCommonInvalidPasswordException {
		String regex = variableService.getVariable(String.class, PuiVariableValues.PASSWORD_PATTERN.name());

		if (ObjectUtils.isEmpty(regex)) {
			return;
		}

		String desc = variableService.getVariable(String.class, PuiVariableValues.PASSWORD_PATTERN_INFO.name());
		if (desc != null) {
			Map<String, String> map;
			try {
				map = GsonSingleton.getSingleton().getGson().fromJson(desc,
						new ParameterizedTypeReference<HashMap<String, String>>() {
						}.getType());
			} catch (JsonParseException e) {
				map = Collections.emptyMap();
			}

			if (!ObjectUtils.isEmpty(map)) {
				String descLang = map.get(PuiUserSession.getSessionLanguage().getIsocode());
				if (ObjectUtils.isEmpty(descLang)) {
					descLang = map.get(PuiLanguageUtils.getDefaultLanguage().getIsocode());
				}
				desc = descLang;
			}
		} else {
			desc = "";
		}

		if (ObjectUtils.isEmpty(newPassword) || !newPassword.matches(regex)) {
			throw new PuiCommonInvalidPasswordException(desc);
		}
	}

}