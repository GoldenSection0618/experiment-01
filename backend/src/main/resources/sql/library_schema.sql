DROP DATABASE IF EXISTS experiment_01_library;
CREATE DATABASE experiment_01_library DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE experiment_01_library;

CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_sys_user_role CHECK (role IN ('USER', 'ADMIN')),
    CONSTRAINT chk_sys_user_status CHECK (status IN ('NORMAL', 'DISABLED'))
);

CREATE TABLE book_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_book_category_status CHECK (status IN ('ACTIVE', 'DISABLED'))
);

CREATE TABLE book (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    publisher VARCHAR(100),
    isbn VARCHAR(30) NOT NULL UNIQUE,
    total_stock INT NOT NULL,
    available_stock INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ON_SHELF',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_book_category FOREIGN KEY (category_id) REFERENCES book_category(id),
    CONSTRAINT chk_book_stock CHECK (total_stock >= 0 AND available_stock >= 0 AND available_stock <= total_stock),
    CONSTRAINT chk_book_status CHECK (status IN ('ON_SHELF', 'OFF_SHELF'))
);

CREATE INDEX idx_book_category_id ON book(category_id);
CREATE INDEX idx_book_title ON book(title);
CREATE INDEX idx_book_status ON book(status);

CREATE TABLE borrow_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_name VARCHAR(50) NOT NULL,
    borrow_days INT NOT NULL,
    renew_days INT NOT NULL,
    max_renew_count INT NOT NULL,
    fine_per_day DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_borrow_rule_number CHECK (borrow_days > 0 AND renew_days > 0 AND max_renew_count >= 0 AND fine_per_day >= 0),
    CONSTRAINT chk_borrow_rule_status CHECK (status IN ('ACTIVE', 'DISABLED'))
);

CREATE TABLE borrow_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrow_time DATETIME NOT NULL,
    due_time DATETIME NOT NULL,
    return_time DATETIME NULL,
    renew_count INT NOT NULL DEFAULT 0,
    overdue_days INT NOT NULL DEFAULT 0,
    fine_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL,
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_borrow_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_borrow_book FOREIGN KEY (book_id) REFERENCES book(id),
    CONSTRAINT chk_borrow_record_number CHECK (renew_count >= 0 AND overdue_days >= 0 AND fine_amount >= 0),
    CONSTRAINT chk_borrow_record_status CHECK (status IN ('BORROWED', 'OVERDUE', 'RETURNED'))
);

CREATE INDEX idx_borrow_user_id ON borrow_record(user_id);
CREATE INDEX idx_borrow_book_id ON borrow_record(book_id);
CREATE INDEX idx_borrow_status ON borrow_record(status);
CREATE INDEX idx_borrow_due_time ON borrow_record(due_time);
CREATE INDEX idx_borrow_user_book_status ON borrow_record(user_id, book_id, status);

INSERT INTO sys_user (username, password, email, role, status) VALUES
('admin', '123456', 'admin@example.com', 'ADMIN', 'NORMAL'),
('student', '123456', 'student@example.com', 'USER', 'NORMAL');

INSERT INTO book_category (name, description, status) VALUES
('计算机', '计算机技术、软件开发和网络相关图书', 'ACTIVE'),
('文学', '小说、散文、诗歌等文学作品', 'ACTIVE'),
('经济管理', '经济、金融、管理和市场相关图书', 'ACTIVE');

INSERT INTO book (category_id, title, author, publisher, isbn, total_stock, available_stock, status) VALUES
(1, 'Java 核心技术', 'Cay S. Horstmann', '机械工业出版社', '9787111553850', 5, 5, 'ON_SHELF'),
(1, '深入理解计算机系统', 'Randal E. Bryant', '机械工业出版社', '9787111544933', 3, 3, 'ON_SHELF'),
(1, '数据库系统概念', 'Abraham Silberschatz', '机械工业出版社', '9787111375296', 4, 4, 'ON_SHELF'),
(2, '平凡的世界', '路遥', '北京十月文艺出版社', '9787530216787', 6, 6, 'ON_SHELF'),
(3, '管理学', 'Stephen P. Robbins', '中国人民大学出版社', '9787300286545', 2, 2, 'ON_SHELF');

INSERT INTO borrow_rule (rule_name, borrow_days, renew_days, max_renew_count, fine_per_day, status)
VALUES ('默认借阅规则', 30, 15, 1, 0.50, 'ACTIVE');
