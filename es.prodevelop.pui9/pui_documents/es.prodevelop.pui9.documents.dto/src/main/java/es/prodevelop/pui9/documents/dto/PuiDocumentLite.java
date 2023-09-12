package es.prodevelop.pui9.documents.dto;

import java.util.Map;

import es.prodevelop.pui9.file.PuiDocumentDefinition;
import es.prodevelop.pui9.utils.IPuiObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class PuiDocumentLite implements IPuiObject {

	private static final long serialVersionUID = 1L;

	@Schema(hidden = true)
	private transient PuiDocumentDefinition file;
	@Schema(hidden = true)
	private transient String uniqueFilename;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String model;
	private Map<String, Object> pk;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String role;
	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String description;
	private String language;

	public PuiDocumentDefinition getFile() {
		return file;
	}

	public void setFile(PuiDocumentDefinition file) {
		this.file = file;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Map<String, Object> getPk() {
		return pk;
	}

	public void setPk(Map<String, Object> pk) {
		this.pk = pk;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
