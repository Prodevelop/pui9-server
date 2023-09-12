package es.prodevelop.pui9.eventlistener.listener;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.messages.PuiCommonMessages;
import es.prodevelop.pui9.common.messages.PuiCommonResourceBundle;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.eventlistener.event.PasswordExpirationEmailEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.mail.PuiMailConfiguration;
import es.prodevelop.pui9.mail.PuiMailSender;
import es.prodevelop.pui9.utils.PuiDateUtil;
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
public class PasswordExpirationEmailListener extends PuiListener<PasswordExpirationEmailEvent> {

	@Autowired
	protected IPuiVariableService variableService;

	@Value("classpath*:**/expiration_password*.html")
	private Resource[] resources;

	@Override
	protected void process(PasswordExpirationEmailEvent event) throws PuiException {
		IPuiUser puiUser = event.getSource();
		PuiLanguage lang = getLanguage(puiUser);

		String expireOnVal = PuiDateUtil.temporalAccessorToString(event.getPasswordValidity().getExpireOn(),
				DateTimeFormatter.ofPattern(puiUser.getDateformat()));
		String url = variableService.getVariable(PuiVariableValues.BASE_CLIENT_URL.name());
		String contentTemplate = getContentTemplate(lang);

		Map<String, Object> map = new LinkedHashMap<>();
		map.put("username", puiUser.getName());
		map.put("expireOn", expireOnVal);
		map.put("url", url);

		String subject = PuiCommonMessages.getSingleton().getString(getMailSubjectMessageId(), lang);
		String content = PuiMailConfiguration.compileTemplate(contentTemplate, map);
		PuiMailSender.getSingleton().send(PuiMailConfiguration.builder().withTo(puiUser.getEmail()).withSubject(subject)
				.withContent(content).withIsHtml(true));
	}

	private PuiLanguage getLanguage(IPuiUser puiUser) {
		if (!ObjectUtils.isEmpty(puiUser.getLanguage())) {
			return new PuiLanguage(puiUser.getLanguage());
		} else {
			return PuiLanguageUtils.getDefaultLanguage();
		}
	}

	private String getContentTemplate(PuiLanguage lang) throws PuiException {
		String contentTemplate = PuiResourcesManager.getResourceContent(resources, lang.getIsocode());
		if (contentTemplate == null) {
			throw new PuiException(
					"No template found for password expiration. The file expiration_password.html is not found in the classpath. Please, add it to the resources folder on your web project");
		}
		return contentTemplate;
	}

	private String getMailSubjectMessageId() {
		String mailSubjectLabelId = variableService
				.getVariable(PuiVariableValues.PASSWORD_EXPIRATION_MAIL_SUBJECT_LABEL_ID.name());
		if (mailSubjectLabelId == null) {
			mailSubjectLabelId = PuiCommonResourceBundle.passwordExpirationSubject;
		}

		return mailSubjectLabelId;
	}

}
