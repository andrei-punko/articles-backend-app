
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Alexander', 'Pushkin');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Mikhail', 'Prishvin');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Lev', 'Tolstoy');

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Voina i mir', 'Book summary', 'some text...', 3, current_timestamp, current_timestamp);

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Poltava', 'Poltava book summary', 'some text...', 1, current_timestamp, current_timestamp);
