CREATE TABLE pui_docgen_model (
	model CHARACTER VARYING(100),
	label CHARACTER VARYING(100) NOT NULL,
	identity_fields CHARACTER VARYING(100),
	CONSTRAINT pk_pui_docgen_view PRIMARY KEY (model),
	CONSTRAINT fk_doc_view_model FOREIGN KEY (model) REFERENCES pui_model(model)
);

CREATE TABLE pui_docgen_attribute (
	id CHARACTER VARYING(100),
	label CHARACTER VARYING(100) NOT NULL,
	value CHARACTER VARYING(500) NOT NULL,
	CONSTRAINT pk_pui_docgen_attribute PRIMARY KEY (id)
);

CREATE TABLE pui_docgen_template (
	id SERIAL,
	name CHARACTER VARYING(100) NOT NULL,
	description CHARACTER VARYING(1000),
	main_model CHARACTER VARYING(100) NOT NULL,
	models CHARACTER VARYING(500),
	column_filename CHARACTER VARYING(200),
	filename CHARACTER VARYING(100) NOT NULL,
	mapping TEXT NOT NULL,
	filter TEXT,
	parameters TEXT,
	CONSTRAINT pk_pui_docgen_template PRIMARY KEY (id),
	CONSTRAINT fk_docgen_main_model FOREIGN KEY (main_model) REFERENCES pui_docgen_model(model)
);
