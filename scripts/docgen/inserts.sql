-- models
INSERT INTO pui_model VALUES ('puidocgenattribute', 'pui_docgen_attribute', '{"order":[{"column":"label","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puidocgentemplate', 'v_pui_docgen_template', '{"order":[{"column":"name","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puidocgenmodel', 'v_pui_docgen_model', '{"order":[{"column":"label","direction":"asc"}]}', null);

-- functionalities
INSERT INTO pui_functionality values ('WRITE_PUI_DOCGEN', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCGEN', 'es', 1, 'Edición de Generación de Documentos');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCGEN', 'en', 1, 'Manage Document Generation');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCGEN', 'fr', 1, 'Manage Document Generation');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCGEN', 'ca', 1, 'Edició de Generació de Documents');

INSERT INTO pui_functionality values ('GEN_PUI_DOCGEN', 'PUI');
INSERT INTO pui_functionality_tra values ('GEN_PUI_DOCGEN', 'es', 1, 'Generación de Documentos');
INSERT INTO pui_functionality_tra values ('GEN_PUI_DOCGEN', 'en', 1, 'Document Generation');
INSERT INTO pui_functionality_tra values ('GEN_PUI_DOCGEN', 'fr', 1, 'Document Generation');
INSERT INTO pui_functionality_tra values ('GEN_PUI_DOCGEN', 'ca', 1, 'Generació de Documents');

-- profile-functionalities
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_DOCGEN');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'GEN_PUI_DOCGEN');

-- menu
INSERT INTO pui_menu VALUES (2007, 2000, 'puidocgentemplate', null, 'GEN_PUI_DOCGEN', 'menu.puidocgen', null);

-- variables
INSERT INTO pui_variable values ('DOCGEN_PATH', '/var/www/html/app/docgen', 'ruta absoluta donde se almacenan las plantillas para generación de documentos');
