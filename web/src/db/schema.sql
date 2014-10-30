drop database if exists swappr;
create database swappr;
grant usage on *.* to swappr@localhost identified by 'swappr';
grant all privileges on swappr.* to swappr@localhost;
use swappr;

create table users (
    username varchar(50) not null primary key,
    password varchar(250) not null,
    enabled boolean not null,
    email varchar(150) not null,
    fullname varchar(150) not null
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

create table shiftType (
  code varchar(10) not null,
  description varchar(250) not null,
  primary key (code)
) engine = InnoDb;

create table rota (
  id int UNSIGNED NOT NULL AUTO_INCREMENT,
  shiftCode varchar(10) not null,
  shiftDate date not null,
  username varchar(64) not null,
  primary key (id),
  foreign key (shiftCode) references shiftType(code),
  foreign key (username) references users(username)
) engine = InnoDb;

create table offer (
  id          int UNSIGNED NOT NULL AUTO_INCREMENT,
  rotaId      int UNSIGNED NOT NULL,
  shiftCode   varchar(10) not null,
  shiftDate   date not null,
  status      varchar(50) not null,
  primary     key (id),
  foreign     key (rotaId) references rota(id),
  foreign     key (shiftCode) references shiftType (code)
) engine = InnoDb;

create table volunteer (
  id int UNSIGNED NOT NULL AUTO_INCREMENT,
  rotaId  int UNSIGNED NOT NULL,
  offerId int UNSIGNED NOT NULL,
  status  varchar(50) not null,
  primary key (id),
  foreign key (rotaId) references rota(id),
  foreign key (offerId) references offer(id)
) engine = InnoDb;

