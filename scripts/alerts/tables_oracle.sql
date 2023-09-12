CREATE TABLE pui_alert_configuration (
	id INTEGER NOT NULL, 
	description VARCHAR2(100) NOT NULL,
	model VARCHAR2(100),
	column_name VARCHAR2(100),
	type VARCHAR2(10) DEFAULT 'MODEL' NOT NULL,
	time_value INTEGER,
	time_unit VARCHAR2(10),
	time_before_after VARCHAR2(10),
	content CLOB NOT NULL,
	emails CLOB,
	usr VARCHAR2(100) NOT NULL,
	datetime TIMESTAMP WITH TIME ZONE NOT NULL,
	is_content_html integer DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_alert_configuration PRIMARY KEY (id),
	CONSTRAINT fk_alert_model FOREIGN KEY (model) REFERENCES pui_model (model) ON DELETE CASCADE
);

CREATE TABLE pui_alert (
	id INTEGER NOT NULL,
	alert_config_id INTEGER NOT NULL,
	pk VARCHAR2(100),
	processed INTEGER DEFAULT 0 NOT NULL,
	launching_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
	read integer default 0 not null,
	CONSTRAINT pk_pui_alert PRIMARY KEY (id),
	CONSTRAINT fk_alert_config_alert FOREIGN KEY (alert_config_id) REFERENCES pui_alert_configuration (id) ON DELETE CASCADE
);

-- SEQUENCES FOR AUTOINCREMENTABLE IDs
CREATE SEQUENCE pui_alert_config_sequence;
CREATE SEQUENCE pui_alert_sequence;

-- TRIGGERS FOR AUTOINCREMENTABLE IDs
CREATE TRIGGER PUI_ALERT_CONFIG_ON_INSERT
BEFORE INSERT ON pui_alert_configuration
FOR EACH ROW
BEGIN
	SELECT pui_alert_config_sequence.nextval
	INTO :new.id
	FROM dual;
END;

CREATE TRIGGER PUI_ALERT_ON_INSERT
BEFORE INSERT ON pui_alert
FOR EACH ROW
BEGIN
	SELECT pui_alert_sequence.nextval
	INTO :new.id
	FROM dual;
END;
