CREATE TABLE pui_docgen_model (
	model VARCHAR2(100),
	label VARCHAR2(100) NOT NULL,
	identity_fields VARCHAR2(100),
	CONSTRAINT pk_pui_docgen_view PRIMARY KEY (model),
	CONSTRAINT fk_doc_view_model FOREIGN KEY (model) REFERENCES pui_model(model)
);

CREATE TABLE pui_docgen_attribute (
	id VARCHAR2(100),
	label VARCHAR2(100) NOT NULL,
	value VARCHAR2(500) NOT NULL,
	CONSTRAINT pk_pui_docgen_attribute PRIMARY KEY (id)
);

CREATE TABLE pui_docgen_template (
	id INTEGER,
	name VARCHAR2(100) NOT NULL,
	description VARCHAR2(1000),
	main_model VARCHAR2(100) NOT NULL,
	models VARCHAR2(500),
	column_filename VARCHAR2(200),
	filename VARCHAR2(100) NOT NULL,
	mapping CLOB NOT NULL,
	filter CLOB,
	parameters CLOB,
	CONSTRAINT pk_pui_docgen_template PRIMARY KEY (id),
	CONSTRAINT fk_docgen_main_model FOREIGN KEY (main_model) REFERENCES pui_docgen_model(model)
);

-- SEQUENCES FOR AUTOINCREMENTABLE IDs
CREATE SEQUENCE pui_docgen_template_sequence;

-- TRIGGERS FOR AUTOINCREMENTABLE IDs
CREATE TRIGGER pui_docgen_template_on_insert
  BEFORE INSERT ON pui_docgen_template
  FOR EACH ROW
BEGIN
  SELECT pui_docgen_template_sequence.nextval
  INTO :new.id
  FROM dual;
END;
