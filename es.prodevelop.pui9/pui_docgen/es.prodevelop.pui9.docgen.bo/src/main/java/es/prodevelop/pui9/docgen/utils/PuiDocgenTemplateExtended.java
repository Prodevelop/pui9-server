package es.prodevelop.pui9.docgen.utils;

import org.apache.commons.io.FilenameUtils;

import es.prodevelop.pui9.docgen.model.dto.PuiDocgenTemplate;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplate;
import es.prodevelop.pui9.file.PuiDocumentDefinition;
import es.prodevelop.pui9.utils.PuiObjectUtils;
import io.swagger.v3.oas.annotations.media.Schema;

public class PuiDocgenTemplateExtended extends PuiDocgenTemplate {

	private static final long serialVersionUID = 1L;

	@Schema(hidden = true)
	private PuiDocumentDefinition file;
	@Schema(hidden = true)
	private transient String uniqueFilename;

	@Schema(hidden = true)
	public PuiDocumentDefinition getFile() {
		return file;
	}

	public void setFile(PuiDocumentDefinition document) {
		this.file = document;
	}

	@Schema(hidden = true)
	public String getFullFileName() {
		return getFileName() + "." + getFileExtension();
	}

	@Schema(hidden = true)
	public String getUniqueFullFileName() {
		if (uniqueFilename == null) {
			uniqueFilename = getFileName() + "_" + System.currentTimeMillis() + "." + getFileExtension();
		}
		return uniqueFilename;
	}

	private String getFileName() {
		return FilenameUtils.getBaseName(file.getOriginalFileName());
	}

	private String getFileExtension() {
		return FilenameUtils.getExtension(file.getOriginalFileName()).toLowerCase();
	}

	public IPuiDocgenTemplate asPuiDocgenTemplate() {
		IPuiDocgenTemplate template = new PuiDocgenTemplate();
		PuiObjectUtils.copyProperties(template, this);
		return template;
	}

}
