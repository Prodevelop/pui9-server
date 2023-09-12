package es.prodevelop.pui9.mail;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.services.exceptions.PuiServiceSendMailException;
import es.prodevelop.pui9.services.exceptions.PuiServiceWrongMailException;

/**
 * Utility class to send emails. This class is a singleton, and it's configured
 * using a file named "pui_email.properties" that should exist in the resources
 * folder of the application
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiMailSender {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	protected JavaMailSenderImpl mailSender;
	private InternetAddress from;

	/**
	 * The properties set in the file should be the following:
	 * <ul>
	 * <li><b>pui.mail.from</b>: the email used in the From tag</li>
	 * <li><b>pui.mail.smtp.host</b>: the SMTP server</li>
	 * <li><b>pui.mail.smtp.port</b>: the port of the SMTP server (by default
	 * 587)</li>
	 * <li><b>pui.mail.smtp.user</b>: the user to authenticate in the SMTP
	 * server</li>
	 * <li><b>pui.mail.smtp.pass</b>: the password of the SMTP server user</li>
	 * <li><b>pui.mail.smtp.default.encoding</b>: the encoding to be used in the
	 * email (by default UTF8</li>
	 * <li><b>pui.mail.smtp.auth</b>: if authentication is needed (by default
	 * true)</li>
	 * <li><b>pui.mail.smtp.starttls.enable</b>: if TLS o SSL connection is needed
	 * (by default true)</li>
	 * </ul>
	 */
	protected AbstractPuiMailSender() {
		mailSender = new JavaMailSenderImpl();
	}

	public abstract void configureSender();

	/**
	 * Set the "from" with the provided one
	 * 
	 * @param from The new "from" email
	 */
	protected void setFrom(String from) throws PuiServiceWrongMailException {
		try {
			setFrom(new InternetAddress(from));
		} catch (AddressException e) {
			throw new PuiServiceWrongMailException(from);
		}
	}

	/**
	 * Set the "from" with the provided, using InternetAddress, that allows you to
	 * set the email and a identifying name
	 * 
	 * @param from The new "from" email
	 */
	protected void setFrom(InternetAddress from) {
		this.from = from;
	}

	/**
	 * Send an email providing the following information in the parameters:
	 * 
	 * @param config The email configuration
	 * @throws Exception If any exception is thrown while sending the email
	 */
	public void send(PuiMailConfiguration config) throws PuiServiceSendMailException {
		configureSender();

		if (ObjectUtils.isEmpty(config.getTo()) && ObjectUtils.isEmpty(config.getCc())
				&& ObjectUtils.isEmpty(config.getBcc())) {
			throw new PuiServiceSendMailException("Missing receiver email address");
		}

		InternetAddress realFrom = config.getFrom() != null ? config.getFrom() : this.from;
		logger.debug("Sending email from '" + realFrom + "' to '" + String.join(",", config.getTo().toString())
				+ "' (cc: '" + String.join(",", config.getCc().toString()) + "', bcc: '"
				+ String.join(",", config.getBcc().toString()) + "') [through '" + mailSender.getHost() + ":"
				+ mailSender.getPort() + "', username: '" + mailSender.getUsername() + "']");

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, !config.getAttachments().isEmpty(),
					StandardCharsets.UTF_8.name());

			helper.setFrom(realFrom);
			helper.setTo(config.getTo().toArray(new InternetAddress[0]));
			helper.setCc(config.getCc().toArray(new InternetAddress[0]));
			helper.setBcc(config.getBcc().toArray(new InternetAddress[0]));
			helper.setSubject(config.getSubject());
			helper.setText(config.getContent(), config.isHtml());

			// attachments
			for (PuiMailAttachment attachment : config.getAttachments()) {
				InputStreamSource byteArrayResource = new ByteArrayResource(attachment.getContent());
				if (StringUtils.hasText(attachment.getContentType())) {
					helper.addAttachment(attachment.getFilename(), byteArrayResource, attachment.getContentType());
				} else {
					helper.addAttachment(attachment.getFilename(), byteArrayResource);
				}
			}
		} catch (MessagingException e) {
			throw new PuiServiceSendMailException(e.getMessage());
		}

		try {
			mailSender.send(message);
		} catch (MailException e) {
			throw new PuiServiceSendMailException(e.getMessage());
		}

		logger.debug("The email was sent correctly");
	}

}
