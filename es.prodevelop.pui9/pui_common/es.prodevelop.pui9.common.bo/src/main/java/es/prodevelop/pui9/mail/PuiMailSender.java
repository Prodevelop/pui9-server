package es.prodevelop.pui9.mail;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.mail.internet.InternetAddress;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.cypher.AESCypher;
import es.prodevelop.pui9.services.exceptions.PuiServiceWrongMailException;

/**
 * Utility class to send emails. This class is a singleton, and it's configured
 * using the properties of the table PUI_VARIABLE
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiMailSender extends AbstractPuiMailSender {

	private static PuiMailSender singleton;

	public static PuiMailSender getSingleton() {
		if (singleton == null) {
			singleton = new PuiMailSender();
		}

		return singleton;
	}

	private IPuiVariableService variableService;

	private PuiMailSender() {
		super();
		variableService = PuiApplicationContext.getInstance().getBean(IPuiVariableService.class);
		Assert.notNull(variableService, "PuiVariableService still not available");
	}

	public void configureSender() {
		if (variableService == null) {
			return;
		}

		mailSender = new JavaMailSenderImpl();

		String smtpHost = variableService.getVariable(PuiVariableValues.MAIL_SMTP_HOST.name());
		if (ObjectUtils.isEmpty(smtpHost)) {
			smtpHost = null;
		}

		Integer smtpPort = variableService.getVariable(Integer.class, PuiVariableValues.MAIL_SMTP_PORT.name());

		String smtpUser = variableService.getVariable(PuiVariableValues.MAIL_SMTP_USER.name());
		if (ObjectUtils.isEmpty(smtpUser)) {
			smtpUser = null;
		}

		String smtpPass = variableService.getVariable(PuiVariableValues.MAIL_SMTP_PASS.name());
		smtpPass = AESCypher.decrypt(smtpPass, PuiApplicationContext.getInstance().getBean("aesSecret", String.class));

		Boolean smtpAuth = variableService.getVariable(Boolean.class, PuiVariableValues.MAIL_SMTP_AUTH.name());
		if (smtpAuth == null) {
			smtpAuth = false;
		}

		Boolean smtpStarttlsEnable = variableService.getVariable(Boolean.class,
				PuiVariableValues.MAIL_SMTP_STARTTLS_ENABLE.name());
		if (smtpStarttlsEnable == null) {
			smtpStarttlsEnable = false;
		}

		String from = variableService.getVariable(PuiVariableValues.MAIL_FROM.name());
		String fromAlias = variableService.getVariable(PuiVariableValues.MAIL_FROM_ALIAS.name());
		if (!ObjectUtils.isEmpty(fromAlias)) {
			try {
				setFrom(new InternetAddress(from, fromAlias));
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			try {
				setFrom(from);
			} catch (PuiServiceWrongMailException e) {
				logger.error(e.getMessage(), e);
			}
		}

		mailSender.setHost(smtpHost);
		mailSender.setPort(smtpPort);
		mailSender.setUsername(smtpUser);
		mailSender.setPassword(smtpPass);
		mailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());
		mailSender.getJavaMailProperties().put("mail.smtp.auth", smtpAuth);
		mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", smtpStarttlsEnable);
		if (smtpStarttlsEnable) {
			mailSender.getJavaMailProperties().put("mail.smtp.starttls.required", "true");
			mailSender.getJavaMailProperties().put("mail.smtp.ssl.protocols", "TLSv1.2");
		}
	}

}
