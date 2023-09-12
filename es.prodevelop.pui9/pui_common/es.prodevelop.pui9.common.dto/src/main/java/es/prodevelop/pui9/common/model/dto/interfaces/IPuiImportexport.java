package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiImportexport extends IPuiImportexportPk {
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String USR_COLUMN = "usr";
	@PuiGenerated
	String USR_FIELD = "usr";
	@PuiGenerated
	String DATETIME_COLUMN = "datetime";
	@PuiGenerated
	String DATETIME_FIELD = "datetime";
	@PuiGenerated
	String FILENAME_CSV_COLUMN = "filename_csv";
	@PuiGenerated
	String FILENAME_CSV_FIELD = "filenamecsv";
	@PuiGenerated
	String FILENAME_JSON_COLUMN = "filename_json";
	@PuiGenerated
	String FILENAME_JSON_FIELD = "filenamejson";
	@PuiGenerated
	String EXECUTED_COLUMN = "executed";
	@PuiGenerated
	String EXECUTED_FIELD = "executed";

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getUsr();

	@PuiGenerated
	void setUsr(String usr);

	@PuiGenerated
	java.time.Instant getDatetime();

	@PuiGenerated
	void setDatetime(java.time.Instant datetime);

	@PuiGenerated
	String getFilenamecsv();

	@PuiGenerated
	void setFilenamecsv(String filenamecsv);

	@PuiGenerated
	String getFilenamejson();

	@PuiGenerated
	void setFilenamejson(String filenamejson);

	@PuiGenerated
	Integer getExecuted();

	@PuiGenerated
	void setExecuted(Integer executed);
}
