package es.prodevelop.pui9.common.dto.issue;

import java.util.List;

import es.prodevelop.pui9.file.AttachmentDefinition;
import es.prodevelop.pui9.utils.IPuiObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class IssueTicket implements IPuiObject {

	private static final long serialVersionUID = 1L;

	@Schema(hidden = true)
	private transient List<AttachmentDefinition> files;

	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String subject;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String content;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private IssueUrgencyEnum urgency;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String name;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String email;
	private String phone;

	public List<AttachmentDefinition> getFiles() {
		return files;
	}

	public void setFiles(List<AttachmentDefinition> files) {
		this.files = files;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public IssueUrgencyEnum getUrgency() {
		return urgency;
	}

	public void setUrgency(IssueUrgencyEnum urgency) {
		this.urgency = urgency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
