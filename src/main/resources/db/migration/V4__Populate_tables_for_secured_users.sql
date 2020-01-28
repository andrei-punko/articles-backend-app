
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Ivan', '{noop}ivan_pass', 'ROLE_ADMIN', true);
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Vasily', '{noop}vasily_pass', 'ROLE_USER', true);
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Petr', '{noop}petr_pass', 'ROLE_USER', true);
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Oleg', '{noop}oleg_pass', 'ROLE_USER', false);
