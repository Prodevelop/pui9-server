package es.prodevelop.pui9.alerts.model.dto;

import es.prodevelop.pui9.alerts.model.dto.interfaces.IPuiAlert;
import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.GeometryType;

@PuiEntity(tablename = "pui_alert")
@PuiGenerated
public class PuiAlert extends PuiAlertPk implements IPuiAlert {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiAlert.ALERT_CONFIG_ID_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private Integer alertconfigid;
	@PuiGenerated
	@PuiField(columnname = IPuiAlert.PK_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private String pk;
	@PuiGenerated
	@PuiField(columnname = IPuiAlert.PROCESSED_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private Integer processed = 0;
	@PuiGenerated
	@PuiField(columnname = IPuiAlert.LAUNCHING_DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private java.time.Instant launchingdatetime;
	@PuiGenerated
	@PuiField(columnname = IPuiAlert.READ_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, geometrytype = GeometryType.NONE, issequence = false)
	private Integer read = 0;

	@PuiGenerated
	@Override
	public Integer getAlertconfigid() {
		return alertconfigid;
	}

	@PuiGenerated
	@Override
	public void setAlertconfigid(Integer alertconfigid) {
		this.alertconfigid = alertconfigid;
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
	public java.time.Instant getLaunchingdatetime() {
		return launchingdatetime;
	}

	@PuiGenerated
	@Override
	public void setLaunchingdatetime(java.time.Instant launchingdatetime) {
		this.launchingdatetime = launchingdatetime;
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
}
