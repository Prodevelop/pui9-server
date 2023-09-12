CREATE TABLE pui_alert_configuration (
	id SERIAL, 
	description CHARACTER VARYING(100) NOT NULL,
	model CHARACTER VARYING(100),
	column_name CHARACTER VARYING(100),
	type CHARACTER VARYING(10) DEFAULT 'MODEL' NOT NULL,
	time_value INTEGER,
	time_unit CHARACTER VARYING(10),
	time_before_after CHARACTER VARYING(10),
	content TEXT NOT NULL,
	emails TEXT,
	usr CHARACTER VARYING(100) NOT NULL,
	datetime TIMESTAMPTZ(3) NOT NULL,
	is_content_html integer NOT NULL DEFAULT 0,
	CONSTRAINT pk_pui_alert_configuration PRIMARY KEY (id),
	CONSTRAINT fk_alert_model FOREIGN KEY (model) REFERENCES pui_model (model) ON DELETE CASCADE
);

CREATE TABLE pui_alert (
	id SERIAL,
	alert_config_id INTEGER NOT NULL,
	pk CHARACTER VARYING(100),
	processed INTEGER DEFAULT 0 NOT NULL,
	launching_datetime TIMESTAMPTZ(3) NOT NULL,
	read integer default 0 not null,
	CONSTRAINT pk_pui_alert PRIMARY KEY (id),
	CONSTRAINT fk_alert_config_alert FOREIGN KEY (alert_config_id) REFERENCES pui_alert_configuration (id) ON DELETE CASCADE
);
