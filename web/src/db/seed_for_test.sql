use swappr;

insert into users (username, password, enabled) values ('bill', 'f8f0cf7ba6486ae73858572eeff1830e67978d0426e7b894ada42e8d0af38a7658a1d1a8ce81ff2e', true);
insert into authorities (username, authority) values ('bill', 'ROLE_USER');
insert into users (username, password, enabled) values ('ben', 'f8f0cf7ba6486ae73858572eeff1830e67978d0426e7b894ada42e8d0af38a7658a1d1a8ce81ff2e', true);
insert into authorities (username, authority) values ('ben', 'ROLE_USER');