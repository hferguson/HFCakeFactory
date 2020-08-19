create table address (
accountid varchar(255) primary key not null,
street varchar(255) not null,
city varchar(50) not null,
state_or_province varchar(2) not null,
postal_code varchar(7) not null
);

create table account (
uid varchar(255) primary key not null, 
first_name varchar(255) not null,
last_name varchar(255) not null,
passwd varchar(255) not null
);