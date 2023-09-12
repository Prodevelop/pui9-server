CREATE TABLE pui_user_fcm (
	token CHARACTER VARYING(300) NOT NULL,
	usr CHARACTER VARYING(100) NOT NULL,
	last_use timestamptz(3) NOT NULL,
	CONSTRAINT PK_user_fcm_token PRIMARY KEY (token),
	CONSTRAINT FK_user_fcm_token_user_fv FOREIGN KEY (usr) REFERENCES pui_user(usr) ON DELETE CASCADE
);
CREATE INDEX pui_user_fcm_usr_IDX ON pui_user_fcm (usr);
