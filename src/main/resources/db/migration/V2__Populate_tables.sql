insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Тихон', 'Задонский');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Федор', 'Достоевский');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Николай', 'Гоголь');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Николай', 'Пестов');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Сергей', 'Нилус');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Игнатий', 'Брянчанинов');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Георгий', 'Максимов');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Даниил', 'Сысоев');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Иоанн', 'Сергиев');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Исаак', 'Сирский');
insert into AUTHOR (ID, FIRST_NAME, LAST_NAME) values (nextval('AUTHOR_ID_SEQ'), 'Авва', 'Дорофей');

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Игрок', 'Рассказ о страсти игромании', '', 2, current_timestamp, current_timestamp);

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Преступление и наказание', 'Рассказ о убийце и его раскаянии', '', 2, current_timestamp, current_timestamp);

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Келейные письма', 'Сборник писем', '', 1, current_timestamp, current_timestamp);

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Ревизор', '', '', 3, current_timestamp, current_timestamp);

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Моя жизнь во Христе', 'Дневниковые записи Иоанна Кронштадтского', '', 9, current_timestamp, current_timestamp);

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Слова подвижнические', '', '', 10, current_timestamp, current_timestamp);

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Современная практика православного благочестия', 'Руководство к духовной жизни', '', 4, current_timestamp, current_timestamp);

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Великое в малом', '', '', 5, current_timestamp, current_timestamp);

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Отечник', 'Выписки из святых отцов', '', 6, current_timestamp, current_timestamp);

insert into ARTICLE (ID, TITLE, SUMMARY, TEXT, AUTHOR_ID, DATE_CREATED, DATE_UPDATED)
values (nextval('ARTICLE_ID_SEQ'), 'Душеполезные поучения', 'Азбука духовной жизни', '', 11, current_timestamp, current_timestamp);
