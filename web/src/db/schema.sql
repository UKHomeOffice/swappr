drop database if exists swappr;
create database swappr;
grant usage on *.* to swappr@localhost identified by 'swappr';
grant all privileges on swappr.* to swappr@localhost;
use swappr;

create table users (
    username varchar(50) not null primary key,
    password varchar(250) not null,
    enabled boolean not null
) engine = InnoDb;

create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    foreign key (username) references users (username),
    unique index authorities_idx_1 (username, authority)
) engine = InnoDb;

create table persistent_logins (
    username varchar(64) not null,
    series varchar(64) primary key,
    token varchar(64) not null,
    last_used timestamp not null
) engine = InnoDb;

create table swap (
  id int UNSIGNED NOT NULL AUTO_INCREMENT,
  username varchar(250) not null,
  fromDate date not null,
  fromShift varchar(50) not null,
  toDate date not null,
  toShift varchar(50) not null,
  PRIMARY KEY(id),
  foreign key (username) references users (username)
) engine = InnoDb;