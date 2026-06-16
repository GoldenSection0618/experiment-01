CREATE DATABASE IF NOT EXISTS experiment_01 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE experiment_01;

DROP TABLE IF EXISTS user_info;
DROP TABLE IF EXISTS login_user;

CREATE TABLE login_user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255),
    birthday DATETIME,
    money FLOAT,
    avatar VARCHAR(45)
);

CREATE TABLE user_info (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    name VARCHAR(45) NOT NULL,
    province VARCHAR(45),
    city VARCHAR(45),
    address VARCHAR(45),
    zip VARCHAR(45)
);
