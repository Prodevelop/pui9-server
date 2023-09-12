package es.prodevelop.pui9.documents.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.documents.model.dto.interfaces.IPuiDocument;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_document")
public class PuiDocument extends PuiDocumentPk implements IPuiDocument {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiDocument.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IPuiDocument.PK_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String pk;
	@PuiGenerated
	@PuiField(columnname = IPuiDocument.LANGUAGE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	private String language;
	@PuiGenerated
	@PuiField(columnname = IPuiDocument.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String description;
	@PuiGenerated
	@PuiField(columnname = IPuiDocument.FILENAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String filename;
	@PuiGenerated
	@PuiField(columnname = IPuiDocument.FILENAME_ORIG_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String filenameorig;
	@PuiGenerated
	@PuiField(columnname = IPuiDocument.ROLE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String role;
	@PuiGenerated
	@PuiField(columnname = IPuiDocument.THUMBNAILS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String thumbnails;
	@PuiGenerated
	@PuiField(columnname = IPuiDocument.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant datetime;

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
	public String getPk() {
		return pk;
	}

	@PuiGenerated
	@Override
	public void setPk(String pk) {
		this.pk = pk;
	}

	@PuiGenerated
	@Override
	public String getLanguage() {
		return language;
	}

	@PuiGenerated
	@Override
	public void setLanguage(String language) {
		this.language = language;
	}

	@PuiGenerated
	@Override
	public String getDescription() {
		return description;
	}

	@PuiGenerated
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@PuiGenerated
	@Override
	public String getFilename() {
		return filename;
	}

	@PuiGenerated
	@Override
	public void setFilename(String filename) {
		this.filename = filename;
	}

	@PuiGenerated
	@Override
	public String getFilenameorig() {
		return filenameorig;
	}

	@PuiGenerated
	@Override
	public void setFilenameorig(String filenameorig) {
		this.filenameorig = filenameorig;
	}

	@PuiGenerated
	@Override
	public String getRole() {
		return role;
	}

	@PuiGenerated
	@Override
	public void setRole(String role) {
		this.role = role;
	}

	@PuiGenerated
	@Override
	public String getThumbnails() {
		return thumbnails;
	}

	@PuiGenerated
	@Override
	public void setThumbnails(String thumbnails) {
		this.thumbnails = thumbnails;
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

	private String url;

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}
}
