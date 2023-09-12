package es.prodevelop.pui9.docgen.dto;

import es.prodevelop.pui9.file.PuiDocumentDefinition;
import es.prodevelop.pui9.utils.IPuiObject;
import io.swagger.v3.oas.annotations.media.Schema;

public class PuiDocgenLite implements IPuiObject {

	private static final long serialVersionUID = 1L;

	@Schema(hidden = true)
	private transient PuiDocumentDefinition file;

	public PuiDocumentDefinition getFile() {
		return file;
	}

	public void setFile(PuiDocumentDefinition file) {
		this.file = file;
	}

}
