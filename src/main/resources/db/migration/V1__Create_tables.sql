
create table AUTHOR (
    ID int not null,
    FIRST_NAME varchar(50) not null,
    LAST_NAME varchar(50) not null,
    primary key (ID)
);

create sequence AUTHOR_ID_SEQ START WITH 1 INCREMENT BY 1;

create table ARTICLE (
    ID int not null,
    TITLE varchar(100) not null,
    SUMMARY varchar(255),
    TEXT varchar not null,
    AUTHOR_ID int not null,
    DATE_CREATED timestamp not null,
    DATE_UPDATED timestamp not null,
    primary key (ID),
    foreign key (AUTHOR_ID) references AUTHOR(ID)
);

create sequence ARTICLE_ID_SEQ START WITH 1 INCREMENT BY 1;

create index IDX_ARTICLE_TITLE on ARTICLE (TITLE);
create index IDX_ARTICLE_DATE_CREATED on ARTICLE (DATE_CREATED);
