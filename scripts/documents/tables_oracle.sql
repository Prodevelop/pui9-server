CREATE TABLE pui_document_extension (
	extension VARCHAR2(10),
	max_size INTEGER,
	CONSTRAINT pk_pui_doc_ext PRIMARY KEY (extension)
);

CREATE TABLE pui_document_model_extension (
	model VARCHAR2(100),
	extension VARCHAR2(10),
	CONSTRAINT pk_pui_doc_mod_ext PRIMARY KEY (model, extension),
	CONSTRAINT fk_pui_doc_mod_ext_mod FOREIGN KEY (model) REFERENCES pui_model(model) ON DELETE CASCADE,
	CONSTRAINT fk_pui_doc_mod_ext_ext FOREIGN KEY (extension) REFERENCES pui_document_extension(extension) ON DELETE CASCADE
);

CREATE TABLE pui_document_role (
	role VARCHAR2(100),
	CONSTRAINT pk_pui_document_role PRIMARY KEY (role)
);

CREATE TABLE pui_document_role_tra (
	role VARCHAR2(100),
	lang VARCHAR2(2),
	lang_status INTEGER DEFAULT 0 NOT NULL,
	description VARCHAR2(200) NOT NULL,
	CONSTRAINT pk_pui_document_role_tra PRIMARY KEY (role, lang),
	CONSTRAINT fk_doc_role_tra_role FOREIGN KEY (role) REFERENCES pui_document_role(role) ON DELETE CASCADE,
	CONSTRAINT fk_doc_role_tra_lang FOREIGN KEY (lang) REFERENCES pui_language(isocode) ON DELETE CASCADE,
	CONSTRAINT ck_doc_role_tra_status CHECK (lang_status in (0, 1))
);

CREATE TABLE pui_document (
	id INTEGER,
	model VARCHAR2(100) NOT NULL,
	pk VARCHAR2(100) NOT NULL,
	language VARCHAR2(2),
	description VARCHAR2(100) NOT NULL,
	filename VARCHAR2(100) NOT NULL,
	filename_orig VARCHAR2(100) NOT NULL,
	role VARCHAR2(100) NOT NULL,
	thumbnails VARCHAR2(100),
	datetime TIMESTAMP WITH TIME ZONE NOT NULL,
	CONSTRAINT pk_pui_document PRIMARY KEY (id),
	CONSTRAINT fk_doc_model FOREIGN KEY (model) REFERENCES pui_model(model),
	CONSTRAINT fk_doc_doc_role FOREIGN KEY (role) REFERENCES pui_document_role(role),
	CONSTRAINT fk_doc_lang FOREIGN KEY (language) REFERENCES pui_language(isocode)
);

-- SEQUENCES FOR AUTOINCREMENTABLE IDs
CREATE SEQUENCE pui_document_sequence;

-- TRIGGERS FOR AUTOINCREMENTABLE IDs
CREATE TRIGGER pui_document_on_insert
  BEFORE INSERT ON pui_document
  FOR EACH ROW
BEGIN
  SELECT pui_document_sequence.nextval
  INTO :new.id
  FROM dual;
END;
