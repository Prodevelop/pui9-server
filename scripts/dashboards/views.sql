CREATE VIEW v_pui_widget_type AS
SELECT
   ID,
   NAME,
   TYPE,
   COMPONENT,
   DEFINITION
FROM PUI_WIDGET_TYPE;

CREATE VIEW v_pui_widget AS
SELECT
   W.ID,
   W.NAME,
   W.TYPEID,
   WT.TYPE,
   WT.COMPONENT,
   W.DEFINITION
FROM PUI_WIDGET W
LEFT JOIN PUI_WIDGET_TYPE WT ON
   W.TYPEID = WT.ID;

CREATE VIEW v_pui_dashboard AS
SELECT
   ID,
   NAME,
   DEFINITION
FROM PUI_DASHBOARD;
