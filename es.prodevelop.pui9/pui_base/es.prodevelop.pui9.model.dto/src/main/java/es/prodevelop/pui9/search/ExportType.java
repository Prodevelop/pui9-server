package es.prodevelop.pui9.search;

/**
 * The allowed Grid Exporting types
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public enum ExportType {

	excel("xlsx"),

	csv("csv"),

	pdf("pdf");

	public final String extension;

	private ExportType(String extension) {
		this.extension = extension;
	}

}