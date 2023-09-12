package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.search.ExportRequest;

/**
 * Event for the Export action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ExportEvent extends PuiEvent<ExportRequest> {

	private static final long serialVersionUID = 1L;

	private FileDownload fileDownload;

	public ExportEvent(ExportRequest request, FileDownload fileDownload) {
		super(request);
		this.fileDownload = fileDownload;
	}

	public FileDownload getFileDownload() {
		return fileDownload;
	}

}
