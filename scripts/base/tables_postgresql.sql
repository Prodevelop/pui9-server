CREATE EXTENSION IF NOT EXISTS UNACCENT;

CREATE TABLE pui_language (
	isocode CHARACTER VARYING(2),
	name CHARACTER VARYING(100) NOT NULL,
	isdefault INTEGER DEFAULT 0 NOT NULL,
	enabled INTEGER DEFAULT 1 NOT NULL,
	CONSTRAINT pk_pui_language PRIMARY KEY (isocode),
	CONSTRAINT ck_lang_def CHECK (isdefault in (0, 1))
);

CREATE TABLE pui_user (
	usr CHARACTER VARYING(100),
	name CHARACTER VARYING(200) NOT NULL,
	password CHARACTER VARYING(100),
	language CHARACTER VARYING(2),
	email CHARACTER VARYING(100),
	disabled INTEGER DEFAULT 0 NOT NULL,
	disabled_date timestamptz(3),
	dateformat CHARACTER VARYING(10) DEFAULT 'dd/MM/yyyy' NOT NULL,
	reset_password_token CHARACTER VARYING(100),
	last_access_time timestamptz(3),
	last_access_ip CHARACTER VARYING(50),
	last_password_change timestamptz(3),
	login_wrong_attempts INTEGER DEFAULT 0 NOT NULL,
	change_password_next_login INTEGER DEFAULT 0 NOT NULL,
	secret_2fa CHARACTER VARYING(50),
	reset_password_token_date timestamptz(3),
	CONSTRAINT pk_pui_user PRIMARY KEY (usr),
	CONSTRAINT ck_lang_dis CHECK (disabled in (0, 1)),
	CONSTRAINT fk_user_language FOREIGN KEY (language) REFERENCES pui_language(isocode) ON DELETE SET NULL,
	CONSTRAINT ck_dateformat CHECK (dateformat in ('yyyy/MM/dd', 'yyyy-MM-dd', 'dd/MM/yyyy', 'dd-MM-yyyy'))
);

CREATE TABLE pui_session (
	uuid CHARACTER VARYING(100) NOT NULL,
	usr CHARACTER VARYING(100) NOT NULL,
	created timestamptz(3) NOT NULL,
	expiration timestamptz(3) NOT NULL,
	lastuse timestamptz(3) NOT NULL,
	persistent INTEGER DEFAULT 0 NOT NULL,
	jwt TEXT NOT NULL,
	CONSTRAINT pk_pui_session PRIMARY KEY (uuid)
);

CREATE TABLE pui_subsystem (
	subsystem CHARACTER VARYING(3),
	CONSTRAINT pk_pui_subsystem PRIMARY KEY (subsystem)
);

CREATE TABLE pui_subsystem_tra (
	subsystem CHARACTER VARYING(3),
	lang CHARACTER VARYING(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name CHARACTER VARYING(200) NOT NULL,
	CONSTRAINT pk_pui_subsystem_tra PRIMARY KEY (subsystem, lang),
	CONSTRAINT fk_subsystem_tra_subsystem FOREIGN KEY (subsystem) REFERENCES pui_subsystem(subsystem) ON DELETE CASCADE,
	CONSTRAINT fk_subsystem_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_subsystem_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_functionality (
	functionality CHARACTER VARYING(100),
	subsystem CHARACTER VARYING(3) NOT NULL,
	CONSTRAINT pk_pui_functionality PRIMARY KEY (functionality),
	CONSTRAINT fk_func_subsystem FOREIGN KEY (subsystem) REFERENCES pui_subsystem(subsystem) ON DELETE CASCADE
);

CREATE TABLE pui_functionality_tra (
	functionality CHARACTER VARYING(100),
	lang CHARACTER VARYING(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name CHARACTER VARYING(200) NOT NULL,
	CONSTRAINT pk_pui_functionality_tra PRIMARY KEY (functionality, lang),
	CONSTRAINT fk_func_tra_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality) ON DELETE CASCADE,
	CONSTRAINT fk_func_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_func_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_profile (
	profile CHARACTER VARYING(100),
	CONSTRAINT pk_pui_profile PRIMARY KEY (profile)
);

CREATE TABLE pui_profile_tra (
	profile CHARACTER VARYING(100),
	lang CHARACTER VARYING(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	name CHARACTER VARYING(200) NOT NULL,
	CONSTRAINT pk_pui_profile_tra PRIMARY KEY (profile, lang),
	CONSTRAINT fk_profile_tra_profile FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE,
	CONSTRAINT fk_profile_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_profile_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_profile_functionality (
	profile CHARACTER VARYING(100),
	functionality CHARACTER VARYING(100),
	CONSTRAINT pk_pui_profile_functionality PRIMARY KEY (profile, functionality),
	CONSTRAINT fk_prof_func_prof FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE,
	CONSTRAINT fk_prof_func_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality) ON DELETE CASCADE
);

CREATE TABLE pui_user_profile (
	USR CHARACTER VARYING(100),
	profile CHARACTER VARYING(100),
	CONSTRAINT pk_pui_user_profile PRIMARY KEY (USR, profile),
	CONSTRAINT fk_user_profile_user FOREIGN KEY (USR) REFERENCES pui_user(USR) ON DELETE CASCADE,
	CONSTRAINT fk_user_profile_profile FOREIGN KEY (profile) REFERENCES pui_profile(profile) ON DELETE CASCADE
);

CREATE TABLE pui_model (
	model CHARACTER VARYING(100),
	entity CHARACTER VARYING(100),
	configuration TEXT,
	filter TEXT,
	CONSTRAINT pk_pui_grid PRIMARY KEY (model)
);

CREATE TABLE pui_user_model_filter (
	id SERIAL,
	usr CHARACTER VARYING(100) NOT NULL,
	model CHARACTER VARYING(100) NOT NULL,
	label CHARACTER VARYING(200) NOT NULL,
	filter TEXT NOT NULL,
	CONSTRAINT pk_pui_user_filter PRIMARY KEY (id),
	CONSTRAINT fk_user_model_filter_usr FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE,
	CONSTRAINT fk_user_model_filter_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_model_filter (
	id SERIAL,
	model CHARACTER VARYING(100) NOT NULL,
	label CHARACTER VARYING(200) NOT NULL,
	description CHARACTER VARYING(300),
	filter TEXT NOT NULL,
	isdefault INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_grid_filter PRIMARY KEY (id),
	CONSTRAINT ck_model_filter_def CHECK (isdefault in (0, 1)),
	CONSTRAINT fk_model_filter_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_user_model_config (
	id SERIAL,
	usr CHARACTER VARYING(100) NOT NULL,
	model CHARACTER VARYING(100) NOT NULL,
	configuration TEXT NOT NULL,
	type CHARACTER VARYING(50) NOT NULL,
	CONSTRAINT pk_pui_user_grid_config PRIMARY KEY (id),
	CONSTRAINT fk_user_config_usr FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE,
	CONSTRAINT fk_user_config_model FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE
);

CREATE TABLE pui_menu (
	node INTEGER,
	parent INTEGER,
	model CHARACTER VARYING(100),
	component CHARACTER VARYING(100),
	functionality CHARACTER VARYING(100),
	label CHARACTER VARYING(100) NOT NULL,
	icon_label CHARACTER VARYING(100),
	CONSTRAINT pk_pui_menu PRIMARY KEY (node),
	CONSTRAINT fk_menu_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_menu_func FOREIGN KEY (functionality) REFERENCES pui_functionality(functionality),
	CONSTRAINT fk_menu_parent FOREIGN KEY (parent) REFERENCES pui_menu(node)
);

CREATE TABLE pui_variable (
	variable CHARACTER VARYING(50),
	value TEXT NOT NULL,
	description CHARACTER VARYING(500) NOT NULL,
	CONSTRAINT pk_pui_variable PRIMARY KEY (variable)
);

CREATE TABLE pui_elasticsearch_views (
    appname CHARACTER VARYING(100) NOT NULL DEFAULT 'DEFAULT',
    viewname CHARACTER VARYING(100) NOT NULL,
    identity_fields CHARACTER VARYING(100) NOT NULL,
    CONSTRAINT pk_elasticsearch_views PRIMARY KEY (appname, viewname)
);

CREATE TABLE pui_audit (
	id SERIAL,
	model CHARACTER VARYING(100) NOT NULL,
	type CHARACTER VARYING(50) NOT NULL,
	pk CHARACTER VARYING(100),
	datetime timestamptz(3) NOT NULL,
	usr CHARACTER VARYING(100) NOT NULL,
	ip CHARACTER VARYING(100) NOT NULL DEFAULT '0.0.0.0',
	content TEXT,
	client CHARACTER VARYING(100),
	CONSTRAINT pk_pui_audit PRIMARY KEY (id)
);

CREATE TABLE pui_importexport (
	id SERIAL,
	model character varying(100) NOT NULL,
	usr character varying(100) NOT NULL,
	datetime timestamptz(3) NOT NULL,
	filename_csv character varying(100) NOT NULL,
	filename_json character varying(100) NOT NULL,
	executed INTEGER DEFAULT 0 NOT NULL,
	CONSTRAINT pk_pui_impexp PRIMARY KEY (id),
	CONSTRAINT fk_pui_impexp_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_pui_impexp_usr FOREIGN KEY (usr) REFERENCES pui_user(usr),
	CONSTRAINT ck_executed CHECK (executed in (0, 1))
);

CREATE TABLE pui_multi_instance_process (
    id character varying(100) NOT NULL,
    period integer not null,
    time_unit character varying(15) not null,
    instance_assignee_uuid character varying(100),
    latest_execution timestamptz(3),
    latest_heartbeat timestamptz(3),
    CONSTRAINT pk_pui_parallel_process PRIMARY KEY (id)
);

CREATE TABLE pui_api_key (
	api_key character varying(100) NOT NULL,
	name character varying(100) NOT NULL,
	description character varying(100) NOT NULL,
	profile character varying(100) NULL,
	CONSTRAINT pk_api_key PRIMARY KEY (api_key),
	CONSTRAINT unique_name UNIQUE (name),
	CONSTRAINT fk_api_key_profile FOREIGN KEY (profile) REFERENCES pui_profile (profile)
);
