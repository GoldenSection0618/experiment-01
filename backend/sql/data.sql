USE experiment_01;

INSERT INTO login_user (name, password, email, birthday, money, avatar) VALUES
('zhangsan', '123456', 'zhangsan@example.com', '2006-05-01 00:00:00', 100.0, ''),
('lisi', '123456', 'lisi@example.com', '2004-08-01 00:00:00', 80.0, ''),
('wangwu', '123456', 'wangwu@example.com', '2001-11-01 00:00:00', 60.0, '');

INSERT INTO user_info (date, name, province, city, address, zip) VALUES
('2026-01-15', 'zhangsan', '湖北省', '武汉市', '洪山区珞喻路', '430070'),
('2026-02-03', 'lisi', '北京市', '北京市', '朝阳区建国路', '100020'),
('2026-02-18', 'wangwu', '上海市', '上海市', '浦东新区世纪大道', '200120'),
('2026-03-06', 'zhaoliu', '广东省', '广州市', '天河区体育西路', '510620'),
('2026-03-19', 'sunqi', '浙江省', '杭州市', '西湖区文三路', '310012'),
('2026-04-02', 'zhouba', '江苏省', '南京市', '玄武区中山东路', '210018');
