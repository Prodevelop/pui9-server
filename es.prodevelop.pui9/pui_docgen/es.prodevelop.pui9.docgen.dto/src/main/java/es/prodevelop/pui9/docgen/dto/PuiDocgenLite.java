package es.prodevelop.pui9.docgen.dto;

import es.prodevelop.pui9.file.AttachmentDefinition;
import es.prodevelop.pui9.utils.IPuiObject;
import io.swagger.v3.oas.annotations.media.Schema;

public class PuiDocgenLite implements IPuiObject {

	private static final long serialVersionUID = 1L;

	@Schema(hidden = true)
	private transient AttachmentDefinition file;

	public AttachmentDefinition getFile() {
		return file;
	}

	public void setFile(AttachmentDefinition file) {
		this.file = file;
	}

}
