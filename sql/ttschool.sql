DROP DATABASE IF EXISTS ttschool;
CREATE DATABASE `ttschool`;
USE `ttschool`;

CREATE TABLE subject (
    id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE school (
    id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    year INT(4) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (name, year)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `group` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    schoolid INT(11) NOT NULL,
    name VARCHAR(50) NOT NULL,
    room VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (schoolid) REFERENCES school (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE trainee (
    id INT(11) NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    rating INT(1),
    groupid INT(11),
    PRIMARY KEY (id),
    FOREIGN KEY (groupid) REFERENCES `group` (id) ON DELETE SET NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- дополнительная таблица со связью многие ко многим
CREATE TABLE group_subject (
    id INT(11) NOT NULL AUTO_INCREMENT,
    groupid INT(11) NOT NULL,
    subjectid INT(11) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (groupid) REFERENCES `group` (id) ON DELETE CASCADE,
    FOREIGN KEY (subjectid) REFERENCES subject (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

