CREATE DATABASE IF NOT EXISTS lecture;
USE lecture;
create table attender (attender_number varchar(5), attender_id bigint not null auto_increment, created_at DATETIME, updated_at DATETIME, name varchar(255), tel varchar(255), status enum ('DELETED','REGISTER'), primary key (attender_id)) engine=InnoDB;
create table instructor (created_at DATETIME, instructor_id bigint not null auto_increment, updated_at DATETIME, name varchar(255), phone varchar(255), primary key (instructor_id)) engine=InnoDB;
create table lecture (current_attendees integer not null, max_attendees integer not null, created_at DATETIME, instructor_id bigint, lecture_id bigint not null auto_increment, start_time DATETIME, updated_at DATETIME, venue_id bigint, description varchar(255), title varchar(255), status enum ('DELETED','REGISTER'), primary key (lecture_id)) engine=InnoDB;
create table lecture_application (attender_id bigint, created_at DATETIME, lecture_application_id bigint not null auto_increment, lecture_id bigint, updated_at DATETIME, status enum ('CANCELED','REGISTER'), primary key (lecture_application_id)) engine=InnoDB;
create table venue (created_at DATETIME, seat_count bigint, updated_at DATETIME, venue_id bigint not null auto_increment, address varchar(255), name varchar(255), primary key (venue_id)) engine=InnoDB;
alter table if exists attender add constraint attender_number_unique unique (attender_number);
alter table if exists lecture add constraint lecture_instructor_id foreign key (instructor_id) references instructor (instructor_id);
alter table if exists lecture add constraint lecture_venue_id foreign key (venue_id) references venue (venue_id);
alter table if exists lecture_application add constraint lecture_application_attender_id foreign key (attender_id) references attender (attender_id);
alter table if exists lecture_application add constraint lecture_application_lecture_id foreign key (lecture_id) references lecture (lecture_id);


