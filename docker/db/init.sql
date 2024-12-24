CREATE DATABASE IF NOT EXISTS lecture;
USE lecture;
drop table if exists lecture_application;
drop table if exists lecture;
drop table if exists attender;
drop table if exists instructor;
drop table if exists venue;
create table attender (attender_number varchar(5), attender_id bigint not null auto_increment, created_at DATETIME, updated_at DATETIME, name varchar(255), tel varchar(255), status enum ('DELETED','REGISTER'), primary key (attender_id)) engine=InnoDB;
create table instructor (created_at DATETIME, instructor_id bigint not null auto_increment, updated_at DATETIME, name varchar(255), phone varchar(255), primary key (instructor_id)) engine=InnoDB;
create table lecture (current_attendees integer not null, max_attendees integer not null, created_at DATETIME, instructor_id bigint, lecture_id bigint not null auto_increment, start_time DATETIME, updated_at DATETIME, venue_id bigint, description varchar(255), title varchar(255), status enum ('DELETED','REGISTER'), primary key (lecture_id)) engine=InnoDB;
create table lecture_application (attender_id bigint, created_at DATETIME, lecture_application_id bigint not null auto_increment, lecture_id bigint, updated_at DATETIME, status enum ('CANCELED','REGISTER'), primary key (lecture_application_id)) engine=InnoDB;
create table venue (created_at DATETIME, seat_count bigint, updated_at DATETIME, venue_id bigint not null auto_increment, address varchar(255), name varchar(255), primary key (venue_id)) engine=InnoDB;
alter table attender add constraint attender_number_unique unique (attender_number);
alter table lecture add constraint lecture_instructor_id foreign key (instructor_id) references instructor (instructor_id);
alter table lecture add constraint lecture_venue_id foreign key (venue_id) references venue (venue_id);


