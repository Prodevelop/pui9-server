CREATE TABLE pui_widget_type (
	id INTEGER,
	name VARCHAR2(100) NOT NULL,
	type VARCHAR2(100) NOT NULL,
	component VARCHAR2(100) NOT NULL,
	definition CLOB,
	CONSTRAINT PK_PUI_WIDGET_TYPE PRIMARY KEY (ID)
);

CREATE TABLE pui_widget (
	id INTEGER,
	name VARCHAR2(100) NOT NULL,
	typeid INTEGER NOT NULL,
	definition CLOB,
	CONSTRAINT PK_PUI_WIDGET PRIMARY KEY (ID),
	CONSTRAINT FK_PUI_WIDGET_TYPE FOREIGN KEY (TYPEID) REFERENCES PUI_WIDGET_TYPE (ID)
);

CREATE TABLE pui_dashboard (
	id INTEGER,
	name VARCHAR2(100) NOT NULL,
	definition CLOB,
	CONSTRAINT PK_PUI_DASHBOARD PRIMARY KEY (ID)
);

-- SEQUENCES FOR AUTOINCREMENTABLE IDs
CREATE SEQUENCE pui_widget_type_sequence;
CREATE SEQUENCE pui_widget_sequence;
CREATE SEQUENCE pui_dashboard_sequence;

-- TRIGGERS FOR AUTOINCREMENTABLE IDs
CREATE TRIGGER pui_widget_type_on_insert
  BEFORE INSERT ON pui_widget_type
  FOR EACH ROW
BEGIN
  SELECT pui_widget_type_sequence.nextval
  INTO :new.id
  FROM dual;
END;

CREATE TRIGGER pui_widget_on_insert
  BEFORE INSERT ON pui_widget
  FOR EACH ROW
BEGIN
  SELECT pui_widget_sequence.nextval
  INTO :new.id
  FROM dual;
END;

CREATE TRIGGER pui_dashboard_on_insert
  BEFORE INSERT ON pui_dashboard
  FOR EACH ROW
BEGIN
  SELECT pui_dashboard_sequence.nextval
  INTO :new.id
  FROM dual;
END;
