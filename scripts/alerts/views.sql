CREATE VIEW v_pui_alert_configuration AS 
SELECT
	ID,
	DESCRIPTION,
	TYPE,
	MODEL,
	COLUMN_NAME,
	TIME_VALUE,
	TIME_UNIT,
	TIME_BEFORE_AFTER,
	CONTENT,
	EMAILS,
	USR,
	DATETIME
FROM pui_alert_configuration;

CREATE VIEW v_pui_alert AS 
SELECT
	pa.ID,
	pac.DESCRIPTION,
	pac.TYPE,
	pac.MODEL,
	pac.COLUMN_NAME,
	pa.PK,
	pa.LAUNCHING_DATETIME,
	pa.PROCESSED,
	pa.READ,
	pac.CONTENT,
	pac.EMAILS
FROM pui_alert pa
LEFT JOIN pui_alert_configuration pac ON
	pac.ID = pa.ALERT_CONFIG_ID;
