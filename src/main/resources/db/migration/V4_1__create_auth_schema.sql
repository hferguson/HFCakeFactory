create table authority (
uid varchar(255) not null primary key,
role varchar(50) not null default 'ROLE_GUEST'
)