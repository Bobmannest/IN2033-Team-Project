CREATE DATABASE IF NOT EXISTS ipos_pu;
USE ipos_pu;

CREATE TABLE IF NOT EXISTS Member (
    account_no VARCHAR(10) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    member_type ENUM('non_commercial' , 'commercial') NOT NULL DEFAULT 'non_commercial',
    is_first_login BOOLEAN NOT NULL DEFAULT TRUE,
    order_count INT NOT NULL DEFAULT 0
);