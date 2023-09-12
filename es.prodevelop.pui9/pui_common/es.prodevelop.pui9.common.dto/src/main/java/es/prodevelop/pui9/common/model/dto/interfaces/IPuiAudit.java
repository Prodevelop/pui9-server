package es.prodevelop.pui9.common.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiAudit extends IPuiAuditPk {
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String TYPE_COLUMN = "type";
	@PuiGenerated
	String TYPE_FIELD = "type";
	@PuiGenerated
	String PK_COLUMN = "pk";
	@PuiGenerated
	String PK_FIELD = "pk";
	@PuiGenerated
	String DATETIME_COLUMN = "datetime";
	@PuiGenerated
	String DATETIME_FIELD = "datetime";
	@PuiGenerated
	String USR_COLUMN = "usr";
	@PuiGenerated
	String USR_FIELD = "usr";
	@PuiGenerated
	String IP_COLUMN = "ip";
	@PuiGenerated
	String IP_FIELD = "ip";
	@PuiGenerated
	String CONTENT_COLUMN = "content";
	@PuiGenerated
	String CONTENT_FIELD = "content";
	@PuiGenerated
	String CLIENT_COLUMN = "client";
	@PuiGenerated
	String CLIENT_FIELD = "client";

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getType();

	@PuiGenerated
	void setType(String type);

	@PuiGenerated
	String getPk();

	@PuiGenerated
	void setPk(String pk);

	@PuiGenerated
	java.time.Instant getDatetime();

	@PuiGenerated
	void setDatetime(java.time.Instant datetime);

	@PuiGenerated
	String getUsr();

	@PuiGenerated
	void setUsr(String usr);

	@PuiGenerated
	String getIp();

	@PuiGenerated
	void setIp(String ip);

	@PuiGenerated
	String getContent();

	@PuiGenerated
	void setContent(String content);

	@PuiGenerated
	String getClient();

	@PuiGenerated
	void setClient(String client);
}
