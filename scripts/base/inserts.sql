-- languages
INSERT INTO pui_language VALUES ('es', 'Español', 1, 1);
INSERT INTO pui_language VALUES ('en', 'English', 0, 1);
INSERT INTO pui_language VALUES ('fr', 'Français', 0, 1);
INSERT INTO pui_language VALUES ('ca', 'Català', 0, 1);

-- models
INSERT INTO pui_model VALUES ('puiaudit', 'v_pui_audit', '{"columns":[{"name":"id","visibility":"hidden"},{"name":"datetime","visibility":"visible"},{"name":"usr","visibility":"visible"},{"name":"client","visibility":"visible"},{"name":"ip","visibility":"visible"},{"name":"type","visibility":"hidden"},{"name":"model","visibility":"hidden"},{"name":"pk","visibility":"hidden"},{"name":"content","visibility":"hidden"}],"order":[{"column":"datetime","direction":"desc"}]}', null);
INSERT INTO pui_model VALUES ('puifunctionality', 'v_pui_functionality', '{"order":[{"column":"functionality","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puilanguage', 'v_pui_language', '{"order":[{"column":"name","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puiprofile', 'v_pui_profile', '{"order":[{"column":"profile","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puisubsystem', 'v_pui_subsystem', '{"order":[{"column":"subsystem","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puiuser', 'v_pui_user', '{"order":[{"column":"usr","direction":"asc"}]}', null);
INSERT INTO pui_model VALUES ('puivariable', 'v_pui_variable', '{"order":[{"column":"variable","direction":"asc"}]}', null);

-- profiles
INSERT INTO pui_profile VALUES ('ADMIN_PUI');
INSERT INTO pui_profile_tra VALUES ('ADMIN_PUI', 'es', 1, 'Administración');
INSERT INTO pui_profile_tra VALUES ('ADMIN_PUI', 'en', 1, 'Administration');
INSERT INTO pui_profile_tra VALUES ('ADMIN_PUI', 'fr', 1, 'Administration');
INSERT INTO pui_profile_tra VALUES ('ADMIN_PUI', 'ca', 1, 'Administració');

-- subsystems
INSERT INTO pui_subsystem VALUES ('PUI');
INSERT INTO pui_subsystem_tra VALUES ('PUI', 'es', 1, 'Administración de PUI');
INSERT INTO pui_subsystem_tra VALUES ('PUI', 'en', 1, 'PUI Administration');
INSERT INTO pui_subsystem_tra VALUES ('PUI', 'fr', 1, 'Administration de PUI');
INSERT INTO pui_subsystem_tra VALUES ('PUI', 'ca', 1, 'Administració de PUI');

-- functionalities
INSERT INTO pui_functionality values ('READ_PUI_AUDIT', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_AUDIT', 'es', 1, 'Visualización de auditoría');
INSERT INTO pui_functionality_tra values ('READ_PUI_AUDIT', 'en', 1, 'View PUI Audit');
INSERT INTO pui_functionality_tra values ('READ_PUI_AUDIT', 'fr', 1, 'View PUI Audit');
INSERT INTO pui_functionality_tra values ('READ_PUI_AUDIT', 'ca', 1, 'Visualització d''auditoria');

INSERT INTO pui_functionality values ('ACTION_PUI_AUDIT', 'PUI');
INSERT INTO pui_functionality_tra values ('ACTION_PUI_AUDIT', 'es', 1, 'Acción para ver la auditoría');
INSERT INTO pui_functionality_tra values ('ACTION_PUI_AUDIT', 'en', 1, 'PUI Audit action');
INSERT INTO pui_functionality_tra values ('ACTION_PUI_AUDIT', 'fr', 1, 'PUI Audit action');
INSERT INTO pui_functionality_tra values ('ACTION_PUI_AUDIT', 'ca', 1, 'Acció per a veure l''auditoria');

INSERT INTO pui_functionality values ('READ_PUI_FUNCTIONALITY', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_FUNCTIONALITY', 'es', 1, 'Visualización de funcionalidades');
INSERT INTO pui_functionality_tra values ('READ_PUI_FUNCTIONALITY', 'en', 1, 'View funcionalities');
INSERT INTO pui_functionality_tra values ('READ_PUI_FUNCTIONALITY', 'fr', 1, 'View funcionalities');
INSERT INTO pui_functionality_tra values ('READ_PUI_FUNCTIONALITY', 'ca', 1, 'Visualització de funcionalitats');

INSERT INTO pui_functionality values ('READ_PUI_LANGUAGE', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_LANGUAGE', 'es', 1, 'Visualización de Idiomas');
INSERT INTO pui_functionality_tra values ('READ_PUI_LANGUAGE', 'en', 1, 'View languages');
INSERT INTO pui_functionality_tra values ('READ_PUI_LANGUAGE', 'fr', 1, 'View languages');
INSERT INTO pui_functionality_tra values ('READ_PUI_LANGUAGE', 'ca', 1, 'Visualització d''idiomes');

INSERT INTO pui_functionality values ('WRITE_PUI_LANGUAGE', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_LANGUAGE', 'es', 1, 'Edición de Idiomas');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_LANGUAGE', 'en', 1, 'Manage languages');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_LANGUAGE', 'fr', 1, 'Manage languages');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_LANGUAGE', 'ca', 1, 'Edició d''idiomes');

INSERT INTO pui_functionality values ('READ_PUI_PROFILE', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_PROFILE', 'es', 1, 'Visualización de perfiles');
INSERT INTO pui_functionality_tra values ('READ_PUI_PROFILE', 'en', 1, 'View profiles');
INSERT INTO pui_functionality_tra values ('READ_PUI_PROFILE', 'fr', 1, 'View profiles');
INSERT INTO pui_functionality_tra values ('READ_PUI_PROFILE', 'ca', 1, 'Visualització de perfils');

INSERT INTO pui_functionality values ('WRITE_PUI_PROFILE', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_PROFILE', 'es', 1, 'Edición de perfiles');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_PROFILE', 'en', 1, 'Manage profiles');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_PROFILE', 'fr', 1, 'Manage profiles');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_PROFILE', 'ca', 1, 'Edició de perfils');

INSERT INTO pui_functionality values ('READ_PUI_USER', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_USER', 'es', 1, 'Visualización de usuarios');
INSERT INTO pui_functionality_tra values ('READ_PUI_USER', 'en', 1, 'View application users');
INSERT INTO pui_functionality_tra values ('READ_PUI_USER', 'fr', 1, 'View application users');
INSERT INTO pui_functionality_tra values ('READ_PUI_USER', 'ca', 1, 'Visualització d''usuaris');

INSERT INTO pui_functionality values ('WRITE_PUI_USER', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_USER', 'es', 1, 'Edición de usuarios');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_USER', 'en', 1, 'Manage application users');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_USER', 'fr', 1, 'Manage application users');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_USER', 'ca', 1, 'Edició d''usuaris');

INSERT INTO pui_functionality values ('CHANGE_USER_PASSWORDS', 'PUI');
INSERT INTO pui_functionality_tra values ('CHANGE_USER_PASSWORDS', 'es', 1, 'Modificar contraseñas de los usuarios');
INSERT INTO pui_functionality_tra values ('CHANGE_USER_PASSWORDS', 'en', 1, 'Modify users passwords');
INSERT INTO pui_functionality_tra values ('CHANGE_USER_PASSWORDS', 'fr', 1, 'Modifier les mots de passe des utilisateurs');
INSERT INTO pui_functionality_tra values ('CHANGE_USER_PASSWORDS', 'ca', 1, 'Modificar contrasenyes dels usuaris');

INSERT INTO pui_functionality values ('UPDATE_CURRENT_USER', 'PUI');
INSERT INTO pui_functionality_tra values ('UPDATE_CURRENT_USER', 'es', 1, 'Modificar usuario actual');
INSERT INTO pui_functionality_tra values ('UPDATE_CURRENT_USER', 'en', 1, 'Modify current user');
INSERT INTO pui_functionality_tra values ('UPDATE_CURRENT_USER', 'fr', 1, 'Modifier l''utilisateur actuel');
INSERT INTO pui_functionality_tra values ('UPDATE_CURRENT_USER', 'ca', 1, 'Modificar usuari actual');

INSERT INTO pui_functionality values ('READ_PUI_VARIABLE', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_VARIABLE', 'es', 1, 'Visualización de variables');
INSERT INTO pui_functionality_tra values ('READ_PUI_VARIABLE', 'en', 1, 'View variable');
INSERT INTO pui_functionality_tra values ('READ_PUI_VARIABLE', 'fr', 1, 'View variable');
INSERT INTO pui_functionality_tra values ('READ_PUI_VARIABLE', 'ca', 1, 'Visualització de variables');

INSERT INTO pui_functionality values ('WRITE_PUI_VARIABLE', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_VARIABLE', 'es', 1, 'Edición de variables');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_VARIABLE', 'en', 1, 'Manage variable');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_VARIABLE', 'fr', 1, 'Manage variable');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_VARIABLE', 'ca', 1, 'Edició de variables');

INSERT INTO pui_functionality values ('READ_PUI_GENERATED_ENTITY', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_PUI_GENERATED_ENTITY', 'es', 1, 'Visualización para tablas generadas automáticamente');
INSERT INTO pui_functionality_tra values ('READ_PUI_GENERATED_ENTITY', 'en', 1, 'View autogen tables');
INSERT INTO pui_functionality_tra values ('READ_PUI_GENERATED_ENTITY', 'fr', 1, 'View autogen tables');
INSERT INTO pui_functionality_tra values ('READ_PUI_GENERATED_ENTITY', 'ca', 1, 'Visualització de taules generades automàticament');

INSERT INTO pui_functionality values ('WRITE_PUI_GENERATED_ENTITY', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_GENERATED_ENTITY', 'es', 1, 'Edición de tablas generadas automáticamente');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_GENERATED_ENTITY', 'en', 1, 'Manage autogen tables');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_GENERATED_ENTITY', 'fr', 1, 'Manage autogen tables');
INSERT INTO pui_functionality_tra values ('WRITE_PUI_GENERATED_ENTITY', 'ca', 1, 'Edició de taules generades automàticament');

INSERT INTO pui_functionality values ('GEN_PUI_GENERATED_ENTITY', 'PUI');
INSERT INTO pui_functionality_tra values ('GEN_PUI_GENERATED_ENTITY', 'es', 1, 'Generación automática de tablas');
INSERT INTO pui_functionality_tra values ('GEN_PUI_GENERATED_ENTITY', 'en', 1, 'Generate autogen tables');
INSERT INTO pui_functionality_tra values ('GEN_PUI_GENERATED_ENTITY', 'fr', 1, 'Generate autogen tables');
INSERT INTO pui_functionality_tra values ('GEN_PUI_GENERATED_ENTITY', 'ca', 1, 'Generació automàtica de taules');

INSERT INTO pui_functionality values ('LIST_LOGIN', 'PUI');
INSERT INTO pui_functionality_tra values ('LIST_LOGIN', 'es', 1, 'Visualización de logins');
INSERT INTO pui_functionality_tra values ('LIST_LOGIN', 'en', 1, 'View PUI Logins');
INSERT INTO pui_functionality_tra values ('LIST_LOGIN', 'fr', 1, 'View PUI Logins');
INSERT INTO pui_functionality_tra values ('LIST_LOGIN', 'ca', 1, 'Visualització de logins');

INSERT INTO pui_functionality values ('LIST_PUI_SESSIONS', 'PUI');
INSERT INTO pui_functionality_tra values ('LIST_PUI_SESSIONS', 'es', 1, 'Listar sesiones abiertas');
INSERT INTO pui_functionality_tra values ('LIST_PUI_SESSIONS', 'en', 1, 'List opened sessions');
INSERT INTO pui_functionality_tra values ('LIST_PUI_SESSIONS', 'fr', 1, 'Liste des sessions ouvertes');
INSERT INTO pui_functionality_tra values ('LIST_PUI_SESSIONS', 'ca', 1, 'Llistar sessions obertes');

INSERT INTO pui_functionality values ('KILL_PUI_SESSIONS', 'PUI');
INSERT INTO pui_functionality_tra values ('KILL_PUI_SESSIONS', 'es', 1, 'Eliminar sesiones abiertas');
INSERT INTO pui_functionality_tra values ('KILL_PUI_SESSIONS', 'en', 1, 'Remove opened sessions');
INSERT INTO pui_functionality_tra values ('KILL_PUI_SESSIONS', 'fr', 1, 'Supprimer les sessions ouvertes');
INSERT INTO pui_functionality_tra values ('KILL_PUI_SESSIONS', 'ca', 1, 'Eliminar sessions obertes');

INSERT INTO pui_functionality values ('EXECUTE_IMPORT_EXPORT','PUI');
INSERT INTO pui_functionality_tra values ('EXECUTE_IMPORT_EXPORT', 'ca', 1, 'Acció de Import/Export');
INSERT INTO pui_functionality_tra values ('EXECUTE_IMPORT_EXPORT', 'en', 1, 'Action Import/Export');
INSERT INTO pui_functionality_tra values ('EXECUTE_IMPORT_EXPORT', 'fr', 1, 'Action Import/Export');
INSERT INTO pui_functionality_tra values ('EXECUTE_IMPORT_EXPORT', 'es', 1, 'Acción de Import/Export');

INSERT INTO pui_functionality values ('EXECUTE_COPY','PUI');
INSERT INTO pui_functionality_tra values ('EXECUTE_COPY','es', 1, 'Acción de Copiar Registro');
INSERT INTO pui_functionality_tra values ('EXECUTE_COPY','en', 1, 'Action Copy Registry');
INSERT INTO pui_functionality_tra values ('EXECUTE_COPY','fr', 1, 'Action copier un document');
INSERT INTO pui_functionality_tra values ('EXECUTE_COPY','ca', 1, 'Acció de Copiar Registre');

INSERT INTO pui_functionality values ('READ_ELASTICSEARCH', 'PUI');
INSERT INTO pui_functionality_tra values ('READ_ELASTICSEARCH', 'es', 1, 'Consultas ElasticSearch');
INSERT INTO pui_functionality_tra values ('READ_ELASTICSEARCH', 'en', 1, 'ElasticSearch requests');
INSERT INTO pui_functionality_tra values ('READ_ELASTICSEARCH', 'fr', 1, 'ElasticSearch demandes');
INSERT INTO pui_functionality_tra values ('READ_ELASTICSEARCH', 'ca', 1, 'Consultes ElasticSearch');

INSERT INTO pui_functionality values ('WRITE_ELASTICSEARCH', 'PUI');
INSERT INTO pui_functionality_tra values ('WRITE_ELASTICSEARCH', 'es', 1, 'Modificaciones ElasticSearch');
INSERT INTO pui_functionality_tra values ('WRITE_ELASTICSEARCH', 'en', 1, 'ElasticSearch modifications');
INSERT INTO pui_functionality_tra values ('WRITE_ELASTICSEARCH', 'fr', 1, 'ElasticSearch modifications');
INSERT INTO pui_functionality_tra values ('WRITE_ELASTICSEARCH', 'ca', 1, 'Modificacions ElasticSearch');

INSERT INTO pui_functionality values ('ADMIN_ELASTICSEARCH', 'PUI');
INSERT INTO pui_functionality_tra values ('ADMIN_ELASTICSEARCH', 'es', 1, 'Administrar ElasticSearch');
INSERT INTO pui_functionality_tra values ('ADMIN_ELASTICSEARCH', 'en', 1, 'Manage modifications');
INSERT INTO pui_functionality_tra values ('ADMIN_ELASTICSEARCH', 'fr', 1, 'ElasticSearch admin');
INSERT INTO pui_functionality_tra values ('ADMIN_ELASTICSEARCH', 'ca', 1, 'Administrar ElasticSearch');

-- profile-functionalities
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_AUDIT');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'ACTION_PUI_AUDIT');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_FUNCTIONALITY');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_LANGUAGE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_LANGUAGE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_PROFILE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_PROFILE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_USER');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_USER');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'CHANGE_USER_PASSWORDS');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'UPDATE_CURRENT_USER');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_VARIABLE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_VARIABLE');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_PUI_GENERATED_ENTITY');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_PUI_GENERATED_ENTITY');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'GEN_PUI_GENERATED_ENTITY');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'LIST_LOGIN');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'LIST_PUI_SESSIONS');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'KILL_PUI_SESSIONS');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'EXECUTE_IMPORT_EXPORT');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'EXECUTE_COPY');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'READ_ELASTICSEARCH');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'WRITE_ELASTICSEARCH');
INSERT INTO pui_profile_functionality values ('ADMIN_PUI', 'ADMIN_ELASTICSEARCH');

-- menu
INSERT INTO pui_menu VALUES (1000, null, null, null, null, 'menu.accounts', 'fal fa-users');
INSERT INTO pui_menu VALUES (1001, 1000, 'puiuser', null, 'WRITE_PUI_USER', 'menu.puiuser', null);
INSERT INTO pui_menu VALUES (1002, 1000, 'puiprofile', null, 'READ_PUI_PROFILE', 'menu.puiprofile', null);
INSERT INTO pui_menu VALUES (1003, 1000, 'puifunctionality', null, 'READ_PUI_FUNCTIONALITY', 'menu.puifunctionality', null);
INSERT INTO pui_menu VALUES (2000, null, null, null, null, 'menu.configuration', 'fal fa-sliders-h');
INSERT INTO pui_menu VALUES (2001, 2000, 'puilanguage', null, 'WRITE_PUI_LANGUAGE', 'menu.puilanguage', null);
INSERT INTO pui_menu VALUES (2002, 2000, 'puivariable', null, 'WRITE_PUI_VARIABLE', 'menu.puivariable', null);
INSERT INTO pui_menu VALUES (2003, 2000, null, null, null, 'menu.elasticsearch', null);
INSERT INTO pui_menu VALUES (2004, 2000, 'puiaudit', null, 'LIST_LOGIN', 'menu.puiaudit', null);
INSERT INTO pui_menu VALUES (2005, 2000, null, 'session', 'LIST_PUI_SESSIONS', 'menu.puisession', null);
INSERT INTO pui_menu VALUES (2006, 2000, null, 'elasticsearch', 'ADMIN_ELASTICSEARCH', 'menu.elasticsearch', NULL);

-- users
INSERT INTO pui_user VALUES ('admin', 'Admin', '$2a$10$z7T1x/t8rhJgPLA/0DijL.4XQXeruaRkYweDx3h.OrlvS0M04vHAm', 'es', 'admin@prodevelop.es', 0, NULL,'dd/MM/yyyy',NULL,NULL,NULL,NULL,'0','0',NULL,null); -- admin

-- users-profiles
INSERT INTO pui_user_profile VALUES ('admin', 'ADMIN_PUI');

-- api-key
INSERT INTO pui_api_key VALUES ('01234567890123456789', 'testApiKey', 'Api Key de prueba', NULL);

-- variables
-- LDAP_PASSWORD, MAIL_SMTP_PASS, SESSION_JWT_SECRET values are
-- encrypted with AES using the secret '01234567890123456789012345678901'
INSERT INTO pui_variable values ('APPLICATION_LEGAL_TEXT', '-', 'Texto legal de la aplicación');
INSERT INTO pui_variable values ('APPLICATION_NAME', 'APPNAME', 'Nombre de la aplicación');
INSERT INTO pui_variable values ('BASE_CLIENT_URL','http://localhost/admin','URL base de la web cliente');
INSERT INTO pui_variable values ('DEVELOPMENT_ENVIRONMENT', 'true', 'indica si estamos en el entorno de desarrollo-preproducción o producción (true/false)');
INSERT INTO pui_variable values ('ELASTICSEARCH_ACTIVE', 'true', 'Si Elastic Search está activado para la aplicación o no (true/false)');
INSERT INTO pui_variable values ('GRIDFILTER_SHOW_SUBGROUP_BTN', 'false', 'true/false, indica si se debe mostrar el botón añadir subgrupo en la definición de filtros de usuario');
INSERT INTO pui_variable values ('IMPORTEXPORT_PATH', '/var/www/html/app/importexport', 'ruta absoluta donde se almacenan los ficheros de import/export');
INSERT INTO pui_variable values ('ISSUE_TICKET_SERVICE_ENABLED', 'false', 'Habilita/Deshabilita el componente para mandar incidencias al CAU (true/false)');
INSERT INTO pui_variable values ('ISSUE_TICKET_SERVICE_EMAIL', '-', 'Email del servicio de incidencias del CAU');
INSERT INTO pui_variable values ('LDAP_ACTIVE', 'false', 'Indica si el LDAP está activo o no (true/false)');
INSERT INTO pui_variable values ('LDAP_DOMAIN', 'prode_domi.es', 'Dominio a usar en el LDAP. Si no se quiere, especificar ''-''');
INSERT INTO pui_variable values ('LDAP_PASSWORD', 'OqF6SOhFeHNuP9/HFe4jcw==', 'Password del usuario de conexión al LDAP. Si no se quiere, especificar ''-''');
INSERT INTO pui_variable values ('LDAP_URL', 'ldap://130.0.2.90:389', 'URL del servidor LDAP en formato ldap://server:ip. Si no se quiere, especificar ''-''');
INSERT INTO pui_variable values ('LDAP_USER', 'ldapI1803', 'Usuario de conexión al LDAP. Si no se quiere, especificar ''-''');
INSERT INTO pui_variable values ('LOGIN_ENABLE_2FA', 'false', 'Habilitar/Deshabilitar doble factor de autenticación (2FA) mediante QR a nivel de aplicación (true/false)');
INSERT INTO pui_variable values ('LOGIN_MAX_ATTEMPTS', '-', 'Número máximo de intentos de login antes de ser bloqueado. Si no se quiere, especificar ''-''');
INSERT INTO pui_variable values ('MAIL_FROM', 'no-reply@prodevelop.es', 'Email por defecto para el FROM. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('MAIL_FROM_ALIAS', '-', 'Alias para el FROM. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('MAIL_SMTP_AUTH', 'true', 'Usar autenticación en el servidor SMTP (true/false)');
INSERT INTO pui_variable values ('MAIL_SMTP_HOST', 'smtp.office365.com', 'Host SMTP. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('MAIL_SMTP_PASS', '3O2KOmL3/FVduHBM9j/5xgfML9+oCbM88+WTrJFYPh4=', 'Contraseña del usuario SMTP. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('MAIL_SMTP_PORT', '587', 'Puerto SMTP. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('MAIL_SMTP_STARTTLS_ENABLE', 'true', 'Usar seguridad TLS en el servidor SMTP (true/false)');
INSERT INTO pui_variable values ('MAIL_SMTP_USER', 'no-reply@prodevelop.es', 'Usuario SMTP. Especificar ''-'' si no se quiere valor');
INSERT INTO pui_variable values ('PASSWORD_CHANGE_MAIL_SUBJECT_LABEL_ID', '-', 'Etiqueta de traducción del título del email de cambio de contraseña. ''-'' para valor por defecto del framework');
INSERT INTO pui_variable values ('PASSWORD_EXPIRATION_DAYS', '-', 'Periodo de validez de la contraseña (en días). Si no se quiere, especificar ''-''');
INSERT INTO pui_variable VALUES ('PASSWORD_EXPIRATION_MAIL_NOTIFY', 'false', 'Indica si se debe notificar por email a los usuarios cuando su contraseña está a punto de expirar. true/false');
INSERT INTO pui_variable values ('PASSWORD_EXPIRATION_MAIL_SUBJECT_LABEL_ID', '-', 'Etiqueta de traducción del título del email de expiración de contraseña. ''-'' para valor por defecto del framework');
INSERT INTO pui_variable values ('PASSWORD_PATTERN', '-', 'Patrón que la contraseña debe cumplir. Si no se quiere, especificar ''-''');
INSERT INTO pui_variable values ('PASSWORD_PATTERN_INFO', '-', 'Descripción del patrón. Puede ser un JSON con un valor para cada idioma. Si no se quiere, especificar ''-''');
INSERT INTO pui_variable values ('PASSWORD_REMEMBER_DAYS', '-', 'Recordar cambio de contraseña a los usuarios con N días de antelación. Si no se quiere, especificar ''-''');
INSERT INTO pui_variable values ('SESSION_JWT_SECRET', 'dwMJYDwMsisLVvWS1O/lA9V1W6lcd60ToK7rghYAt/1Gr5gWNOJECAe1W9WUMvTro4Ulia+mNy0Fa2xZakGj0dFlGvoImCMGhY1M5uX/ni92mhl3yf3QfbD7s0pidZe+', 'clave secreta para cifrar los tokens JWT');
INSERT INTO pui_variable values ('SESSION_PERSISTENT_DURATION', '30', 'Tiempo en días que una sesión persistente estará vigente desde la última vez que se usó');
INSERT INTO pui_variable values ('SESSION_RECREATE_IF_NOT_IN_DB', 'false', 'Recrear la sesión aún si no existe en BBDD. Se recomienda tenerlo a ''false''');
INSERT INTO pui_variable values ('SESSION_TIMEOUT', '30', 'tiempo en minutos para invalidar la sesión de usuario');
