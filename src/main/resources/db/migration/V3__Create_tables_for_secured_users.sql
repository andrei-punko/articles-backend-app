
create type user_role as enum ('ROLE_ADMIN', 'ROLE_USER');

create table secured_users (
    id int not null,
    username varchar(100) not null,
    password varchar(100) not null,
    role user_role,
    enabled bool not null,
    primary key (id)
);

create sequence secured_user_id_seq start with 1 increment by 1;
