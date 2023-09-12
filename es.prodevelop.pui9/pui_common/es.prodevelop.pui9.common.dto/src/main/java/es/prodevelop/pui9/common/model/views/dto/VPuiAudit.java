package es.prodevelop.pui9.common.model.views.dto;

import es.prodevelop.pui9.annotations.PuiEntity;
import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiAudit;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.AbstractViewDto;

@PuiGenerated
@PuiEntity(tablename = "v_pui_audit")
public class VPuiAudit extends AbstractViewDto implements IVPuiAudit {
	@PuiGenerated
	private static final long serialVersionUID = 1L;
	@PuiGenerated
	@PuiField(columnname = IVPuiAudit.ID_COLUMN, ispk = false, nullable = false, type = ColumnType.numeric, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 1, visibility = ColumnVisibility.visible)
	private Integer id;
	@PuiGenerated
	@PuiField(columnname = IVPuiAudit.DATETIME_COLUMN, ispk = false, nullable = false, type = ColumnType.datetime, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 2, visibility = ColumnVisibility.visible)
	private java.time.Instant datetime;
	@PuiGenerated
	@PuiField(columnname = IVPuiAudit.USR_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 3, visibility = ColumnVisibility.visible)
	private String usr;
	@PuiGenerated
	@PuiField(columnname = IVPuiAudit.USERNAME_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 200, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 4, visibility = ColumnVisibility.visible)
	private String username;
	@PuiGenerated
	@PuiField(columnname = IVPuiAudit.CLIENT_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 5, visibility = ColumnVisibility.visible)
	private String client;
	@PuiGenerated
	@PuiField(columnname = IVPuiAudit.IP_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 6, visibility = ColumnVisibility.visible)
	private String ip;
	@PuiGenerated
	@PuiField(columnname = IVPuiAudit.TYPE_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 50, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 7, visibility = ColumnVisibility.visible)
	private String type;
	@PuiGenerated
	@PuiField(columnname = IVPuiAudit.MODEL_COLUMN, ispk = false, nullable = false, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 8, visibility = ColumnVisibility.visible)
	private String model;
	@PuiGenerated
	@PuiField(columnname = IVPuiAudit.PK_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = 100, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 9, visibility = ColumnVisibility.visible)
	private String pk;
	@PuiGenerated
	@PuiField(columnname = IVPuiAudit.CONTENT_COLUMN, ispk = false, nullable = true, type = ColumnType.text, autoincrementable = false, maxlength = -1, islang = false, isgeometry = false, issequence = false)
	@PuiViewColumn(order = 10, visibility = ColumnVisibility.visible)
	private String content;

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
	public String getUsername() {
		return username;
	}

	@PuiGenerated
	@Override
	public void setUsername(String username) {
		this.username = username;
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
	public String getContent() {
		return content;
	}

	@PuiGenerated
	@Override
	public void setContent(String content) {
		this.content = content;
	}
}
