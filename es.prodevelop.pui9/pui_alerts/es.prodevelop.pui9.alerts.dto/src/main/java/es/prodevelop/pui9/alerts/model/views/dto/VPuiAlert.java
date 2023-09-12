package es.prodevelop.pui9.alerts.model.views.dto;

import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlert;
import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.enums.GeometryType;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiEntity(tablename = "v_pui_alert")
@PuiGenerated
public class VPuiAlert extends AbstractViewDto implements IVPuiAlert {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.ID_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.completelyhidden)
	private Integer id;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.DESCRIPTION_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String description;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.TYPE_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String type;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.MODEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.COLUMN_NAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String columnname;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.PK_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 6, visibility = ColumnVisibility.visible)
	private String pk;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.PROCESSED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 8, visibility = ColumnVisibility.visible)
	private Integer processed;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.READ_COLUMN, ispk = false, nullable = true, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 9, visibility = ColumnVisibility.visible)
	private Integer read;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.CONTENT_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2147483647, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 10, visibility = ColumnVisibility.hidden)
	private String content;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.EMAILS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2147483647, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 11, visibility = ColumnVisibility.hidden)
	private String emails;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlert.LAUNCHING_DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	@PuiViewColumn(order = 7, visibility = ColumnVisibility.visible)
	private java.time.Instant launchingdatetime;

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
	public String getType() {
		return type;
	}

	@PuiGenerated
	@Override
	public void setType(String type) {
		this.type = type;
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
	public String getColumnname() {
		return columnname;
	}

	@PuiGenerated
	@Override
	public void setColumnname(String columnname) {
		this.columnname = columnname;
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
	public Integer getProcessed() {
		return processed;
	}

	@PuiGenerated
	@Override
	public void setProcessed(Integer processed) {
		this.processed = processed;
	}

	@PuiGenerated
	@Override
	public Integer getRead() {
		return read;
	}

	@PuiGenerated
	@Override
	public void setRead(Integer read) {
		this.read = read;
	}

	@PuiGenerated
	@Override
	public String getContent() {
		return content;
	}

	@PuiGenerated
	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@PuiGenerated
	@Override
	public String getEmails() {
		return emails;
	}

	@PuiGenerated
	@Override
	public void setEmails(String emails) {
		this.emails = emails;
	}

	@PuiGenerated
	@Override
	public java.time.Instant getLaunchingdatetime() {
		return launchingdatetime;
	}

	@PuiGenerated
	@Override
	public void setLaunchingdatetime(java.time.Instant launchingdatetime) {
		this.launchingdatetime = launchingdatetime;
	}
}
