package es.prodevelop.pui9.alerts.model.dto.interfaces;

import es.prodevelop.pui9.annotations.PuiGenerated;

@PuiGenerated
public interface IPuiAlert extends IPuiAlertPk {
	@PuiGenerated
	String ALERT_CONFIG_ID_COLUMN = "alert_config_id";
	@PuiGenerated
	String ALERT_CONFIG_ID_FIELD = "alertconfigid";
	@PuiGenerated
	String PK_COLUMN = "pk";
	@PuiGenerated
	String PK_FIELD = "pk";
	@PuiGenerated
	String PROCESSED_COLUMN = "processed";
	@PuiGenerated
	String PROCESSED_FIELD = "processed";
	@PuiGenerated
	String LAUNCHING_DATETIME_COLUMN = "launching_datetime";
	@PuiGenerated
	String LAUNCHING_DATETIME_FIELD = "launchingdatetime";
	@PuiGenerated
	String READ_COLUMN = "read";
	@PuiGenerated
	String READ_FIELD = "read";

	@PuiGenerated
	Integer getAlertconfigid();

	@PuiGenerated
	void setAlertconfigid(Integer alertconfigid);

	@PuiGenerated
	String getPk();

	@PuiGenerated
	void setPk(String pk);

	@PuiGenerated
	Integer getProcessed();

	@PuiGenerated
	void setProcessed(Integer processed);

	@PuiGenerated
	java.time.Instant getLaunchingdatetime();

	@PuiGenerated
	void setLaunchingdatetime(java.time.Instant launchingdatetime);

	@PuiGenerated
	Integer getRead();

	@PuiGenerated
	void setRead(Integer read);
}
