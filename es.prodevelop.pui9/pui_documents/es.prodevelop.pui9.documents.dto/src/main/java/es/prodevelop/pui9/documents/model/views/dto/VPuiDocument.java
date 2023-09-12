package es.prodevelop.pui9.documents.model.views.dto;

import java.util.Map;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.documents.model.views.dto.interfaces.IVPuiDocument;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "v_pui_document")
public class VPuiDocument extends AbstractViewDto implements IVPuiDocument {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.ID_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private Integer id;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.PK_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String pk;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String description;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.LANGUAGE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String language;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.FILENAME_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 6, visibility = ColumnVisibility.visible)
	private String filename;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.FILENAME_ORIG_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 7, visibility = ColumnVisibility.visible)
	private String filenameorig;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.ROLE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 8, visibility = ColumnVisibility.visible)
	private String role;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.ROLE_DESCRIPTION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 9, visibility = ColumnVisibility.visible)
	private String roledescription;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.THUMBNAILS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 10, visibility = ColumnVisibility.visible)
	private String thumbnails;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.URL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 4000, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 11, visibility = ColumnVisibility.visible)
	private String url;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 12, visibility = ColumnVisibility.visible)
	private java.time.Instant datetime;
	@PuiGenerated
	@PuiField(columnname = IVPuiDocument.LANG_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 13, visibility = ColumnVisibility.completelyhidden)
	private String lang;

	@PuiGenerated
	@Override
	public Integer getId() {
		return id;
	}

	@PuiGenerated
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

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
	public String getRoledescription() {
		return roledescription;
	}

	@PuiGenerated
	@Override
	public void setRoledescription(String roledescription) {
		this.roledescription = roledescription;
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
	public String getUrl() {
		return url;
	}

	@PuiGenerated
	@Override
	public void setUrl(String url) {
		this.url = url;
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
	public String getLang() {
		return lang;
	}

	@PuiGenerated
	@Override
	public void setLang(String lang) {
		this.lang = lang;
	}

	private Map<String, Object> extraData;

	@Override
	public Map<String, Object> getExtraData() {
		return extraData;
	}

	@Override
	public void setExtraData(Map<String, Object> extraData) {
		this.extraData = extraData;
	}

}
