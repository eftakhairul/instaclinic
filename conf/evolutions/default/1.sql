# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table appointment (
  schedule_id               integer,
  room_id                   integer)
;

create table room (
  id                        integer not null,
  constraint pk_room primary key (id))
;

create table schedule (
  id                        integer not null,
  start_time                timestamp,
  end_time                  timestamp,
  constraint pk_schedule primary key (id))
;

create table user (
  id                        bigint not null,
  username                  varchar(255),
  password                  varchar(255),
  create_date               timestamp,
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id))
;

create sequence room_seq;

create sequence schedule_seq;

create sequence user_seq;

alter table appointment add constraint fk_appointment_schedule_1 foreign key (schedule_id) references schedule (id) on delete restrict on update restrict;
create index ix_appointment_schedule_1 on appointment (schedule_id);
alter table appointment add constraint fk_appointment_room_2 foreign key (room_id) references room (id) on delete restrict on update restrict;
create index ix_appointment_room_2 on appointment (room_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists appointment;

drop table if exists room;

drop table if exists schedule;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists room_seq;

drop sequence if exists schedule_seq;

drop sequence if exists user_seq;

