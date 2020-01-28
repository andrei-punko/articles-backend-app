
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Ivan', '{bcrypt}$2y$12$hkcB6greXd/nlLPzBVL8cu0kZA0vJFiPpp4szK/G2Bx3zHh5PR0di', 'ROLE_ADMIN', true);
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Vasily', '{bcrypt}$2y$12$xuRL6ncg2RVb4imAFKLOz.a3OAbxmIAYV5YfZrv2vVhm/26eS7V2C', 'ROLE_USER', true);
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Petr', '{bcrypt}$2y$12$5dYMkdF.Qn9J4azlMG1F1.XcSNrpNPcYd95ExjAYI3TrO8d/wPahW', 'ROLE_USER', true);
insert into SECURED_USERS (ID, USERNAME, PASSWORD, ROLE, ENABLED) values (nextval('SECURED_USER_ID_SEQ'), 'Oleg', '{bcrypt}$2y$12$AUNYn/u5lbjj9grNiGTTQuvtKZObRhhsbsWM9Szkzx15ZBQXldsa.', 'ROLE_USER', false);
