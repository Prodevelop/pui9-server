package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_importexport")
public class PuiImportexport extends PuiImportexportPk implements IPuiImportexport {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiImportexport.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IPuiImportexport.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IPuiImportexport.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant datetime;
	@PuiGenerated
	@PuiField(columnname = IPuiImportexport.FILENAME_CSV_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String filenamecsv;
	@PuiGenerated
	@PuiField(columnname = IPuiImportexport.FILENAME_JSON_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String filenamejson;
	@PuiGenerated
	@PuiField(columnname = IPuiImportexport.EXECUTED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private Integer executed = 0;

	@PuiGenerated
	@Override
	public String getModel() {
		return model;
	}

	@PuiGenerated
	@Override
	public void setModel(String model) {
		this.model = model;
	}

	@PuiGenerated
	@Override
	public String getUsr() {
		return usr;
	}

	@PuiGenerated
	@Override
	public void setUsr(String usr) {
		this.usr = usr;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getDatetime() {
		return datetime;
	}

	@PuiGenerated
	@Override
	public void setDatetime(java.time.Instant datetime) {
		this.datetime = datetime;
	}

	@PuiGenerated
	@Override
	public String getFilenamecsv() {
		return filenamecsv;
	}

	@PuiGenerated
	@Override
	public void setFilenamecsv(String filenamecsv) {
		this.filenamecsv = filenamecsv;
	}

	@PuiGenerated
	@Override
	public String getFilenamejson() {
		return filenamejson;
	}

	@PuiGenerated
	@Override
	public void setFilenamejson(String filenamejson) {
		this.filenamejson = filenamejson;
	}

	@PuiGenerated
	@Override
	public Integer getExecuted() {
		return executed;
	}

	@PuiGenerated
	@Override
	public void setExecuted(Integer executed) {
		this.executed = executed;
	}
}
