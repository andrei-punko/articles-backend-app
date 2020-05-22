
create table authors (
    id int not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    primary key (id)
);

create sequence author_id_seq START WITH 1 INCREMENT BY 1;

create table articles (
    id int not null,
    title varchar(100) not null,
    summary varchar(255),
    ts timestamp,
    text varchar not null,
    author_id int not null,
    date_created timestamp not null,
    date_updated timestamp not null,
    primary key (id),
    foreign key (author_id) references authors(id)
);

create sequence article_id_seq START WITH 1 INCREMENT BY 1;

create index idx_article_title on articles(title);
create index idx_article_date_created on articles(date_created);
create index ts_idx on articles(ts);
