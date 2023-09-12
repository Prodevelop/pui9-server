CREATE TABLE pui_document_extension (
	extension CHARACTER VARYING(10),
	max_size INTEGER,
	CONSTRAINT pk_pui_doc_ext PRIMARY KEY (extension)
);

CREATE TABLE pui_document_model_extension (
	model CHARACTER VARYING(100),
	extension CHARACTER VARYING(10),
	CONSTRAINT pk_pui_doc_mod_ext PRIMARY KEY (model, extension),
	CONSTRAINT fk_pui_doc_mod_ext_mod FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE,
	CONSTRAINT fk_pui_doc_mod_ext_ext FOREIGN KEY (extension) REFERENCES pui_document_extension(extension) ON DELETE CASCADE
);

CREATE TABLE pui_document_role (
	role CHARACTER VARYING(100),
	CONSTRAINT pk_pui_document_role PRIMARY KEY (role)
);

CREATE TABLE pui_document_role_tra (
	role CHARACTER VARYING(100),
	lang CHARACTER VARYING(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	description CHARACTER VARYING(200) NOT NULL,
	CONSTRAINT pk_pui_document_role_tra PRIMARY KEY (role, lang),
	CONSTRAINT fk_doc_role_tra_role FOREIGN KEY (role) REFERENCES pui_document_role(role) ON DELETE CASCADE,
	CONSTRAINT fk_doc_role_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_doc_role_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_document (
	id SERIAL,
	model CHARACTER VARYING(100) NOT NULL,
	pk CHARACTER VARYING(100) NOT NULL,
	language CHARACTER VARYING(2),
	description CHARACTER VARYING(100) NOT NULL,
	filename CHARACTER VARYING(100) NOT NULL,
	filename_orig CHARACTER VARYING(100) NOT NULL,
	role CHARACTER VARYING(100) NOT NULL,
	thumbnails CHARACTER VARYING(100),
	datetime timestamptz(3) NOT NULL,
	CONSTRAINT pk_pui_document PRIMARY KEY (id),
	CONSTRAINT fk_doc_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_doc_doc_role FOREIGN KEY (role) REFERENCES pui_document_role(role),
	CONSTRAINT fk_doc_lang FOREIGN KEY (language) REFERENCES pui_language(isocode)
);
