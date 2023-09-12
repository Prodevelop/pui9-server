CREATE TABLE pui_docgen_model (
	model VARCHAR(100),
	label VARCHAR(100) NOT NULL,
	identity_fields VARCHAR(100),
	CONSTRAINT pk_pui_docgen_view PRIMARY KEY (model),
	CONSTRAINT fk_doc_view_model FOREIGN KEY (model) REFERENCES pui_model(model)
);

CREATE TABLE pui_docgen_attribute (
	id VARCHAR(100),
	label VARCHAR(100) NOT NULL,
	value VARCHAR(500) NOT NULL,
	CONSTRAINT pk_pui_docgen_attribute PRIMARY KEY (id)
);

CREATE TABLE pui_docgen_template (
	id INTEGER IDENTITY(1, 1),
	name VARCHAR(100) NOT NULL,
	description VARCHAR(1000),
	main_model VARCHAR(100) NOT NULL,
	models VARCHAR(500),
	column_filename VARCHAR(200),
	filename VARCHAR(100) NOT NULL,
	mapping VARCHAR(MAX) NOT NULL,
	filter VARCHAR(MAX),
	parameters VARCHAR(MAX),
	CONSTRAINT pk_pui_docgen_template PRIMARY KEY (id),
	CONSTRAINT fk_docgen_main_model FOREIGN KEY (main_model) REFERENCES pui_docgen_model(model)
);
