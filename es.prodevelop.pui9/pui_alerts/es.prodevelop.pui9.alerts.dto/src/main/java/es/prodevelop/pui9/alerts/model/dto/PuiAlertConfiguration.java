package es.prodevelop.pui9.alerts.model.dto;

import java.time.Instant;

import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlertConfiguration;
import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.GeometryType;

@PuiEntity(tablename = "pui_alert_configuration")
@PuiGenerated
public class PuiAlertConfiguration extends PuiAlertConfigurationPk implements IPuiAlertConfiguration {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String description;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.MODEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.COLUMN_NAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String columnname;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.TYPE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String type = "MODEL";
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.TIME_UNIT_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String timeunit;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.TIME_VALUE_COLUMN, ispk = false, nullable = true, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private Integer timevalue;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.TIME_BEFORE_AFTER_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String timebeforeafter;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.CONTENT_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 2147483647, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String content;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.EMAILS_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 2147483647, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String emails;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private java.time.Instant datetime;
	@PuiGenerated
	@PuiField(columnname = IPuiAlertConfiguration.IS_CONTENT_HTML_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private Integer iscontenthtml = 0;

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
	public String getTimeunit() {
		return timeunit;
	}

	@PuiGenerated
	@Override
	public void setTimeunit(String timeunit) {
		this.timeunit = timeunit;
	}

	@PuiGenerated
	@Override
	public Integer getTimevalue() {
		return timevalue;
	}

	@PuiGenerated
	@Override
	public void setTimevalue(Integer timevalue) {
		this.timevalue = timevalue;
	}

	@PuiGenerated
	@Override
	public String getTimebeforeafter() {
		return timebeforeafter;
	}

	@PuiGenerated
	@Override
	public void setTimebeforeafter(String timebeforeafter) {
		this.timebeforeafter = timebeforeafter;
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
	public Integer getIscontenthtml() {
		return iscontenthtml;
	}

	@PuiGenerated
	@Override
	public void setIscontenthtml(Integer iscontenthtml) {
		this.iscontenthtml = iscontenthtml;
	}

	private Instant launchingdatetime;

	@Override
	public Instant getLaunchingdatetime() {
		return launchingdatetime;
	}

	@Override
	public void setLaunchingdatetime(Instant launchingdatetime) {
		this.launchingdatetime = launchingdatetime;
	}
}
