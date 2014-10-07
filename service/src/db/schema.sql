drop database if exists swappr;
create database swappr;
grant usage on *.* to swappr@localhost identified by 'swappr';
grant all privileges on swappr.* to swappr@localhost;
use swappr;

create table users (
id INT UNSIGNED NOT NULL AUTO_INCREMENT,
name varchar(250),
password varchar(250),
role varchar(50),
PRIMARY KEY(id)
)engine=InnoDB;