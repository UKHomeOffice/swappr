use swappr;

insert into users (username, password, enabled, email, fullname) values ('bill', 'f8f0cf7ba6486ae73858572eeff1830e67978d0426e7b894ada42e8d0af38a7658a1d1a8ce81ff2e', true, 'bill@mail.com', 'Bill Beetroot');
insert into authorities (username, authority) values ('bill', 'USER');
insert into users (username, password, enabled, email, fullname) values ('ben', 'f8f0cf7ba6486ae73858572eeff1830e67978d0426e7b894ada42e8d0af38a7658a1d1a8ce81ff2e', true, 'ben@mail.com', 'Ben Bernanke');
insert into authorities (username, authority) values ('ben', 'USER');