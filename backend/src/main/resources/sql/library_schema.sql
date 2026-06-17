DROP DATABASE IF EXISTS experiment_01_library;
CREATE DATABASE experiment_01_library DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE experiment_01_library;

CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '登录密码',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    role VARCHAR(20) NOT NULL COMMENT '用户角色：USER普通用户，ADMIN管理员',
    status VARCHAR(20) NOT NULL COMMENT '账号状态：NORMAL正常，DISABLED禁用',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT chk_sys_user_role CHECK (role IN ('USER', 'ADMIN')),
    CONSTRAINT chk_sys_user_status CHECK (status IN ('NORMAL', 'DISABLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

CREATE TABLE book_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    description VARCHAR(255) COMMENT '分类说明',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '分类状态：ACTIVE启用，DISABLED禁用',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT chk_book_category_status CHECK (status IN ('ACTIVE', 'DISABLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书分类表';

CREATE TABLE book (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '图书ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    title VARCHAR(100) NOT NULL COMMENT '书名',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    publisher VARCHAR(100) COMMENT '出版社',
    isbn VARCHAR(30) NOT NULL UNIQUE COMMENT 'ISBN编号',
    total_stock INT NOT NULL COMMENT '总库存',
    available_stock INT NOT NULL COMMENT '可借库存',
    status VARCHAR(20) NOT NULL DEFAULT 'ON_SHELF' COMMENT '图书状态：ON_SHELF上架，OFF_SHELF下架',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_book_category FOREIGN KEY (category_id) REFERENCES book_category(id),
    CONSTRAINT chk_book_stock CHECK (total_stock >= 0 AND available_stock >= 0 AND available_stock <= total_stock),
    CONSTRAINT chk_book_status CHECK (status IN ('ON_SHELF', 'OFF_SHELF'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书信息表';

CREATE INDEX idx_book_category_id ON book(category_id);
CREATE INDEX idx_book_title ON book(title);
CREATE INDEX idx_book_status ON book(status);

CREATE TABLE borrow_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规则ID',
    rule_name VARCHAR(50) NOT NULL COMMENT '规则名称',
    borrow_days INT NOT NULL COMMENT '可借天数',
    renew_days INT NOT NULL COMMENT '续借天数',
    max_renew_count INT NOT NULL COMMENT '最大续借次数',
    fine_per_day DECIMAL(10,2) NOT NULL COMMENT '每日逾期罚金',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '规则状态：ACTIVE启用，DISABLED禁用',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT chk_borrow_rule_number CHECK (borrow_days > 0 AND renew_days > 0 AND max_renew_count >= 0 AND fine_per_day >= 0),
    CONSTRAINT chk_borrow_rule_status CHECK (status IN ('ACTIVE', 'DISABLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='借阅规则表';

CREATE TABLE borrow_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '借阅记录ID',
    user_id BIGINT NOT NULL COMMENT '借阅用户ID',
    book_id BIGINT NOT NULL COMMENT '图书ID',
    rule_id BIGINT NOT NULL COMMENT '借阅规则ID',
    borrow_time DATETIME NOT NULL COMMENT '借阅时间',
    due_time DATETIME NOT NULL COMMENT '应还时间',
    return_time DATETIME NULL COMMENT '实际归还时间',
    renew_count INT NOT NULL DEFAULT 0 COMMENT '已续借次数',
    overdue_days INT NOT NULL DEFAULT 0 COMMENT '逾期天数',
    fine_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '逾期罚金',
    status VARCHAR(20) NOT NULL COMMENT '借阅状态：BORROWED借阅中，OVERDUE已逾期，RETURNED已归还',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_borrow_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_borrow_book FOREIGN KEY (book_id) REFERENCES book(id),
    CONSTRAINT fk_borrow_rule FOREIGN KEY (rule_id) REFERENCES borrow_rule(id),
    CONSTRAINT chk_borrow_record_number CHECK (renew_count >= 0 AND overdue_days >= 0 AND fine_amount >= 0),
    CONSTRAINT chk_borrow_record_status CHECK (status IN ('BORROWED', 'OVERDUE', 'RETURNED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='借阅记录表';

CREATE INDEX idx_borrow_user_id ON borrow_record(user_id);
CREATE INDEX idx_borrow_book_id ON borrow_record(book_id);
CREATE INDEX idx_borrow_rule_id ON borrow_record(rule_id);
CREATE INDEX idx_borrow_status ON borrow_record(status);
CREATE INDEX idx_borrow_due_time ON borrow_record(due_time);
CREATE INDEX idx_borrow_user_book_status ON borrow_record(user_id, book_id, status);

INSERT INTO sys_user (username, password, email, role, status) VALUES
('admin', '123456', 'admin@example.com', 'ADMIN', 'NORMAL'),
('student', '123456', 'student@example.com', 'USER', 'NORMAL'),
('badguy', '123456', 'badguy@example.com', 'USER', 'NORMAL');

INSERT INTO book_category (name, description, status) VALUES
('计算机', '计算机技术、软件开发和网络相关图书', 'ACTIVE'),
('文学', '小说、散文、诗歌等文学作品', 'ACTIVE'),
('经济管理', '经济、金融、管理和市场相关图书', 'ACTIVE');

INSERT INTO book (category_id, title, author, publisher, isbn, total_stock, available_stock, status) VALUES
(1, 'Java 核心技术', 'Cay S. Horstmann', '机械工业出版社', '9787111553850', 5, 5, 'ON_SHELF'),
(1, '深入理解计算机系统', 'Randal E. Bryant', '机械工业出版社', '9787111544933', 3, 3, 'ON_SHELF'),
(1, '数据库系统概念', 'Abraham Silberschatz', '机械工业出版社', '9787111375296', 4, 4, 'ON_SHELF'),
(2, '平凡的世界', '路遥', '北京十月文艺出版社', '9787530216787', 6, 5, 'ON_SHELF'),
(3, '管理学', 'Stephen P. Robbins', '中国人民大学出版社', '9787300286545', 2, 2, 'ON_SHELF');

INSERT INTO borrow_rule (rule_name, borrow_days, renew_days, max_renew_count, fine_per_day, status)
VALUES ('默认借阅规则', 30, 15, 1, 0.50, 'ACTIVE');

INSERT INTO borrow_record (
    user_id, book_id, rule_id, borrow_time, due_time, return_time,
    renew_count, overdue_days, fine_amount, status
) VALUES
(3, 4, 1, DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NULL, 0, 10, 5.00, 'OVERDUE'),
(3, 5, 1, DATE_SUB(NOW(), INTERVAL 38 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 6, 3.00, 'RETURNED');

CREATE VIEW book_stock_consistency AS
SELECT
    b.id AS book_id,
    b.title AS book_title,
    b.total_stock,
    b.available_stock,
    b.total_stock - b.available_stock AS borrowed_stock,
    COALESCE(active_record.active_count, 0) AS active_borrow_count,
    CASE
        WHEN b.total_stock - b.available_stock = COALESCE(active_record.active_count, 0) THEN 1
        ELSE 0
    END AS is_consistent
FROM book b
LEFT JOIN (
    SELECT book_id, COUNT(*) AS active_count
    FROM borrow_record
    WHERE status IN ('BORROWED', 'OVERDUE')
    GROUP BY book_id
) active_record ON b.id = active_record.book_id;

DELIMITER //
CREATE PROCEDURE assert_book_stock_consistency()
BEGIN
    IF EXISTS (
        SELECT 1
        FROM book_stock_consistency
        WHERE is_consistent = 0
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'book stock is inconsistent with active borrow records';
    END IF;
END//
DELIMITER ;

CALL assert_book_stock_consistency();
