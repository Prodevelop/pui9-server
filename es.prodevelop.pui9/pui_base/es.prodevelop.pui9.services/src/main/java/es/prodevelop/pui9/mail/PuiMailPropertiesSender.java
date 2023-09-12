package es.prodevelop.pui9.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.internet.InternetAddress;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.services.exceptions.PuiServiceWrongMailException;
import es.prodevelop.pui9.utils.PuiPropertiesManager;

/**
 * Utility class to send emails. This class is a singleton, and it's configured
 * using a file named "pui_email.properties" that should exist in the resources
 * folder of the application
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiMailPropertiesSender extends AbstractPuiMailSender {

	private static final String PROPERTIES_FILE_NAME = "pui_email.properties";
	private static PuiMailPropertiesSender singleton;

	public static PuiMailPropertiesSender getSingleton() {
		if (singleton == null) {
			singleton = new PuiMailPropertiesSender();
		}

		return singleton;
	}

	private Properties props = null;

	@Override
	public void configureSender() {
		if (props != null) {
			return;
		}

		try {
			props = PuiPropertiesManager.loadPropertiesFile(PROPERTIES_FILE_NAME);
		} catch (IOException e) {
			throw new RuntimeException("No email properties file available on resources folder");
		}

		mailSender = new JavaMailSenderImpl();

		String smtpHost = props.getProperty("MAIL_SMTP_HOST");
		if (ObjectUtils.isEmpty(smtpHost)) {
			smtpHost = null;
		}

		Integer smtpPort = 25;
		String smtpPortVal = props.getProperty("MAIL_SMTP_PORT");
		if (!ObjectUtils.isEmpty(smtpPortVal)) {
			smtpPort = Integer.parseInt(smtpPortVal);
		}

		String smtpUser = props.getProperty("MAIL_SMTP_USER");
		if (ObjectUtils.isEmpty(smtpUser)) {
			smtpUser = null;
		}

		String smtpPass = props.getProperty("MAIL_SMTP_PASS");
		if (ObjectUtils.isEmpty(smtpPass)) {
			smtpPass = null;
		}

		Boolean smtpAuth = false;
		String smtpAuthVal = props.getProperty("MAIL_SMTP_AUTH");
		if (!ObjectUtils.isEmpty(smtpAuthVal)) {
			smtpAuth = Boolean.valueOf(smtpAuthVal);
		}

		Boolean smtpStarttlsEnable = false;
		String smtpStarttlsEnableVal = props.getProperty("MAIL_SMTP_STARTTLS_ENABLE");
		if (!ObjectUtils.isEmpty(smtpStarttlsEnableVal)) {
			smtpStarttlsEnable = Boolean.valueOf(smtpStarttlsEnableVal);
		}

		String from = props.getProperty("MAIL_FROM");
		String fromAlias = props.getProperty("MAIL_FROM_ALIAS");
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
