package es.prodevelop.pui9.mail;

import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.classpath.PuiClassLoaderUtils;
import es.prodevelop.pui9.services.exceptions.PuiServiceNoMailContentException;
import es.prodevelop.pui9.services.exceptions.PuiServiceWrongMailException;

public class PuiMailConfiguration {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);
	private static VelocityEngine engine;
	static {
		engine = new VelocityEngine();
		engine.addProperty("runtime.log", FileUtils.getTempDirectory().toString() + File.separator + "velocity.log");
		engine.init();
	}

	public static PuiMailConfiguration builder() {
		return new PuiMailConfiguration();
	}

	private InternetAddress from;
	private Set<InternetAddress> to;
	private Set<InternetAddress> cc;
	private Set<InternetAddress> bcc;
	private String subject;
	private String content;
	private boolean isHtml;
	private List<PuiMailAttachment> attachments;

	private PuiMailConfiguration() {
	}

	public PuiMailConfiguration withFrom(String from) throws PuiServiceWrongMailException {
		validateEmail(from);
		try {
			return withFrom(new InternetAddress(from));
		} catch (AddressException e) {
			return this;
		}
	}

	public PuiMailConfiguration withFrom(InternetAddress from) {
		this.from = from;
		return this;
	}

	public PuiMailConfiguration withTo(String to) throws PuiServiceWrongMailException {
		return withTo(Collections.singleton(to));
	}

	public PuiMailConfiguration withToInternetAddress(InternetAddress to) {
		return withToInternetAddress(Collections.singleton(to));
	}

	public PuiMailConfiguration withTo(Set<String> to) throws PuiServiceWrongMailException {
		validateEmails(to);
		this.withToInternetAddress(to.stream().map(t -> {
			try {
				return new InternetAddress(t);
			} catch (AddressException e) {
				return null;
			}
		}).collect(Collectors.toSet()));
		return this;
	}

	public PuiMailConfiguration withToInternetAddress(Set<InternetAddress> to) {
		if (this.to == null) {
			this.to = new LinkedHashSet<>();
		}
		this.to.addAll(to);
		return this;
	}

	public PuiMailConfiguration withCc(String cc) throws PuiServiceWrongMailException {
		return withCc(Collections.singleton(cc));
	}

	public PuiMailConfiguration withCcInternetAddress(InternetAddress cc) {
		return withCcInternetAddress(Collections.singleton(cc));
	}

	public PuiMailConfiguration withCc(Set<String> cc) throws PuiServiceWrongMailException {
		validateEmails(cc);
		this.withCcInternetAddress(cc.stream().map(c -> {
			try {
				return new InternetAddress(c);
			} catch (AddressException e) {
				return null;
			}
		}).collect(Collectors.toSet()));
		return this;
	}

	public PuiMailConfiguration withCcInternetAddress(Set<InternetAddress> cc) {
		if (this.cc == null) {
			this.cc = new LinkedHashSet<>();
		}
		this.cc.addAll(cc);
		return this;
	}

	public PuiMailConfiguration withBcc(String bcc) throws PuiServiceWrongMailException {
		return withBcc(Collections.singleton(bcc));
	}

	public PuiMailConfiguration withBccInternetAddress(InternetAddress bcc) {
		return withBccInternetAddress(Collections.singleton(bcc));
	}

	public PuiMailConfiguration withBcc(Set<String> bcc) throws PuiServiceWrongMailException {
		validateEmails(bcc);
		this.withBccInternetAddress(bcc.stream().map(b -> {
			try {
				return new InternetAddress(b);
			} catch (AddressException e) {
				return null;
			}
		}).collect(Collectors.toSet()));
		return this;
	}

	public PuiMailConfiguration withBccInternetAddress(Set<InternetAddress> bcc) {
		if (this.bcc == null) {
			this.bcc = new LinkedHashSet<>();
		}
		this.bcc.addAll(bcc);
		return this;
	}

	public PuiMailConfiguration withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public PuiMailConfiguration withContent(String content) throws PuiServiceNoMailContentException {
		if (ObjectUtils.isEmpty(content)) {
			throw new PuiServiceNoMailContentException();
		}
		this.content = content;
		return this;
	}

	public PuiMailConfiguration withIsHtml(boolean isHtml) {
		this.isHtml = isHtml;
		return this;
	}

	public PuiMailConfiguration withAttachment(PuiMailAttachment attachment) {
		return withAttachment(Collections.singletonList(attachment));
	}

	public PuiMailConfiguration withAttachment(List<PuiMailAttachment> attachments) {
		if (this.attachments == null) {
			this.attachments = new ArrayList<>();
		}
		this.attachments.addAll(attachments);
		return this;
	}

	public InternetAddress getFrom() {
		return from;
	}

	public Set<InternetAddress> getTo() {
		if (to == null) {
			to = new LinkedHashSet<>();
		}
		return to;
	}

	public Set<InternetAddress> getCc() {
		if (cc == null) {
			cc = new LinkedHashSet<>();
		}
		return cc;
	}

	public Set<InternetAddress> getBcc() {
		if (bcc == null) {
			bcc = new LinkedHashSet<>();
		}
		return bcc;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public boolean isHtml() {
		return isHtml;
	}

	public List<PuiMailAttachment> getAttachments() {
		if (attachments == null) {
			attachments = new ArrayList<>();
		}
		return attachments;

	}

	private void validateEmails(Set<String> emails) throws PuiServiceWrongMailException {
		for (String email : emails) {
			validateEmail(email);
		}
	}

	public static void validateEmail(String email) throws PuiServiceWrongMailException {
		if (!StringUtils.hasText(email)) {
			throw new PuiServiceWrongMailException(email);
		}

		email = email.trim();
		boolean isValid = EMAIL_PATTERN.matcher(email).matches();
		if (!isValid) {
			throw new PuiServiceWrongMailException(email);
		}
	}

	/**
	 * Compile the given template (as path) with the given parameters using Velocity
	 * 
	 * @param templatePath The path of the template
	 * @param parameters   The map with the parameters for velocity
	 * @return The content compiled
	 */
	public static String compileTemplateFromClasspath(String templatePath, Map<String, Object> parameters) {
		try {
			String template = IOUtils.toString(PuiClassLoaderUtils.getClassLoader().getResourceAsStream(templatePath),
					StandardCharsets.UTF_8);
			return compileTemplate(template, parameters);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Compile the given template with the given parameters using Velocity
	 * 
	 * @param templateThe path of the template
	 * @param parameters  The map with the parameters for velocity
	 * @return The content compiled
	 */
	public static String compileTemplate(String template, Map<String, Object> parameters) {
		try {
			VelocityContext context = new VelocityContext();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}

			InputStreamReader isr = new InputStreamReader(IOUtils.toInputStream(template, StandardCharsets.UTF_8));
			StringWriter writer = new StringWriter();
			boolean processed = engine.evaluate(context, writer, "PUI_MAIL", isr);

			return processed ? writer.toString() : null;
		} catch (Exception e) {
			return null;
		}
	}

}
