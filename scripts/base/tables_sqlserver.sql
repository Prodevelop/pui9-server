CREATE TABLE pui_language (
	isocode VARCHAR(2),
	name VARCHAR(100) NOT NULL,
	isdefault INTEGER DEFAULT 0 NOT NULL,
	enabled INTEGER DEFAULT 1 NOT NULL,
	CONSTRAINT pk_pui_language PRIMARY KEY (isocode),
	CONSTRAINT ck_lang_def CHECK (isdefault in (0, 1))
);

CREATE TABLE pui_user (
	usr VARCHAR(100),
	name VARCHAR(200) NOT NULL,
	password VARCHAR(100),
	language VARCHAR(2),
	email VARCHAR(100),
	disabled INTEGER DEFAULT 0 NOT NULL,
	disabled_date DATETIME,
	dateformat VARCHAR(10) DEFAULT 'dd/MM/yyyy' NOT NULL,
	reset_password_token VARCHAR(100),
	last_access_time DATETIME,
	last_access_ip VARCHAR(50),
	last_password_change DATETIME,
	login_wrong_attempts INTEGER DEFAULT 0 NOT NULL,
	change_password_next_login INTEGER DEFAULT 0 NOT NULL,
	secret_2fa VARCHAR(50),
	reset_password_token_date DATETIME,
	CONSTRAINT pk_pui_user PRIMARY KEY (usr),
	CONSTRAINT ck_lang_dis CHECK (disabled in (0, 1)),
	CONSTRAINT fk_user_language FOREIGN KEY (language) REFERENCES pui_language(isocode) ON DELETE SET NULL,
	CONSTRAINT ck_dateformat CHECK (dateformat in ('yyyy/MM/dd', 'yyyy-MM-dd', 'dd/MM/yyyy', 'dd-MM-yyyy'))
);

CREATE TABLE pui_session (
	uuid VARCHAR(100) NOT NULL,
	usr VARCHAR(100) NOT NULL,
	created DATETIME NOT NULL,
	expiration DATETIME NOT NULL,
	lastuse DATETIME NOT NULL,
	persistent INTEGER DEFAULT 0 NOT NULL,
	jwt VARCHAR(MAX) NOT NULL,
	CONSTRAINT pk_pui_session PRIMARY KEY (uuid)
);

CREATE TABLE pui_subsystem (
	subsystem VARCHAR(3),
	CONSTRAINT pk_pui_subsystem PRIMARY KEY (subsystem)
);

CREATE TABLE pui_subsystem_tra (
	subsystem VARCHAR(3),
	lang VARCHAR(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name VARCHAR(200) NOT NULL,
	CONSTRAINT pk_pui_subsystem_tra PRIMARY KEY (subsystem, lang),
	CONSTRAINT fk_subsystem_tra_subsystem FOREIGN KEY (subsystem) REFERENCES pui_subsystem(subsystem) ON DELETE CASCADE,
	CONSTRAINT fk_subsystem_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_subsystem_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_functionality (
	functionality VARCHAR(100),
	subsystem VARCHAR(3) NOT NULL,
	CONSTRAINT pk_pui_functionality PRIMARY KEY (functionality),
	CONSTRAINT fk_func_subsystem FOREIGN KEY (subsystem) REFERENCES pui_subsystem(subsystem) ON DELETE CASCADE
);

CREATE TABLE pui_functionality_tra (
	functionality VARCHAR(100),
	lang VARCHAR(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name VARCHAR(200) NOT NULL,
	CONSTRAINT pk_pui_functionality_tra PRIMARY KEY (functionality, lang),
	CONSTRAINT fk_func_tra_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality) ON DELETE CASCADE,
	CONSTRAINT fk_func_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_func_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_profile (
	profile VARCHAR(100),
	CONSTRAINT pk_pui_profile PRIMARY KEY (profile)
);

CREATE TABLE pui_profile_tra (
	profile VARCHAR(100),
	lang VARCHAR(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name VARCHAR(200) NOT NULL,
	CONSTRAINT pk_pui_profile_tra PRIMARY KEY (profile, lang),
	CONSTRAINT fk_profile_tra_profile FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE,
	CONSTRAINT fk_profile_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_profile_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_profile_functionality (
	profile VARCHAR(100),
	functionality VARCHAR(100),
	CONSTRAINT pk_pui_profile_functionality PRIMARY KEY (profile, functionality),
	CONSTRAINT fk_prof_func_prof FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE,
	CONSTRAINT fk_prof_func_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality) ON DELETE CASCADE
);

CREATE TABLE pui_user_profile (
	USR VARCHAR(100),
	profile VARCHAR(100),
	CONSTRAINT pk_pui_user_profile PRIMARY KEY (USR, profile),
	CONSTRAINT fk_user_profile_usr FOREIGN KEY (USR) REFERENCES pui_user(USR) ON DELETE CASCADE,
	CONSTRAINT fk_user_profile_profile FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE
);

CREATE TABLE pui_model (
	model VARCHAR(100),
	entity VARCHAR(100),
	configuration VARCHAR(MAX),
	filter VARCHAR(MAX),
	CONSTRAINT pk_pui_grid PRIMARY KEY (model)
);

CREATE TABLE pui_user_model_filter (
	id INTEGER IDENTITY(1, 1),
	usr VARCHAR(100) NOT NULL,
	model VARCHAR(100) NOT NULL,
	label VARCHAR(200) NOT NULL,
	filter VARCHAR(MAX) NOT NULL,
	isdefault integer DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_user_filter PRIMARY KEY (id),
	CONSTRAINT fk_user_model_filter_usr FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE,
	CONSTRAINT fk_user_model_filter_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_model_filter (
	id INTEGER IDENTITY(1, 1),
	model VARCHAR(100) NOT NULL,
	label VARCHAR(200) NOT NULL,
	description VARCHAR(300),
	filter VARCHAR(MAX) NOT NULL,
	isdefault INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_model_filter PRIMARY KEY (id),
	CONSTRAINT ck_model_filter_def CHECK (isdefault in (0, 1)),
	CONSTRAINT fk_model_filter_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_user_model_config (
	id INTEGER IDENTITY(1, 1),
	usr VARCHAR(100) NOT NULL,
	model VARCHAR(100) NOT NULL,
	configuration VARCHAR(MAX) NOT NULL,
	type VARCHAR(50) NOT NULL,
	CONSTRAINT pk_pui_user_grid_config PRIMARY KEY (id),
	CONSTRAINT fk_user_config_usr FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE,
	CONSTRAINT fk_user_config_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_menu (
	node INTEGER,
	parent INTEGER,
	model VARCHAR(100),
	component VARCHAR(100),
	functionality VARCHAR(100),
	label VARCHAR(100) NOT NULL,
	icon_label VARCHAR(100),
	CONSTRAINT pk_pui_menu PRIMARY KEY (node),
	CONSTRAINT fk_menu_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_menu_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality),
	CONSTRAINT fk_menu_parent FOREIGN KEY (parent) REFERENCES pui_menu(node)
);

CREATE TABLE pui_variable (
	variable VARCHAR(50),
	value VARCHAR(MAX) NOT NULL,
	description VARCHAR(500) NOT NULL,
	CONSTRAINT pk_pui_variable PRIMARY KEY (variable)
);

CREATE TABLE pui_elasticsearch_views (
    appname VARCHAR(100) NOT NULL DEFAULT 'DEFAULT',
    viewname VARCHAR(100) NOT NULL,
    identity_fields VARCHAR(100) NOT NULL,
    CONSTRAINT pk_elasticsearch_views PRIMARY KEY (appname, viewname)
);

CREATE TABLE pui_audit (
	id INTEGER IDENTITY(1, 1),
	model VARCHAR(100) NOT NULL,
	type VARCHAR(50) NOT NULL,
	pk VARCHAR(100),
	datetime DATETIME NOT NULL,
	usr VARCHAR(100) NOT NULL,
	ip VARCHAR(100) NOT NULL DEFAULT '0.0.0.0',
	content VARCHAR(MAX),
	client VARCHAR(100),
	CONSTRAINT pk_pui_audit PRIMARY KEY (id)
);

CREATE TABLE pui_importexport (
	id INTEGER IDENTITY(1, 1),
	model VARCHAR(100) NOT NULL,
	usr VARCHAR(100) NOT NULL,
	datetime DATETIME NOT NULL,
	filename_csv VARCHAR(100) NOT NULL,
	filename_json VARCHAR(100) NOT NULL,
	executed INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_impexp PRIMARY KEY (id),
	CONSTRAINT fk_pui_impexp_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_pui_impexp_usr FOREIGN KEY (usr) REFERENCES pui_user(usr),
	CONSTRAINT ck_executed CHECK (executed in (0, 1))
);

CREATE TABLE pui_multi_instance_process (
    id varchar(100) NOT NULL,
    period integer not null,
    time_unit varchar(15) not null,
    instance_assignee_uuid varchar(100),
    latest_execution datetime,
    latest_heartbeat datetime,
    CONSTRAINT pk_pui_parallel_process PRIMARY KEY (id)
);

CREATE TABLE pui_api_key (
	api_key varchar(100) NOT NULL,
	name varchar(100) NOT NULL,
	description varchar(100) NOT NULL,
	profile varchar(100) NULL,
	CONSTRAINT pk_api_key PRIMARY KEY (api_key),
	CONSTRAINT unique_name UNIQUE (name),
	CONSTRAINT fk_api_key_profile FOREIGN KEY (profile) REFERENCES pui_profile (profile)
);
