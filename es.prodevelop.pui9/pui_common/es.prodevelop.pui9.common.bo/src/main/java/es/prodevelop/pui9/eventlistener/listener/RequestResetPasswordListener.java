package es.prodevelop.pui9.eventlistener.listener;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.messages.PuiCommonMessages;
import es.prodevelop.pui9.common.messages.PuiCommonResourceBundle;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.eventlistener.event.RequestResetPasswordEvent;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.mail.PuiMailConfiguration;
import es.prodevelop.pui9.mail.PuiMailSender;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import es.prodevelop.pui9.utils.PuiResourcesManager;

/**
 * Listener fired when a user request to reset the password. It sends a
 * verification email to the user with a URL to generate a new password. This
 * URL is available only for 30 minutes
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class RequestResetPasswordListener extends PuiListener<RequestResetPasswordEvent> {

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private IPuiUserService userService;

	@Value("classpath*:**/request_reset_password*.html")
	private Resource[] resources;

	@Override
	protected boolean passFilter(RequestResetPasswordEvent event) {
		return !ObjectUtils.isEmpty(event.getSource().getPassword())
				&& !ObjectUtils.isEmpty(event.getSource().getEmail());
	}

	@Override
	protected void process(RequestResetPasswordEvent event) throws PuiException {
		IPuiUser puiUser = event.getSource();
		PuiLanguage lang = getLanguage(puiUser);

		modifyUser(puiUser);
		String url = getUrl(puiUser, lang);
		String contentTemplate = getContentTemplate(lang);

		Map<String, Object> map = new LinkedHashMap<>();
		map.put("username", puiUser.getName());
		map.put("url", url);

		String subject = PuiCommonMessages.getSingleton().getString(getMailSubjectMessageId(), lang);
		String content = PuiMailConfiguration.compileTemplate(contentTemplate, map);
		PuiMailSender.getSingleton().send(PuiMailConfiguration.builder().withTo(puiUser.getEmail()).withSubject(subject)
				.withContent(content).withIsHtml(true));

		updateUser(puiUser);
	}

	private PuiLanguage getLanguage(IPuiUser puiUser) {
		if (!ObjectUtils.isEmpty(puiUser.getLanguage())) {
			return new PuiLanguage(puiUser.getLanguage());
		} else {
			return PuiLanguageUtils.getDefaultLanguage();
		}
	}

	private void modifyUser(IPuiUser puiUser) {
		puiUser.setResetpasswordtoken(generateRandomString());
		puiUser.setResetpasswordtokendate(Instant.now());
	}

	private String getUrl(IPuiUser puiUser, PuiLanguage lang) {
		String baseUrl = variableService.getVariable(PuiVariableValues.BASE_CLIENT_URL.name());
		if (!baseUrl.endsWith("/")) {
			baseUrl += "/";
		}
		String resetPasswordUrl = "resetPassword";
		String params = "?resetToken=" + puiUser.getResetpasswordtoken() + "&lang=" + lang.getIsocode();
		return baseUrl + resetPasswordUrl + params;
	}

	private String getContentTemplate(PuiLanguage lang) throws PuiException {
		String contentTemplate = PuiResourcesManager.getResourceContent(resources, lang.getIsocode());
		if (contentTemplate == null) {
			throw new PuiException(
					"No template found for reseting the password. The file request_reset_password.html is not found in the classpath. Please, add it to the resources folder on your web project");
		}
		return contentTemplate;
	}

	private String getMailSubjectMessageId() {
		String mailSubjectLabelId = variableService
				.getVariable(PuiVariableValues.PASSWORD_CHANGE_MAIL_SUBJECT_LABEL_ID.name());
		if (mailSubjectLabelId == null) {
			mailSubjectLabelId = PuiCommonResourceBundle.requestResetPasswordSubject;
		}

		return mailSubjectLabelId;
	}

	private String generateRandomString() {
		final int length = 100;
		final char[] allAllowed = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789".toCharArray();
		Random random = new SecureRandom();
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < length; i++) {
			password.append(allAllowed[random.nextInt(allAllowed.length)]);
		}

		return password.toString();
	}

	private void updateUser(IPuiUser puiUser) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(IPuiUser.RESET_PASSWORD_TOKEN_COLUMN, puiUser.getResetpasswordtoken());
		map.put(IPuiUser.RESET_PASSWORD_TOKEN_DATE_COLUMN, puiUser.getResetpasswordtokendate());
		try {
			userService.getTableDao().patch(puiUser.createPk(), map);
		} catch (PuiDaoSaveException e) {
			// do nothing
		}
	}

}
