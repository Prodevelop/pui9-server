CREATE TABLE pui_alert_configuration (
	id INTEGER IDENTITY(1,1), 
	description VARCHAR(100) NOT NULL,
	model VARCHAR(100),
	column_name VARCHAR(100),
	type VARCHAR(10) DEFAULT 'MODEL' NOT NULL,
	time_value INTEGER,
	time_unit VARCHAR(10),
	time_before_after VARCHAR(10),
	content VARCHAR(MAX) NOT NULL,
	emails VARCHAR(MAX),
	usr VARCHAR(100) NOT NULL,
	datetime DATETIME NOT NULL,
	is_content_html integer NOT NULL DEFAULT 0,
	CONSTRAINT pk_pui_alert_configuration PRIMARY KEY (id),
	CONSTRAINT fk_alert_model FOREIGN KEY (model) REFERENCES pui_model (model) ON DELETE CASCADE
);

CREATE TABLE pui_alert (
	id INTEGER IDENTITY(1,1),
	alert_config_id INTEGER NOT NULL,
	pk VARCHAR(100),
	processed INTEGER DEFAULT 0 NOT NULL,
	launching_datetime DATETIME NOT NULL,
	read integer default 0 not null,
	CONSTRAINT pk_pui_alert PRIMARY KEY (id),
	CONSTRAINT fk_alert_config_alert FOREIGN KEY (alert_config_id) REFERENCES pui_alert_configuration (id) ON DELETE CASCADE
);
