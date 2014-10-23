
insert into users (username, password, enabled) values ('admin', 'f8f0cf7ba6486ae73858572eeff1830e67978d0426e7b894ada42e8d0af38a7658a1d1a8ce81ff2e', true);
insert into authorities (username, authority) values ('admin', 'ROLE_ADMIN');
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
