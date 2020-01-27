
create type USER_ROLE as enum ('ROLE_ADMIN', 'ROLE_USER');

create table SECURED_USERS (
    ID int not null,
    USERNAME varchar(50) not null,
    PASSWORD varchar(50) not null,
    ROLE USER_ROLE,
    ENABLED bool not null,
    primary key (ID)
);

create sequence SECURED_USER_ID_SEQ START WITH 1 INCREMENT BY 1;
