-- create table hibernate_sequence (
--     next_val bigint
-- ) engine=MyISAM;
--
-- insert into hibernate_sequence values (1);
-- insert into hibernate_sequence values (1);
--
-- CREATE TABLE user (
--     id BIGINT NOT NULL AUTO_INCREMENT,
--     password VARCHAR(64) not null,
--     username VARCHAR(64) not null unique,
--     primary key(id)
-- ) engine=MyISAM;
--
--
-- CREATE TABLE post (
--     id BIGINT NOT NULL AUTO_INCREMENT,
--     title VARCHAR(64) not null,
--     body VARCHAR(1024) not null,
--     postedAt VARCHAR(20) not null,
--     primary key(id)
-- ) engine=MyISAM;