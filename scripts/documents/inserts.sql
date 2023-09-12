-- models
INSERT INTO pui_model VALUES ('puidocument', 'v_pui_document', '{"order":[{"column":"id","direction":"asc"}]}', null);

-- functionalities
INSERT INTO pui_functionality values ('READ_PUI_DOCUMENT', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_DOCUMENT', 'es', 1, 'Visualización de documentos adjuntos');
INSERT INTO pui_functionality_tra values ('READ_PUI_DOCUMENT', 'en', 1, 'View documents');
INSERT INTO pui_functionality_tra values ('READ_PUI_DOCUMENT', 'fr', 1, 'View documents');
INSERT INTO pui_functionality_tra values ('READ_PUI_DOCUMENT', 'ca', 1, 'Visualització de documents adjunts');

INSERT INTO pui_functionality values ('WRITE_PUI_DOCUMENT', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCUMENT', 'es', 1, 'Edición documentos');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCUMENT', 'en', 1, 'Manage documents');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCUMENT', 'fr', 1, 'Manage documents');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_DOCUMENT', 'ca', 1, 'Edició de documents');

-- profile-functionalities
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_DOCUMENT');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_DOCUMENT');

-- document extensions
INSERT INTO pui_document_extension VALUES ('*', null);

-- document roles
INSERT INTO pui_document_role VALUES ('attachment');
INSERT INTO pui_document_role_tra VALUES ('attachment', 'es', 1, 'Rol para adjuntos');
INSERT INTO pui_document_role_tra VALUES ('attachment', 'en', 1, 'An attachment role');
INSERT INTO pui_document_role_tra VALUES ('attachment', 'fr', 1, 'Un rôle d''attachement');
INSERT INTO pui_document_role_tra VALUES ('attachment', 'ca', 1, 'Rol per a adjunts');

-- variables
INSERT INTO pui_variable values ('DOCUMENTS_BASE_URL', '-', 'URL base para obtener ficheros adjuntos a través del Apache. Ejemplo: ''http://localhost/appdocs/''. Si no se quiere, poner valor ''-''');
INSERT INTO pui_variable values ('DOCUMENTS_CLEAN_ENABLED', 'false', 'habilita el proceso de limpieza de Documentos no existentes (true/false)');
INSERT INTO pui_variable values ('DOCUMENTS_PATH', '/var/www/html/app/documents', 'ruta absoluta donde se almacenan los documentos adjuntos');
INSERT INTO pui_variable values ('DOCUMENTS_THUMBNAILS_CROP', 'true', 'indica si las imágenes se deben cortar en caso de tener que reducir su tamaño (true/false)');
INSERT INTO pui_variable values ('DOCUMENTS_THUMBNAILS_GENERATE', 'false', 'indica si se deben generar thumbnails (true/false)');
INSERT INTO pui_variable values ('DOCUMENTS_THUMBNAILS_VALUES', '150x150,240x180,640x480', 'lista separada por comas de los tamaños de los thumbnails para documentos adjuntos de tipo imagen');
