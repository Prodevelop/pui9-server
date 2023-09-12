package es.prodevelop.pui9.alerts.model.views.dto;

import es.prodevelop.pui9.alerts.model.views.dto.interfaces.IVPuiAlertConfiguration;
import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "v_pui_alert_configuration")
public class VPuiAlertConfiguration extends AbstractViewDto implements IVPuiAlertConfiguration {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.ID_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.completelyhidden)
	private Integer id;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.DESCRIPTION_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private String description;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.MODEL_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.COLUMN_NAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String columnname;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.TYPE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String type;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.TIME_UNIT_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 7, visibility = ColumnVisibility.visible)
	private String timeunit;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.TIME_VALUE_COLUMN, ispk = false, nullable = true, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 6, visibility = ColumnVisibility.visible)
	private Integer timevalue;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.TIME_BEFORE_AFTER_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 10, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 8, visibility = ColumnVisibility.visible)
	private String timebeforeafter;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.CONTENT_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 9, visibility = ColumnVisibility.hidden)
	private String content;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.EMAILS_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 10, visibility = ColumnVisibility.hidden)
	private String emails;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 11, visibility = ColumnVisibility.visible)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IVPuiAlertConfiguration.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 12, visibility = ColumnVisibility.visible)
	private java.time.Instant datetime;

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
}
