
insert into secured_users (id, username, password, role, enabled) values (nextval('secured_user_id_seq'), 'Ivan', '{bcrypt}$2y$12$hkcB6greXd/nlLPzBVL8cu0kZA0vJFiPpp4szK/G2Bx3zHh5PR0di', 'ROLE_ADMIN', true);
insert into secured_users (id, username, password, role, enabled) values (nextval('secured_user_id_seq'), 'Vasily', '{bcrypt}$2y$12$xuRL6ncg2RVb4imAFKLOz.a3OAbxmIAYV5YfZrv2vVhm/26eS7V2C', 'ROLE_USER', true);
insert into secured_users (id, username, password, role, enabled) values (nextval('secured_user_id_seq'), 'Petr', '{bcrypt}$2y$12$5dYMkdF.Qn9J4azlMG1F1.XcSNrpNPcYd95ExjAYI3TrO8d/wPahW', 'ROLE_USER', true);
insert into secured_users (id, username, password, role, enabled) values (nextval('secured_user_id_seq'), 'Oleg', '{bcrypt}$2y$12$AUNYn/u5lbjj9grNiGTTQuvtKZObRhhsbsWM9Szkzx15ZBQXldsa.', 'ROLE_USER', false);
