
insert into users (username, password, enabled, email, fullname) values ('admin', 'f8f0cf7ba6486ae73858572eeff1830e67978d0426e7b894ada42e8d0af38a7658a1d1a8ce81ff2e', true, 'admin@mail.com', 'Administrator');
insert into authorities (username, authority) values ('admin', 'ADMIN');
insert into users (username, password, enabled, email, fullname) values ('Bill', 'password', true, 'bill@mail.com', 'Bill Beetroot');
insert into authorities (username, authority) values ('Bill', 'USER');
insert into users (username, password, enabled, email, fullname) values ('Ben', 'password', true, 'ben@mail.com', 'Ben Bernanke');
insert into authorities (username, authority) values ('Ben', 'USER');

insert into shiftType (code, description) values ('B1H', 'Early');
insert into shiftType (code, description) values ('BFH', 'Early');
insert into shiftType (code, description) values ('C1H', 'Mid');
insert into shiftType (code, description) values ('CFH', 'Mid');
insert into shiftType (code, description) values ('S1H', 'Late');
insert into shiftType (code, description) values ('T1H', 'Late');
