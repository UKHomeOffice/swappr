
insert into users (username, password, enabled) values ('Bill', 'password', true);
insert into authorities (username, authority) values ('Bill', 'ROLE_USER');
insert into users (username, password, enabled) values ('Ben', 'password', true);
insert into authorities (username, authority) values ('Ben', 'ROLE_USER');

insert into shiftType (code, description) values ('B1H', 'Early');
insert into shiftType (code, description) values ('BFH', 'Early');
insert into shiftType (code, description) values ('C1H', 'Mid');
insert into shiftType (code, description) values ('CFH', 'Mid');
insert into shiftType (code, description) values ('S1H', 'Late');
insert into shiftType (code, description) values ('T1H', 'Late');
