insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Тихон', 'Задонский');
insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Федор', 'Достоевский');
insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Николай', 'Гоголь');
insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Николай', 'Пестов');
insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Сергей', 'Нилус');
insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Игнатий', 'Брянчанинов');
insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Георгий', 'Максимов');
insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Даниил', 'Сысоев');
insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Иоанн', 'Сергиев');
insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Исаак', 'Сирский');
insert into authors (id, first_name, last_name) values (nextval('author_id_seq'), 'Авва', 'Дорофей');

insert into articles (id, title, summary, text, author_id, date_created, date_updated)
values (nextval('ARTICLE_ID_SEQ'), 'Игрок', 'Рассказ о страсти игромании', '', 2, current_timestamp, current_timestamp);

insert into articles (id, title, summary, text, author_id, date_created, date_updated)
values (nextval('ARTICLE_ID_SEQ'), 'Преступление и наказание', 'Рассказ о убийце и его раскаянии', '', 2, current_timestamp, current_timestamp);

insert into articles (id, title, summary, text, author_id, date_created, date_updated)
values (nextval('ARTICLE_ID_SEQ'), 'Келейные письма', 'Сборник писем', '', 1, current_timestamp, current_timestamp);

insert into articles (id, title, summary, text, author_id, date_created, date_updated)
values (nextval('ARTICLE_ID_SEQ'), 'Ревизор', '', '', 3, current_timestamp, current_timestamp);

insert into articles (id, title, summary, text, author_id, date_created, date_updated)
values (nextval('ARTICLE_ID_SEQ'), 'Моя жизнь во Христе', 'Дневниковые записи Иоанна Кронштадтского', '', 9, current_timestamp, current_timestamp);

insert into articles (id, title, summary, text, author_id, date_created, date_updated)
values (nextval('ARTICLE_ID_SEQ'), 'Слова подвижнические', '', '', 10, current_timestamp, current_timestamp);

insert into articles (id, title, summary, text, author_id, date_created, date_updated)
values (nextval('ARTICLE_ID_SEQ'), 'Современная практика православного благочестия', 'Руководство к духовной жизни', '', 4, current_timestamp, current_timestamp);

insert into articles (id, title, summary, text, author_id, date_created, date_updated)
values (nextval('ARTICLE_ID_SEQ'), 'Великое в малом', '', '', 5, current_timestamp, current_timestamp);

insert into articles (id, title, summary, text, author_id, date_created, date_updated)
values (nextval('ARTICLE_ID_SEQ'), 'Отечник', 'Выписки из святых отцов', '', 6, current_timestamp, current_timestamp);

insert into articles (id, title, summary, text, author_id, date_created, date_updated)
values (nextval('ARTICLE_ID_SEQ'), 'Душеполезные поучения', 'Азбука духовной жизни', '', 11, current_timestamp, current_timestamp);
