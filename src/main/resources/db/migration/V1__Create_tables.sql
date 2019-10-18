
create table AUTHOR (
    ID int not null,
    FIRST_NAME varchar(50) not null,
    LAST_NAME varchar(50) not null,
    primary key (ID)
);

create sequence AUTHOR_ID_SEQ START WITH 1 INCREMENT BY 1 NOCYCLE;

create table ARTICLE (
    ID int not null,
    TITLE varchar(100) not null,
    SUMMARY varchar(255) not null,
    TEXT varchar(50) not null,
    AUTHOR_ID int not null,
    DATE_CREATED datetime not null,
    DATE_UPDATED datetime not null,
    primary key (ID),
    foreign key (AUTHOR_ID) references AUTHOR(ID)
);

create sequence ARTICLE_ID_SEQ START WITH 1 INCREMENT BY 1 NOCYCLE;
