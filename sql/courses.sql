DROP DATABASE IF EXISTS courses;
CREATE DATABASE `courses`;
USE `courses`;

CREATE TABLE `user` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    `login` VARCHAR(50) NOT NULL,
    `password` VARCHAR(50) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    secondName VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (`login`),
    KEY (`login`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `seller` (
    userLogin VARCHAR(50) NOT NULL,
    PRIMARY KEY (userLogin),
    FOREIGN KEY (userLogin) REFERENCES `user` (`login`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `buyer` (
    userLogin VARCHAR(50) NOT NULL,
    PRIMARY KEY (userLogin),
    FOREIGN KEY (userLogin) REFERENCES `user` (`login`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `auctioneer` (
    userLogin VARCHAR(50) NOT NULL,
    PRIMARY KEY (userLogin),
    FOREIGN KEY (userLogin) REFERENCES `user` (`login`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `category` (
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `lot` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    sellerLogin VARCHAR(50) NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    `name` VARCHAR(50) NOT NULL,
    description VARCHAR(350) NOT NULL,
    currentPrice INT(11) NOT NULL,
    minSellingPrice INT(11) NOT NULL,
    compulsorySalePrice INT(11) NOT NULL,
    lastBuyer VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (sellerLogin) REFERENCES `seller` (userLogin) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `bid` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    buyerlogin VARCHAR(50) NOT NULL,
    lotid INT(11) NOT NULL,
    price INT(11) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (buyerlogin, lotid),
    FOREIGN KEY (buyerlogin) REFERENCES `buyer` (userLogin) ON DELETE CASCADE,
    FOREIGN KEY (lotid) REFERENCES `lot` (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `session` (
    token VARCHAR(50) NOT NULL,
    userid INT(11) NOT NULL,
    PRIMARY KEY (token),
    FOREIGN KEY (userid) REFERENCES `user` (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `lot_category` (
    id INT(11) NOT NULL AUTO_INCREMENT,
    lotid INT(11) NOT NULL,
    category VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (lotid) REFERENCES `lot` (id) ON DELETE CASCADE,
    FOREIGN KEY (category) REFERENCES `category` (`name`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `buyer_lot` (
    lotid INT(11) NOT NULL,
    buyerLogin VARCHAR(50) NOT NULL,
    PRIMARY KEY (lotid),
    FOREIGN KEY (buyerLogin) REFERENCES `buyer` (userLogin) ON DELETE CASCADE,
    FOREIGN KEY (lotid) REFERENCES `lot` (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

#Base init
INSERT `user` VALUES(null,"admin", "admin", "Jack", "Black", "Thomas");
INSERT `auctioneer` VALUES("admin");
INSERT `category` VALUES("продукты");
INSERT `category` VALUES("овощи");
INSERT `category` VALUES("фрукты");
INSERT `category` VALUES("коплектующие пк");
INSERT `category` VALUES("видеокарты");

#INSERT `lot_category` VALUES(null, 2, "gfg");
#select * from `category`;
#SELECT id, `login`, `password`, firstName, lastName, secondName FROM `user`;
#select * from `user`;
#select * from `auctioneer`;
#select * from `seller`;
#select * from `session`;
select * from `lot_category`;
select * from `bid`;
select * from `lot`;