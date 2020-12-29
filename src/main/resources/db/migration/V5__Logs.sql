
create table logs (
    id int not null,
    method_name varchar(100) not null,
    args varchar(1000) not null,
    result varchar(250) not null,
    result_type varchar(200),
    ts timestamp not null,
    is_succeed boolean default(false) not null,
    primary key (id)
);

create sequence logs_id_seq START WITH 1 INCREMENT BY 1;
