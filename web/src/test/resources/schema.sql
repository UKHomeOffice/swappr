create table users (
    username varchar(50) not null primary key,
    password varchar(250) not null,
    enabled boolean not null,
    email varchar(150) not null,
    fullname varchar(150) not null
);

create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    foreign key (username) references users (username)
);

create table persistent_logins (
    username varchar(64) not null,
    series varchar(64) primary key,
    token varchar(64) not null,
    last_used timestamp not null
);

create table shiftType (
  code        varchar(10) not null,
  description varchar(250) not null,
  primary     key (code)
);

create table rota (
  id          int GENERATED BY DEFAULT AS IDENTITY(START WITH 1001),
  shiftCode   varchar(10) not null,
  shiftDate   date not null,
  username    varchar(64) not null,
  primary     key (id),
  foreign     key (shiftCode) references shiftType (code),
  foreign     key (username) references users(username),
  unique      (username, shiftDate)
);


create table offer (
  id          int GENERATED BY DEFAULT AS IDENTITY(START WITH 1001),
  rotaId      int NOT NULL,
  shiftCode   varchar(10) not null,
  shiftDate   date not null,
  status      varchar(50) not null,
  primary     key (id),
  foreign     key (rotaId) references rota(id),
  foreign     key (shiftCode) references shiftType (code)
);

create table volunteer (
  id      int GENERATED BY DEFAULT AS IDENTITY(START WITH 1001),
  rotaId  int NOT NULL,
  offerId int NOT NULL,
  status  varchar(50) not null,
  primary key (id),
  foreign key (rotaId) references rota(id),
  foreign key (offerId) references offer(id)
);