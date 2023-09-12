package es.prodevelop.pui9.alerts.model.views.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

@PuiGenerated
public interface IVPuiAlert extends IViewDto {
	@PuiGenerated
	String ID_COLUMN = "id";
	@PuiGenerated
	String ID_FIELD = "id";
	@PuiGenerated
	String DESCRIPTION_COLUMN = "description";
	@PuiGenerated
	String DESCRIPTION_FIELD = "description";
	@PuiGenerated
	String TYPE_COLUMN = "type";
	@PuiGenerated
	String TYPE_FIELD = "type";
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String COLUMN_NAME_COLUMN = "column_name";
	@PuiGenerated
	String COLUMN_NAME_FIELD = "columnname";
	@PuiGenerated
	String PK_COLUMN = "pk";
	@PuiGenerated
	String PK_FIELD = "pk";
	@PuiGenerated
	String PROCESSED_COLUMN = "processed";
	@PuiGenerated
	String PROCESSED_FIELD = "processed";
	@PuiGenerated
	String READ_COLUMN = "read";
	@PuiGenerated
	String READ_FIELD = "read";
	@PuiGenerated
	String CONTENT_COLUMN = "content";
	@PuiGenerated
	String CONTENT_FIELD = "content";
	@PuiGenerated
	String EMAILS_COLUMN = "emails";
	@PuiGenerated
	String EMAILS_FIELD = "emails";
	@PuiGenerated
	String LAUNCHING_DATETIME_COLUMN = "launching_datetime";
	@PuiGenerated
	String LAUNCHING_DATETIME_FIELD = "launchingdatetime";

	@PuiGenerated
	Integer getId();

	@PuiGenerated
	void setId(Integer id);

	@PuiGenerated
	String getDescription();

	@PuiGenerated
	void setDescription(String description);

	@PuiGenerated
	String getType();

	@PuiGenerated
	void setType(String type);

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getColumnname();

	@PuiGenerated
	void setColumnname(String columnname);

	@PuiGenerated
	String getPk();

	@PuiGenerated
	void setPk(String pk);

	@PuiGenerated
	Integer getProcessed();

	@PuiGenerated
	void setProcessed(Integer processed);

	@PuiGenerated
	Integer getRead();

	@PuiGenerated
	void setRead(Integer read);

	@PuiGenerated
	String getContent();

	@PuiGenerated
	void setContent(String content);

	@PuiGenerated
	String getEmails();

	@PuiGenerated
	void setEmails(String emails);

	@PuiGenerated
	java.time.Instant getLaunchingdatetime();

	@PuiGenerated
	void setLaunchingdatetime(java.time.Instant launchingdatetime);
}
