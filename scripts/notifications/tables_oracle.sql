CREATE TABLE pui_user_fcm (
	token VARCHAR2(300) NOT NULL,
	usr VARCHAR2(100) NOT NULL,
	last_use TIMESTAMP WITH TIME ZONE NOT NULL,
	CONSTRAINT PK_user_fcm_token PRIMARY KEY (token),
	CONSTRAINT FK_user_fcm_token_user_fv FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE
);
CREATE INDEX pui_user_fcm_usr_IDX ON pui_user_fcm (usr);
