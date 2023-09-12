-- models
INSERT INTO pui_model VALUES ('puidashboard', 'v_pui_dashboard', '{"order":[{"column":"name","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puiwidget', 'v_pui_widget', '{"order":[{"column":"name","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puiwidgettype', 'v_pui_widget_type', '{"order":[{"column":"name","direction":"asc"}]}', null);

-- functionalities
INSERT INTO PUI_FUNCTIONALITY VALUES ('READ_PUI_WIDGET','PUI');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('READ_PUI_WIDGET','es',1, 'Visualización de widgets');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('READ_PUI_WIDGET','en',1, 'View PUI Widgets');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('READ_PUI_WIDGET','fr',1, 'View PUI Widgets');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('READ_PUI_WIDGET','ca',1, 'Visualització de widgets');

INSERT INTO PUI_FUNCTIONALITY VALUES ('WRITE_PUI_WIDGET','PUI');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('WRITE_PUI_WIDGET','es',1, 'Edición de widgets');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('WRITE_PUI_WIDGET','en',1, 'Manage PUI Widgets');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('WRITE_PUI_WIDGET','fr',1, 'Manage PUI Widgets');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('WRITE_PUI_WIDGET','ca',1, 'Edició de widgets');

INSERT INTO PUI_FUNCTIONALITY VALUES ('READ_PUI_DASHBOARD','PUI');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('READ_PUI_DASHBOARD','es',1, 'Visualización de dashboard');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('READ_PUI_DASHBOARD','en',1, 'View PUI Dashboard');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('READ_PUI_DASHBOARD','fr',1, 'View PUI Dashboard');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('READ_PUI_DASHBOARD','ca',1, 'Visualització de dashboard');

INSERT INTO PUI_FUNCTIONALITY VALUES ('WRITE_PUI_DASHBOARD','PUI');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('WRITE_PUI_DASHBOARD','es',1, 'Edición de dashboard');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('WRITE_PUI_DASHBOARD','en',1, 'Manage PUI Dashboard');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('WRITE_PUI_DASHBOARD','fr',1, 'Manage PUI Dashboard');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('WRITE_PUI_DASHBOARD','ca',1, 'Edició de dashboard');

INSERT INTO PUI_FUNCTIONALITY VALUES ('MENU_PUI_WIDGET','PUI');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('MENU_PUI_WIDGET','es',1, 'Menú de PUI widgets');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('MENU_PUI_WIDGET','en',1, 'Menu PUI Widgets');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('MENU_PUI_WIDGET','fr',1, 'Menu PUI Widgets');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('MENU_PUI_WIDGET','ca',1, 'Menú de PUI widgets');

INSERT INTO PUI_FUNCTIONALITY VALUES ('MENU_PUI_DASHBOARD','PUI');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('MENU_PUI_DASHBOARD','es',1, 'Menú de PUI dashboard');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('MENU_PUI_DASHBOARD','en',1, 'Menu PUI Dashboard');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('MENU_PUI_DASHBOARD','fr',1, 'Menu PUI Dashboard');
INSERT INTO PUI_FUNCTIONALITY_TRA VALUES ('MENU_PUI_DASHBOARD','ca',1, 'Menú de PUI dashboard');

-- profile-functionalities
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_WIDGET');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_WIDGET');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_DASHBOARD');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_DASHBOARD');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'MENU_PUI_WIDGET');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'MENU_PUI_DASHBOARD');

-- menu
INSERT INTO pui_menu VALUES (3000, null, null, null, null, 'menu.dashboards', 'far fa-chart-network');
INSERT INTO pui_menu VALUES (3001, 3000, 'puiwidget', null, 'MENU_PUI_WIDGET', 'menu.widgets', null);
INSERT INTO pui_menu VALUES (3002, 3000, null, 'puiwidgetoverview', 'MENU_PUI_WIDGET', 'menu.widgetspreview', null);
INSERT INTO pui_menu VALUES (3003, 3000, 'puidashboard', NULL, 'MENU_PUI_DASHBOARD', 'menu.dashboard', null);
INSERT INTO pui_menu VALUES (3004, 3000, null, 'puidashboardoverview', 'READ_PUI_DASHBOARD', 'menu.dashboardpreview', null);

-- widget type
INSERT INTO PUI_WIDGET_TYPE (NAME, TYPE, COMPONENT, DEFINITION) VALUES('PUI9 Basic Datatable', 'VuetifyDatatable', 'PuiWidgetDatatable', '{
   "title":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.title",
      "value":null,
      "required":true
   },
   "entityName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.entity",
      "value":null,
      "required":true
   },
   "entityColumns":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.columns",
      "value":null,
      "required":false
   },
   "detailModel":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.detailModel",
      "value":null,
      "required":false
   },
   "detailPrimaryKey":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.detailPrimaryKey",
      "value":null,
      "required":false
   },
   "filter":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.filter",
      "value":null,
      "required":false
   },
   "searchable":{
      "component":"pui-checkbox",
      "label":"form.puiwidget.definition.searchable",
      "value":true,
      "required":false
   },
   "hidePagination":{
      "component":"pui-checkbox",
      "label":"form.puiwidget.definition.hide_pagination",
      "value":false,
      "required":false
   }
}');
INSERT INTO PUI_WIDGET_TYPE (NAME, TYPE, COMPONENT, DEFINITION) VALUES('PUI9 Basic Bar Chart', 'EChartsBar', 'PuiWidgetEChart', '{
   "title":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.title",
      "value":null,
      "required":true
   },
   "entityName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.entity",
      "value":null,
      "required":true
   },
   "columnName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.name",
      "value":null,
      "required":true
   },
   "columnValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.value",
      "value":null,
      "required":true
   },
   "tooltipValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.tooltip",
      "value":null,
      "required":false
   },
   "filter":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.filter",
      "value":null,
      "required":false
   },
   "chartOptions":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.options",
      "value":null,
      "required":false
   }
}');
INSERT INTO PUI_WIDGET_TYPE (NAME, TYPE, COMPONENT, DEFINITION) VALUES('PUI9 Basic Line Chart', 'EChartsLine', 'PuiWidgetEChart', '{
   "title":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.title",
      "value":null,
      "required":true
   },
   "entityName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.entity",
      "value":null,
      "required":true
   },
   "columnName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.name",
      "value":null,
      "required":true
   },
   "columnValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.value",
      "value":null,
      "required":true
   },
   "tooltipValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.tooltip",
      "value":null,
      "required":false
   },
   "filter":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.filter",
      "value":null,
      "required":false
   },
   "chartOptions":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.options",
      "value":null,
      "required":false
   }
}');
INSERT INTO PUI_WIDGET_TYPE (NAME, TYPE, COMPONENT, DEFINITION) VALUES('PUI9 Basic Pie Chart', 'EChartsPie', 'PuiWidgetEChart', '{
   "title":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.title",
      "value":null,
      "required":true
   },
   "entityName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.entity",
      "value":null,
      "required":true
   },
   "columnName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.name",
      "value":null,
      "required":true
   },
   "columnValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.value",
      "value":null,
      "required":true
   },
   "tooltipValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.tooltip",
      "value":null,
      "required":false
   },
   "filter":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.filter",
      "value":null,
      "required":false
   },
   "chartOptions":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.options",
      "value":null,
      "required":false
   },
   "theme":{
      "component":"pui-select",
      "label":"form.puiwidget.definition.echartstheme",
      "value":"roma",
      "required":true,
      "items":["infographic","dark","roma","vintage"]
   }
}');
INSERT INTO PUI_WIDGET_TYPE (NAME, TYPE, COMPONENT, DEFINITION) VALUES('Basic Count', 'Count', 'PuiWidgetCount', '{
   "title":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.title",
      "value":null,
      "required":true
   },
   "entityName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.entity",
      "value":null,
      "required":true
   },
   "filter":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.filter",
      "value":null,
      "required":false
   }
}');

UPDATE PUI_WIDGET_TYPE SET DEFINITION='{
   "title":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.title",
      "value":null,
      "required":true
   },
   "entityName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.entity",
      "value":null,
      "required":true
   },
   "columnName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.name",
      "value":null,
      "required":true
   },
   "columnValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.value",
      "value":null,
      "required":true
   },
   "tooltipValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.tooltip",
      "value":null,
      "required":false
   },
   "filter":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.filter",
      "value":null,
      "required":false
   },
   "chartOptions":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.options",
      "value":null,
      "required":false
   },
   "theme":{
      "component":"pui-select",
      "label":"form.puiwidget.definition.echartstheme",
      "value":"roma",
      "required":true,
      "items":["infographic","dark","roma","vintage"]
   },
   "detailModel":{
   	  "component":"pui-text-field",
      "label":"form.puiwidget.definition.detailModelRedirect",
      "value":null,
      "required":false
   },
   "detailAttributeFilter":{
   	  "component":"pui-text-field",
      "label":"form.puiwidget.definition.detailAttributeFilter",
      "value":null,
      "required":false
   }
}' WHERE ID=4;

UPDATE PUI_WIDGET_TYPE SET DEFINITION='{
   "title":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.title",
      "value":null,
      "required":true
   },
   "entityName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.entity",
      "value":null,
      "required":true
   },
   "columnName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.name",
      "value":null,
      "required":true
   },
   "columnValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.value",
      "value":null,
      "required":true
   },
   "tooltipValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.tooltip",
      "value":null,
      "required":false
   },
   "filter":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.filter",
      "value":null,
      "required":false
   },
   "chartOptions":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.options",
      "value":null,
      "required":false
   },
   "detailModel":{
   	  "component":"pui-text-field",
      "label":"form.puiwidget.definition.detailModelRedirect",
      "value":null,
      "required":false
   },
   "detailAttributeFilter":{
   	  "component":"pui-text-field",
      "label":"form.puiwidget.definition.detailAttributeFilter",
      "value":null,
      "required":false
   }
}' WHERE ID=3;

UPDATE PUI_WIDGET_TYPE SET DEFINITION='{
   "title":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.title",
      "value":null,
      "required":true
   },
   "entityName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.entity",
      "value":null,
      "required":true
   },
   "columnName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.name",
      "value":null,
      "required":true
   },
   "columnValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.value",
      "value":null,
      "required":true
   },
   "tooltipValue":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.tooltip",
      "value":null,
      "required":false
   },
   "filter":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.filter",
      "value":null,
      "required":false
   },
   "chartOptions":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.options",
      "value":null,
      "required":false
   },
   "detailModel":{
   	  "component":"pui-text-field",
      "label":"form.puiwidget.definition.detailModelRedirect",
      "value":null,
      "required":false
   },
   "detailAttributeFilter":{
   	  "component":"pui-text-field",
      "label":"form.puiwidget.definition.detailAttributeFilter",
      "value":null,
      "required":false
   }
}' WHERE ID=2;

UPDATE PUI_WIDGET_TYPE SET DEFINITION='{
   "title":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.title",
      "value":null,
      "required":true
   },
   "entityName":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.entity",
      "value":null,
      "required":true
   },
   "entityColumns":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.columns",
      "value":null,
      "required":false
   },
   "detailModel":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.detailModel",
      "value":null,
      "required":false
   },
   "detailPrimaryKey":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.detailPrimaryKey",
      "value":null,
      "required":false
   },
   "filter":{
      "component":"pui-text-area",
      "label":"form.puiwidget.definition.filter",
      "value":null,
      "required":false
   },
   "searchable":{
      "component":"pui-checkbox",
      "label":"form.puiwidget.definition.searchable",
      "value":true,
      "required":false
   },
   "hidePagination":{
      "component":"pui-checkbox",
      "label":"form.puiwidget.definition.hide_pagination",
      "value":false,
      "required":false
   },
    "entityColumnsSize":{
      "component":"pui-text-field",
      "label":"form.puiwidget.definition.columnsSize",
      "value":null,
      "required":false
   }
}' WHERE ID=1;