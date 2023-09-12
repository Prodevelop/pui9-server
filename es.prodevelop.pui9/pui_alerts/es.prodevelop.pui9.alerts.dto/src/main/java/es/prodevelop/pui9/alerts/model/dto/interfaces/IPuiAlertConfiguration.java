package es.prodevelop.pui9.alerts.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiAlertConfiguration extends IPuiAlertConfigurationPk {
	@PuiGenerated
	String DESCRIPTION_COLUMN = "description";
	@PuiGenerated
	String DESCRIPTION_FIELD = "description";
	@PuiGenerated
	String MODEL_COLUMN = "model";
	@PuiGenerated
	String MODEL_FIELD = "model";
	@PuiGenerated
	String COLUMN_NAME_COLUMN = "column_name";
	@PuiGenerated
	String COLUMN_NAME_FIELD = "columnname";
	@PuiGenerated
	String TYPE_COLUMN = "type";
	@PuiGenerated
	String TYPE_FIELD = "type";
	@PuiGenerated
	String TIME_UNIT_COLUMN = "time_unit";
	@PuiGenerated
	String TIME_UNIT_FIELD = "timeunit";
	@PuiGenerated
	String TIME_VALUE_COLUMN = "time_value";
	@PuiGenerated
	String TIME_VALUE_FIELD = "timevalue";
	@PuiGenerated
	String TIME_BEFORE_AFTER_COLUMN = "time_before_after";
	@PuiGenerated
	String TIME_BEFORE_AFTER_FIELD = "timebeforeafter";
	@PuiGenerated
	String CONTENT_COLUMN = "content";
	@PuiGenerated
	String CONTENT_FIELD = "content";
	@PuiGenerated
	String EMAILS_COLUMN = "emails";
	@PuiGenerated
	String EMAILS_FIELD = "emails";
	@PuiGenerated
	String USR_COLUMN = "usr";
	@PuiGenerated
	String USR_FIELD = "usr";
	@PuiGenerated
	String DATETIME_COLUMN = "datetime";
	@PuiGenerated
	String DATETIME_FIELD = "datetime";
	@PuiGenerated
	String IS_CONTENT_HTML_COLUMN = "is_content_html";
	@PuiGenerated
	String IS_CONTENT_HTML_FIELD = "iscontenthtml";

	@PuiGenerated
	String getDescription();

	@PuiGenerated
	void setDescription(String description);

	@PuiGenerated
	String getModel();

	@PuiGenerated
	void setModel(String model);

	@PuiGenerated
	String getColumnname();

	@PuiGenerated
	void setColumnname(String columnname);

	@PuiGenerated
	String getType();

	@PuiGenerated
	void setType(String type);

	@PuiGenerated
	String getTimeunit();

	@PuiGenerated
	void setTimeunit(String timeunit);

	@PuiGenerated
	Integer getTimevalue();

	@PuiGenerated
	void setTimevalue(Integer timevalue);

	@PuiGenerated
	String getTimebeforeafter();

	@PuiGenerated
	void setTimebeforeafter(String timebeforeafter);

	@PuiGenerated
	String getContent();

	@PuiGenerated
	void setContent(String content);

	@PuiGenerated
	String getEmails();

	@PuiGenerated
	void setEmails(String emails);

	@PuiGenerated
	String getUsr();

	@PuiGenerated
	void setUsr(String usr);

	@PuiGenerated
	java.time.Instant getDatetime();

	@PuiGenerated
	void setDatetime(java.time.Instant datetime);

	@PuiGenerated
	Integer getIscontenthtml();

	@PuiGenerated
	void setIscontenthtml(Integer iscontenthtml);

	java.time.Instant getLaunchingdatetime();

	void setLaunchingdatetime(java.time.Instant launchingdatetime);
}
