package es.prodevelop.pui9.common.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiAudit extends IViewDto {
	@PuiGenerated
	String ID_COLUMN = "id";
	@PuiGenerated
	String ID_FIELD = "id";
	@PuiGenerated
	String DATETIME_COLUMN = "datetime";
	@PuiGenerated
	String DATETIME_FIELD = "datetime";
	@PuiGenerated
	String USR_COLUMN = "usr";
	@PuiGenerated
	String USR_FIELD = "usr";
	@PuiGenerated
	String USERNAME_COLUMN = "username";
	@PuiGenerated
	String USERNAME_FIELD = "username";
	@PuiGenerated
	String CLIENT_COLUMN = "client";
	@PuiGenerated
	String CLIENT_FIELD = "client";
	@PuiGenerated
	String IP_COLUMN = "ip";
	@PuiGenerated
	String IP_FIELD = "ip";
	@PuiGenerated
	String TYPE_COLUMN = "type";
	@PuiGenerated
	String TYPE_FIELD = "type";
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String PK_COLUMN = "pk";
	@PuiGenerated
	String PK_FIELD = "pk";
	@PuiGenerated
	String CONTENT_COLUMN = "content";
	@PuiGenerated
	String CONTENT_FIELD = "content";

	@PuiGenerated
	Integer getId();

	@PuiGenerated
	void setId(Integer id);

	@PuiGenerated
	java.time.Instant getDatetime();

	@PuiGenerated
	void setDatetime(java.time.Instant datetime);

	@PuiGenerated
	String getUsr();

	@PuiGenerated
	void setUsr(String usr);

	@PuiGenerated
	String getUsername();

	@PuiGenerated
	void setUsername(String username);

	@PuiGenerated
	String getClient();

	@PuiGenerated
	void setClient(String client);

	@PuiGenerated
	String getIp();

	@PuiGenerated
	void setIp(String ip);

	@PuiGenerated
	String getType();

	@PuiGenerated
	void setType(String type);

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getPk();

	@PuiGenerated
	void setPk(String pk);

	@PuiGenerated
	String getContent();

	@PuiGenerated
	void setContent(String content);
}
