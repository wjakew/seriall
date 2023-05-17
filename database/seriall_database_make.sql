/**
version: 100
date: 16.05.2023
file for creating seriall_database and tables
made by Jakub Wawak 2022
kubawawak@gmail.com / j.wawak@usp.pl
all rights reserved
**/

SET character_set_server = 'utf8mb3';
CREATE DATABASE IF NOT EXISTS seriall_database CHARACTER SET utf8mb3; -- creating the database
USE seriall_database; -- using database to load tables

CREATE TABLE SERIALL_OBJECT -- table for storing data entry
(
    seriall_object_id INT PRIMARY KEY AUTO_INCREMENT,
    seriall_object_time TIMESTAMP,
    seriall_object_data VARCHAR(350)
);

CREATE TABLE SERIALL_SESSION -- table for storing program sessions
(
	seriall_session_id INT PRIMARY KEY AUTO_INCREMENT,
    seriall_session_time_start TIMESTAMP,
    seriall_session_time_end TIMESTAMP,
    seriall_session_desc VARCHAR(300),
    seriall_session_obj LONGBLOB
);

CREATE TABLE SERIALL_APPLOG -- table for storing application log
(
    seriall_applog_id INT PRIMARY KEY AUTO_INCREMENT,
    seriall_applog_code VARCHAR(100),
    seriall_applog_desc TEXT,
    seriall_applog_time TIMESTAMP
);