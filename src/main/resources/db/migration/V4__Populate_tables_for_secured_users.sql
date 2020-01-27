
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Ivan', 'ivan_pass', 'ROLE_ADMIN', true);
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Vasily', 'vasily_pass', 'ROLE_USER', true);
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Petr', 'petr_pass', 'ROLE_USER', true);
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Oleg', 'oleg_pass', 'ROLE_USER', false);
