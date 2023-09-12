package es.prodevelop.pui9.docgen.eventlistener.event;

import es.prodevelop.pui9.docgen.dto.GenerateDocgenRequest;
import es.prodevelop.pui9.docgen.model.dto.interfaces.IPuiDocgenTemplatePk;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.file.FileDownload;

/**
 * Adapter for Delete an element from Database
 */
public class DocgenGenerateEvent extends PuiEvent<IPuiDocgenTemplatePk> {

	private static final long serialVersionUID = 1L;

	private GenerateDocgenRequest info;
	private FileDownload file;

	public DocgenGenerateEvent(GenerateDocgenRequest info, FileDownload file) {
		super(info.getPk());
		this.info = info;
		this.file = file;
	}

	public GenerateDocgenRequest getInfo() {
		return info;
	}

	public FileDownload getFile() {
		return file;
	}

}
