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
(1, 'Java 核心技术', 'Cay S. Horstmann', '机械工业出版社', 'LIB-CS-0001', 5, 5, 'ON_SHELF'),
(1, '深入理解计算机系统', 'Randal E. Bryant', '机械工业出版社', 'LIB-CS-0002', 3, 3, 'ON_SHELF'),
(1, '数据库系统概念', 'Abraham Silberschatz', '机械工业出版社', 'LIB-CS-0003', 4, 4, 'ON_SHELF'),
(2, '平凡的世界', '路遥', '北京十月文艺出版社', 'LIB-LIT-0004', 6, 5, 'ON_SHELF'),
(3, '管理学', 'Stephen P. Robbins', '中国人民大学出版社', 'LIB-MGT-0005', 2, 2, 'ON_SHELF'),
(1, '数据结构与算法分析', '李明远', '清华大学出版社', 'LIB-CS-0006', 4, 4, 'ON_SHELF'),
(1, '操作系统原理与实践', '周志强', '机械工业出版社', 'LIB-CS-0007', 5, 5, 'ON_SHELF'),
(1, '计算机网络基础', '陈思源', '电子工业出版社', 'LIB-CS-0008', 3, 3, 'ON_SHELF'),
(1, '软件工程方法论', '王立新', '人民邮电出版社', 'LIB-CS-0009', 6, 6, 'ON_SHELF'),
(1, 'Python 编程实践', '赵雨晨', '清华大学出版社', 'LIB-CS-0010', 4, 4, 'ON_SHELF'),
(1, 'Web 前端开发实战', '刘嘉宁', '电子工业出版社', 'LIB-CS-0011', 5, 5, 'ON_SHELF'),
(1, 'Spring Boot 企业开发', '孙浩然', '人民邮电出版社', 'LIB-CS-0012', 2, 2, 'ON_SHELF'),
(1, 'MyBatis 数据访问技术', '郭子昂', '机械工业出版社', 'LIB-CS-0013', 4, 4, 'ON_SHELF'),
(1, 'Vue 应用开发教程', '何佳怡', '电子工业出版社', 'LIB-CS-0014', 5, 5, 'ON_SHELF'),
(1, 'Linux 系统管理', '唐宇航', '清华大学出版社', 'LIB-CS-0015', 6, 6, 'ON_SHELF'),
(1, '分布式系统设计', '许文博', '机械工业出版社', 'LIB-CS-0016', 3, 3, 'ON_SHELF'),
(1, '微服务架构入门', '高俊峰', '人民邮电出版社', 'LIB-CS-0017', 4, 4, 'ON_SHELF'),
(1, '数据库设计与优化', '林泽宇', '电子工业出版社', 'LIB-CS-0018', 5, 5, 'ON_SHELF'),
(1, '信息安全基础', '郑一鸣', '清华大学出版社', 'LIB-CS-0019', 3, 3, 'ON_SHELF'),
(1, '人工智能导论', '黄思远', '机械工业出版社', 'LIB-CS-0020', 4, 4, 'ON_SHELF'),
(1, '机器学习基础', '吴晨曦', '人民邮电出版社', 'LIB-CS-0021', 5, 5, 'ON_SHELF'),
(1, '深度学习实践', '罗子涵', '电子工业出版社', 'LIB-CS-0022', 3, 3, 'ON_SHELF'),
(1, '数据挖掘方法', '马俊杰', '清华大学出版社', 'LIB-CS-0023', 4, 4, 'ON_SHELF'),
(1, '大数据平台技术', '胡嘉伟', '机械工业出版社', 'LIB-CS-0024', 5, 5, 'ON_SHELF'),
(1, '云计算原理', '蒋若愚', '人民邮电出版社', 'LIB-CS-0025', 2, 2, 'ON_SHELF'),
(1, '编译原理简明教程', '邓启航', '电子工业出版社', 'LIB-CS-0026', 4, 4, 'ON_SHELF'),
(1, '计算机组成原理', '沈卓然', '清华大学出版社', 'LIB-CS-0027', 5, 5, 'ON_SHELF'),
(1, 'C 语言程序设计', '韩子墨', '机械工业出版社', 'LIB-CS-0028', 3, 3, 'ON_SHELF'),
(1, 'C++ 面向对象程序设计', '彭嘉豪', '人民邮电出版社', 'LIB-CS-0029', 4, 4, 'ON_SHELF'),
(1, 'Java Web 应用开发', '谢雨桐', '电子工业出版社', 'LIB-CS-0030', 5, 5, 'ON_SHELF'),
(1, '算法竞赛入门', '袁启明', '清华大学出版社', 'LIB-CS-0031', 6, 6, 'ON_SHELF'),
(1, '移动应用开发基础', '梁若涵', '机械工业出版社', 'LIB-CS-0032', 3, 3, 'ON_SHELF'),
(1, '软件测试技术', '魏明轩', '人民邮电出版社', 'LIB-CS-0033', 4, 4, 'ON_SHELF'),
(1, '网络协议分析', '范子瑜', '电子工业出版社', 'LIB-CS-0034', 5, 5, 'ON_SHELF'),
(1, '系统设计案例解析', '戴文清', '清华大学出版社', 'LIB-CS-0035', 3, 3, 'ON_SHELF'),
(1, '计算机图形学基础', '宋天宇', '机械工业出版社', 'LIB-CS-0036', 4, 4, 'OFF_SHELF'),
(1, '人机交互设计', '白若琳', '人民邮电出版社', 'LIB-CS-0037', 5, 5, 'ON_SHELF'),
(1, '自然语言处理入门', '陆嘉树', '电子工业出版社', 'LIB-CS-0038', 2, 2, 'ON_SHELF'),
(1, '推荐系统实践', '钟一凡', '清华大学出版社', 'LIB-CS-0039', 4, 4, 'ON_SHELF'),
(1, '数据库事务处理', '邱子昂', '机械工业出版社', 'LIB-CS-0040', 5, 5, 'ON_SHELF'),
(1, '并发编程实战', '秦若海', '人民邮电出版社', 'LIB-CS-0041', 3, 3, 'ON_SHELF'),
(1, '软件项目管理', '顾思齐', '电子工业出版社', 'LIB-CS-0042', 4, 4, 'OFF_SHELF'),
(2, '边城', '沈从文', '人民文学出版社', 'LIB-LIT-0043', 6, 6, 'ON_SHELF'),
(2, '围城', '钱锺书', '人民文学出版社', 'LIB-LIT-0044', 5, 5, 'ON_SHELF'),
(2, '骆驼祥子', '老舍', '人民文学出版社', 'LIB-LIT-0045', 4, 4, 'ON_SHELF'),
(2, '朝花夕拾', '鲁迅', '人民文学出版社', 'LIB-LIT-0046', 7, 7, 'ON_SHELF'),
(2, '呼兰河传', '萧红', '江苏凤凰文艺出版社', 'LIB-LIT-0047', 3, 3, 'ON_SHELF'),
(2, '文化苦旅', '余秋雨', '作家出版社', 'LIB-LIT-0048', 5, 5, 'ON_SHELF'),
(2, '活着', '余华', '作家出版社', 'LIB-LIT-0049', 6, 6, 'ON_SHELF'),
(2, '许三观卖血记', '余华', '作家出版社', 'LIB-LIT-0050', 4, 4, 'ON_SHELF'),
(2, '长恨歌', '王安忆', '人民文学出版社', 'LIB-LIT-0051', 5, 5, 'ON_SHELF'),
(2, '白鹿原', '陈忠实', '人民文学出版社', 'LIB-LIT-0052', 3, 3, 'ON_SHELF'),
(2, '茶馆', '老舍', '人民文学出版社', 'LIB-LIT-0053', 4, 4, 'ON_SHELF'),
(2, '雷雨', '曹禺', '北京十月文艺出版社', 'LIB-LIT-0054', 5, 5, 'ON_SHELF'),
(2, '月亮与六便士', '毛姆', '译林出版社', 'LIB-LIT-0055', 6, 6, 'ON_SHELF'),
(2, '老人与海', '海明威', '上海译文出版社', 'LIB-LIT-0056', 4, 4, 'ON_SHELF'),
(2, '百年孤独', '加西亚·马尔克斯', '南海出版公司', 'LIB-LIT-0057', 3, 3, 'ON_SHELF'),
(2, '局外人', '阿尔贝·加缪', '上海译文出版社', 'LIB-LIT-0058', 5, 5, 'ON_SHELF'),
(2, '小王子', '圣埃克苏佩里', '人民文学出版社', 'LIB-LIT-0059', 7, 7, 'ON_SHELF'),
(2, '傲慢与偏见', '简·奥斯汀', '译林出版社', 'LIB-LIT-0060', 4, 4, 'ON_SHELF'),
(2, '简爱', '夏洛蒂·勃朗特', '上海译文出版社', 'LIB-LIT-0061', 5, 5, 'ON_SHELF'),
(2, '雾都孤儿', '查尔斯·狄更斯', '译林出版社', 'LIB-LIT-0062', 3, 3, 'OFF_SHELF'),
(2, '追风筝的人', '卡勒德·胡赛尼', '上海人民出版社', 'LIB-LIT-0063', 4, 4, 'ON_SHELF'),
(2, '岛上书店', '加布瑞埃拉·泽文', '江苏凤凰文艺出版社', 'LIB-LIT-0064', 5, 5, 'ON_SHELF'),
(2, '解忧杂货店', '东野圭吾', '南海出版公司', 'LIB-LIT-0065', 6, 6, 'ON_SHELF'),
(2, '挪威的森林', '村上春树', '上海译文出版社', 'LIB-LIT-0066', 4, 4, 'ON_SHELF'),
(2, '海边的卡夫卡', '村上春树', '上海译文出版社', 'LIB-LIT-0067', 3, 3, 'ON_SHELF'),
(2, '诗经选读', '佚名', '中华书局', 'LIB-LIT-0068', 5, 5, 'ON_SHELF'),
(2, '唐诗三百首', '蘅塘退士', '中华书局', 'LIB-LIT-0069', 6, 6, 'ON_SHELF'),
(2, '宋词鉴赏辞典', '夏承焘', '上海辞书出版社', 'LIB-LIT-0070', 4, 4, 'ON_SHELF'),
(2, '现代散文选', '林清玄', '作家出版社', 'LIB-LIT-0071', 5, 5, 'OFF_SHELF'),
(3, '经济学原理', 'N. Gregory Mankiw', '北京大学出版社', 'LIB-MGT-0072', 5, 5, 'ON_SHELF'),
(3, '宏观经济学', '高鸿业', '中国人民大学出版社', 'LIB-MGT-0073', 4, 4, 'ON_SHELF'),
(3, '微观经济学', '平新乔', '北京大学出版社', 'LIB-MGT-0074', 5, 5, 'ON_SHELF'),
(3, '金融学', '黄达', '中国人民大学出版社', 'LIB-MGT-0075', 6, 6, 'ON_SHELF'),
(3, '公司金融', '朱叶', '复旦大学出版社', 'LIB-MGT-0076', 3, 3, 'ON_SHELF'),
(3, '投资学', '滋维·博迪', '机械工业出版社', 'LIB-MGT-0077', 4, 4, 'ON_SHELF'),
(3, '货币银行学', '易纲', '格致出版社', 'LIB-MGT-0078', 5, 5, 'ON_SHELF'),
(3, '国际金融', '姜波克', '复旦大学出版社', 'LIB-MGT-0079', 3, 3, 'ON_SHELF'),
(3, '会计学基础', '刘永泽', '东北财经大学出版社', 'LIB-MGT-0080', 4, 4, 'ON_SHELF'),
(3, '财务管理', '荆新', '中国人民大学出版社', 'LIB-MGT-0081', 5, 5, 'ON_SHELF'),
(3, '市场营销学', '吴健安', '高等教育出版社', 'LIB-MGT-0082', 6, 6, 'ON_SHELF'),
(3, '组织行为学', '罗宾斯', '中国人民大学出版社', 'LIB-MGT-0083', 4, 4, 'ON_SHELF'),
(3, '战略管理', '徐二明', '中国人民大学出版社', 'LIB-MGT-0084', 3, 3, 'ON_SHELF'),
(3, '运营管理', '陈荣秋', '机械工业出版社', 'LIB-MGT-0085', 5, 5, 'ON_SHELF'),
(3, '人力资源管理', '彭剑锋', '中国人民大学出版社', 'LIB-MGT-0086', 4, 4, 'ON_SHELF'),
(3, '创业管理', '张玉利', '机械工业出版社', 'LIB-MGT-0087', 3, 3, 'ON_SHELF'),
(3, '项目管理', '哈罗德·科兹纳', '电子工业出版社', 'LIB-MGT-0088', 4, 4, 'ON_SHELF'),
(3, '管理信息系统', '薛华成', '清华大学出版社', 'LIB-MGT-0089', 5, 5, 'ON_SHELF'),
(3, '商业数据分析', '孟庆国', '清华大学出版社', 'LIB-MGT-0090', 6, 6, 'ON_SHELF'),
(3, '供应链管理', '马士华', '机械工业出版社', 'LIB-MGT-0091', 4, 4, 'ON_SHELF'),
(3, '消费者行为学', '迈克尔·所罗门', '中国人民大学出版社', 'LIB-MGT-0092', 3, 3, 'ON_SHELF'),
(3, '品牌管理', '卢泰宏', '高等教育出版社', 'LIB-MGT-0093', 4, 4, 'ON_SHELF'),
(3, '证券投资分析', '张亦春', '厦门大学出版社', 'LIB-MGT-0094', 5, 5, 'ON_SHELF'),
(3, '风险管理基础', '王周伟', '中国金融出版社', 'LIB-MGT-0095', 3, 3, 'ON_SHELF'),
(3, '计量经济学导论', '伍德里奇', '中国人民大学出版社', 'LIB-MGT-0096', 4, 4, 'ON_SHELF'),
(3, '产业经济学', '苏东水', '高等教育出版社', 'LIB-MGT-0097', 5, 5, 'ON_SHELF'),
(3, '数字经济概论', '李晓华', '经济管理出版社', 'LIB-MGT-0098', 6, 6, 'OFF_SHELF'),
(3, '行为金融学', '饶育蕾', '机械工业出版社', 'LIB-MGT-0099', 4, 4, 'ON_SHELF'),
(3, '管理沟通', '康青', '中国人民大学出版社', 'LIB-MGT-0100', 3, 3, 'ON_SHELF');

INSERT INTO borrow_rule (rule_name, borrow_days, renew_days, max_renew_count, fine_per_day, status)
VALUES ('默认借阅规则', 30, 15, 1, 0.50, 'ACTIVE');

INSERT INTO borrow_record (
    user_id, book_id, rule_id, borrow_time, due_time, return_time,
    renew_count, overdue_days, fine_amount, status
) VALUES
(3, 4, 1, DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NULL, 0, 10, 5.00, 'OVERDUE'),
(3, 5, 1, DATE_SUB(NOW(), INTERVAL 38 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 0, 6, 3.00, 'RETURNED');

-- 初始化脚本自带库存一致性检查，避免演示数据和未归还记录脱节。
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
