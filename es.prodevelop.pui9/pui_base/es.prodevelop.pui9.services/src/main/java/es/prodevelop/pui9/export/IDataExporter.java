package es.prodevelop.pui9.export;

import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.ExportType;
import es.prodevelop.pui9.services.exceptions.PuiServiceExportException;

/**
 * This interface is intended to be implemented by all the data exporters
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IDataExporter {

	/**
	 * Get the export type of this data exporter
	 * 
	 * @return The export type of this data exporter
	 */
	ExportType getExportType();

	/**
	 * Generate the file using the configuration provided in the given request
	 * 
	 * @param req The request of the export
	 * @return The generated file represented by a {@link FileDownload}
	 * @throws PuiServiceExportException If any error occurs during the export
	 */
	FileDownload export(ExportRequest req) throws PuiServiceExportException;

}
