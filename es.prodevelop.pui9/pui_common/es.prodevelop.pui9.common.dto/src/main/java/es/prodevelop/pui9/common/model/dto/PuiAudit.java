package es.prodevelop.pui9.common.model.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.enums.ColumnType;

@PuiGenerated
@PuiEntity(tablename = "pui_audit")
public class PuiAudit extends PuiAuditPk implements IPuiAudit {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IPuiAudit.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IPuiAudit.TYPE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	private String type;
	@PuiGenerated
	@PuiField(columnname = IPuiAudit.PK_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String pk;
	@PuiGenerated
	@PuiField(columnname = IPuiAudit.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private java.time.Instant datetime;
	@PuiGenerated
	@PuiField(columnname = IPuiAudit.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IPuiAudit.IP_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String ip = "0.0.0.0";
	@PuiGenerated
	@PuiField(columnname = IPuiAudit.CONTENT_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	private String content;
	@PuiGenerated
	@PuiField(columnname = IPuiAudit.CLIENT_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	private String client;

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
	public String getIp() {
		return ip;
	}

	@PuiGenerated
	@Override
	public void setIp(String ip) {
		this.ip = ip;
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
	public String getClient() {
		return client;
	}

	@PuiGenerated
	@Override
	public void setClient(String client) {
		this.client = client;
	}
}
