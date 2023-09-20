CREATE TABLE pui_language (
	isocode VARCHAR2(2),
	name VARCHAR2(100) NOT NULL,
	isdefault INTEGER DEFAULT 0 NOT NULL,
	enabled INTEGER DEFAULT 1 NOT NULL,
	CONSTRAINT pk_pui_language PRIMARY KEY (isocode),
	CONSTRAINT ck_lang_def CHECK (isdefault in (0, 1))
);

CREATE TABLE pui_user (
	usr VARCHAR2(100),
	name VARCHAR2(200) NOT NULL,
	password VARCHAR2(100),
	language VARCHAR2(2),
	email VARCHAR2(100),
	disabled INTEGER DEFAULT 0 NOT NULL,
	disabled_date TIMESTAMP WITH TIME ZONE,
	dateformat VARCHAR2(10) DEFAULT 'dd/MM/yyyy' NOT NULL,
	reset_password_token VARCHAR2(100),
	last_access_time TIMESTAMP WITH TIME ZONE,
	last_access_ip VARCHAR2(50),
	last_password_change TIMESTAMP WITH TIME ZONE,
	login_wrong_attempts INTEGER DEFAULT 0 NOT NULL,
	change_password_next_login INTEGER DEFAULT 0 NOT NULL,
	secret_2fa VARCHAR2(50),
	reset_password_token_date TIMESTAMP WITH TIME ZONE,
	CONSTRAINT pk_pui_user PRIMARY KEY (usr),
	CONSTRAINT ck_lang_dis CHECK (disabled in (0, 1)),
	CONSTRAINT fk_user_language FOREIGN KEY (language) REFERENCES pui_language(isocode) ON DELETE SET NULL,
	CONSTRAINT ck_dateformat CHECK (dateformat in ('yyyy/MM/dd', 'yyyy-MM-dd', 'dd/MM/yyyy', 'dd-MM-yyyy'))
);

CREATE TABLE pui_session (
	uuid VARCHAR2(100) NOT NULL,
	usr VARCHAR2(100) NOT NULL,
	created TIMESTAMP WITH TIME ZONE NOT NULL,
	expiration TIMESTAMP WITH TIME ZONE NOT NULL,
	lastuse TIMESTAMP WITH TIME ZONE NOT NULL,
	persistent INTEGER DEFAULT 0 NOT NULL,
	jwt CLOB NOT NULL,
	CONSTRAINT pk_pui_session PRIMARY KEY (uuid)
);

CREATE TABLE pui_subsystem (
	subsystem VARCHAR2(3),
	CONSTRAINT pk_pui_subsystem PRIMARY KEY (subsystem)
);

CREATE TABLE pui_subsystem_tra (
	subsystem VARCHAR2(3),
	lang VARCHAR2(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name VARCHAR2(200) NOT NULL,
	CONSTRAINT pk_pui_subsystem_tra PRIMARY KEY (subsystem, lang),
	CONSTRAINT fk_subsystem_tra_subsystem FOREIGN KEY (subsystem) REFERENCES pui_subsystem(subsystem) ON DELETE CASCADE,
	CONSTRAINT fk_subsystem_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_subsystem_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_functionality (
	functionality VARCHAR2(100),
	subsystem VARCHAR2(3) NOT NULL,
	CONSTRAINT pk_pui_functionality PRIMARY KEY (functionality),
	CONSTRAINT fk_func_subsystem FOREIGN KEY (subsystem) REFERENCES pui_subsystem(subsystem) ON DELETE CASCADE
);

CREATE TABLE pui_functionality_tra (
	functionality VARCHAR2(100),
	lang VARCHAR2(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name VARCHAR2(200) NOT NULL,
	CONSTRAINT pk_pui_functionality_tra PRIMARY KEY (functionality, lang),
	CONSTRAINT fk_func_tra_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality) ON DELETE CASCADE,
	CONSTRAINT fk_func_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_func_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_profile (
	profile VARCHAR2(100),
	CONSTRAINT pk_pui_profile PRIMARY KEY (profile)
);

CREATE TABLE pui_profile_tra (
	profile VARCHAR2(100),
	lang VARCHAR2(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name VARCHAR2(200) NOT NULL,
	CONSTRAINT pk_pui_profile_tra PRIMARY KEY (profile, lang),
	CONSTRAINT fk_profile_tra_profile FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE,
	CONSTRAINT fk_profile_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_profile_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_profile_functionality (
	profile VARCHAR2(100),
	functionality VARCHAR2(100),
	CONSTRAINT pk_pui_profile_functionality PRIMARY KEY (profile, functionality),
	CONSTRAINT fk_prof_func_prof FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE,
	CONSTRAINT fk_prof_func_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality) ON DELETE CASCADE
);

CREATE TABLE pui_user_profile (
	USR VARCHAR2(100),
	profile VARCHAR2(100),
	CONSTRAINT pk_pui_user_profile PRIMARY KEY (USR, profile),
	CONSTRAINT fk_user_profile_usr FOREIGN KEY (USR) REFERENCES pui_user(USR) ON DELETE CASCADE,
	CONSTRAINT fk_user_profile_profile FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE
);

CREATE TABLE pui_model (
	model VARCHAR2(100),
	entity VARCHAR2(100),
	configuration CLOB,
	filter CLOB,
	CONSTRAINT pk_pui_grid PRIMARY KEY (model)
);

CREATE TABLE pui_user_model_filter (
	id INTEGER,
	usr VARCHAR2(100) NOT NULL,
	model VARCHAR2(100) NOT NULL,
	label VARCHAR2(200) NOT NULL,
	filter CLOB NOT NULL,
	isdefault INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_user_filter PRIMARY KEY (id),
	CONSTRAINT fk_user_model_filter_usr FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE,
	CONSTRAINT fk_user_model_filter_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_model_filter (
	id INTEGER,
	model VARCHAR2(100) NOT NULL,
	label VARCHAR2(200) NOT NULL,
	description VARCHAR2(300),
	filter CLOB NOT NULL,
	isdefault INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_model_filter PRIMARY KEY (id),
	CONSTRAINT ck_model_filter_def CHECK (isdefault in (0, 1)),
	CONSTRAINT fk_model_filter_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_user_model_config (
	id INTEGER,
	usr VARCHAR2(100) NOT NULL,
	model VARCHAR2(100) NOT NULL,
	configuration CLOB NOT NULL,
	type VARCHAR2(50) NOT NULL,
	CONSTRAINT pk_pui_user_grid_config PRIMARY KEY (id),
	CONSTRAINT fk_user_config_usr FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE,
	CONSTRAINT fk_user_config_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_menu (
	node INTEGER,
	parent INTEGER,
	model VARCHAR2(100),
	component VARCHAR2(100),
	functionality VARCHAR2(100),
	label VARCHAR2(100) NOT NULL,
	icon_label VARCHAR2(100),
	CONSTRAINT pk_pui_menu PRIMARY KEY (node),
	CONSTRAINT fk_menu_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_menu_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality),
	CONSTRAINT fk_menu_parent FOREIGN KEY (parent) REFERENCES pui_menu(node)
);

CREATE TABLE pui_variable (
	variable VARCHAR2(50),
	value CLOB NOT NULL,
	description VARCHAR2(500) NOT NULL,
	CONSTRAINT pk_pui_variable PRIMARY KEY (variable)
);

CREATE TABLE pui_elasticsearch_views (
    appname VARCHAR2(100) DEFAULT 'DEFAULT' NOT NULL,
    viewname VARCHAR2(100) NOT NULL,
    identity_fields VARCHAR2(100) NOT NULL,
    CONSTRAINT pk_elasticsearch_views PRIMARY KEY (appname, viewname)
);

CREATE TABLE pui_audit (
	id INTEGER,
	model VARCHAR2(100) NOT NULL,
	type VARCHAR2(50) NOT NULL,
	pk VARCHAR2(100),
	datetime TIMESTAMP WITH TIME ZONE NOT NULL,
	usr VARCHAR2(100) NOT NULL,
	ip VARCHAR2(100) DEFAULT '0.0.0.0' NOT NULL,
	content CLOB,
	client VARCHAR2(100),
	CONSTRAINT pk_pui_audit PRIMARY KEY (id)
);

CREATE TABLE pui_importexport (
	id integer,
	model VARCHAR2(100) NOT NULL,
	usr VARCHAR2(100) NOT NULL,
	datetime TIMESTAMP WITH TIME ZONE NOT NULL,
	filename_csv VARCHAR2(100) NOT NULL,
	filename_json VARCHAR2(100) NOT NULL,
	executed INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_impexp PRIMARY KEY (id),
	CONSTRAINT fk_pui_impexp_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_pui_impexp_usr FOREIGN KEY (usr) REFERENCES pui_user(usr),
	CONSTRAINT ck_executed CHECK (executed in (0, 1))
);

CREATE TABLE pui_multi_instance_process (
    id varchar2(100) NOT NULL,
    period integer not null,
    time_unit varchar2(15) not null,
    instance_assignee_uuid varchar2(100),
    latest_execution timestamp WITH TIME ZONE,
    latest_heartbeat timestamp WITH TIME ZONE,
    CONSTRAINT pk_pui_parallel_process PRIMARY KEY (id)
);

CREATE TABLE pui_api_key (
	api_key varchar2(100) NOT NULL,
	name varchar(100) NOT NULL,
	description varchar2(100) NOT NULL,
	profile varchar2(100) NULL,
	CONSTRAINT pk_api_key PRIMARY KEY (api_key),
	CONSTRAINT unique_name UNIQUE (name),
	CONSTRAINT fk_api_key_profile FOREIGN KEY (profile) REFERENCES pui_profile (profile)
);

-- SEQUENCES FOR AUTOINCREMENTABLE IDs
CREATE SEQUENCE pui_audit_sequence;
CREATE SEQUENCE pui_importexport_sequence;
CREATE SEQUENCE pui_model_filter_sequence;
CREATE SEQUENCE pui_usermodel_config_sequence;
CREATE SEQUENCE pui_usermodel_filter_sequence;

-- TRIGGERS FOR AUTOINCREMENTABLE IDs
CREATE TRIGGER pui_audit_on_insert
  BEFORE INSERT ON pui_audit
  FOR EACH ROW
BEGIN
  SELECT pui_audit_sequence.nextval
  INTO :new.id
  FROM dual;
END;

CREATE TRIGGER pui_importexport_on_insert
  BEFORE INSERT ON pui_importexport
  FOR EACH ROW
BEGIN
  SELECT pui_importexport_sequence.nextval
  INTO :new.id
  FROM dual;
END;

CREATE TRIGGER pui_model_filter_on_insert
  BEFORE INSERT ON pui_model_filter
  FOR EACH ROW
BEGIN
  SELECT pui_model_filter_sequence.nextval
  INTO :new.id
  FROM dual;
END;

CREATE TRIGGER pui_usermodel_config_on_insert
  BEFORE INSERT ON pui_user_model_config
  FOR EACH ROW
BEGIN
  SELECT pui_usermodel_config_sequence.nextval
  INTO :new.id
  FROM dual;
END;

CREATE TRIGGER pui_usermodel_filter_on_insert
  BEFORE INSERT ON pui_user_model_filter
  FOR EACH ROW
BEGIN
  SELECT pui_usermodel_filter_sequence.nextval
  INTO :new.id
  FROM dual;
END;

