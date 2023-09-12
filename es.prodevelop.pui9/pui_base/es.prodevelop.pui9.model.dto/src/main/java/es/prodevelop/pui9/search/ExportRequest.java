package es.prodevelop.pui9.search;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

/**
 * Object to be used in the Grid Export request. Has the same attributes of a
 * {@link SearchRequest}, and others to specify the visible columns of the grid
 * and the exporting type
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Schema(description = "Export Request List description")
public class ExportRequest extends SearchRequest {

	private static final long serialVersionUID = 1L;

	@Schema(description = "A map representing the column name and the title of the column", requiredMode = RequiredMode.NOT_REQUIRED)
	private List<ExportColumnDefinition> exportColumns = new ArrayList<>();

	@Schema(description = "The title for the export file", requiredMode = RequiredMode.NOT_REQUIRED)
	private String exportTitle;

	@Schema(description = "The export type {csv, excel}", requiredMode = RequiredMode.NOT_REQUIRED)
	private ExportType exportType = ExportType.excel;

	@Schema(description = "Character for decimals", requiredMode = RequiredMode.NOT_REQUIRED)
	private char decimalChar = '.';

	@Schema(description = "Generic date format to use", requiredMode = RequiredMode.NOT_REQUIRED)
	private String dateformat;

	private transient Long total;

	public List<ExportColumnDefinition> getExportColumns() {
		return exportColumns;
	}

	public void setExportColumns(List<ExportColumnDefinition> exportColumns) {
		this.exportColumns = exportColumns;
	}

	public String getExportTitle() {
		return exportTitle;
	}

	public void setExportTitle(String exportTitle) {
		this.exportTitle = exportTitle;
	}

	public ExportType getExportType() {
		return exportType != null ? exportType : ExportType.excel;
	}

	public void setExportType(ExportType exportType) {
		this.exportType = exportType;
	}

	public char getDecimalChar() {
		return decimalChar;
	}

	public void setDecimalChar(char decimalChar) {
		this.decimalChar = decimalChar;
	}

	public String getDateformat() {
		return dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(", type: " + exportType);
		return super.toString();
	}

}
