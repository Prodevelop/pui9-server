-- models
INSERT INTO pui_model VALUES ('puialertconfiguration', 'v_pui_alert_configuration', '{"order":[{"column":"id","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puialert', 'v_pui_alert', '{"order":[{"column":"id","direction":"asc"}]}', null);

-- functionalities
INSERT INTO pui_functionality values ('READ_PUI_ALERT', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_ALERT', 'es', 1, 'Visualización de alertas');
INSERT INTO pui_functionality_tra values ('READ_PUI_ALERT', 'en', 1, 'View alerts');
INSERT INTO pui_functionality_tra values ('READ_PUI_ALERT', 'fr', 1, 'Affichage des alertes');
INSERT INTO pui_functionality_tra values ('READ_PUI_ALERT', 'ca', 1, 'Visualització d''alertes');

INSERT INTO pui_functionality values ('WRITE_PUI_ALERT', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_ALERT', 'es', 1, 'Edición alertas');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_ALERT', 'en', 1, 'Manage alerts');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_ALERT', 'fr', 1, 'Modification des alertes');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_ALERT', 'ca', 1, 'Edició d''alertes');

-- profile-functionalities
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_ALERT');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_ALERT');

-- menu
INSERT INTO pui_menu VALUES (2008, 2000, 'puialertconfiguration', null, 'WRITE_PUI_ALERT', 'menu.puialertconfiguration', null);
INSERT INTO pui_menu VALUES (2009, 2000, 'puialert', null, 'READ_PUI_ALERT', 'menu.puialert', null);

-- variables
INSERT INTO pui_variable VALUES ('ALERTS_EXECUTOR_DELAY','10','tiempo en minutos para ejecutar el proceso de envío de alertas por email');
