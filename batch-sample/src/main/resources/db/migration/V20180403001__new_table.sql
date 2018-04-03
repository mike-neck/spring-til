create table if not exists teams (
  id bigint not null primary key ,
  identifier bigint not null ,
  name varchar(63) not null  ,
  constraint uk_teams_identifier unique key (identifier)
);

create index if not exists index_teams_name on teams (
  name asc 
);

create table if not exists users (
  id bigint not null primary key ,
  name varchar (31) not null 
);

create table if not exists team_member (
  user_id bigint not null ,
  team_id bigint not null ,
  constraint pk_team_member primary key (user_id, team_id)
);
