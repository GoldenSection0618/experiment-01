package com.example.experiment2.store;

import com.example.experiment2.entity.LoginUser;
import com.example.experiment2.entity.UserInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MockUserStore {

    private final List<LoginUser> loginUsers = new ArrayList<>();
    private final List<UserInfo> users = new ArrayList<>();
    private int nextId = 11;

    public MockUserStore() {
        addSeedUser(1, "zhangsan", "123456", "张三", "男", 20, "zhangsan@example.com", "湖北武汉", "2006-05", "2026-01-15");
        addSeedUser(2, "lisi", "123456", "李四", "女", 22, "lisi@example.com", "北京朝阳", "2004-08", "2026-02-03");
        addSeedUser(3, "wangwu", "123456", "王五", "男", 25, "wangwu@example.com", "上海浦东", "2001-11", "2026-02-18");
        addSeedUser(4, "zhaoliu", "123456", "赵六", "女", 19, "zhaoliu@example.com", "广东广州", "2007-03", "2026-03-06");
        addSeedUser(5, "sunqi", "123456", "孙七", "男", 21, "sunqi@example.com", "浙江杭州", "2005-09", "2026-03-19");
        addSeedUser(6, "zhouba", "123456", "周八", "女", 23, "zhouba@example.com", "江苏南京", "2003-12", "2026-04-02");
        addSeedUser(7, "wujiu", "123456", "吴九", "男", 24, "wujiu@example.com", "四川成都", "2002-07", "2026-04-12");
        addSeedUser(8, "zhengshi", "123456", "郑十", "女", 20, "zhengshi@example.com", "重庆渝北", "2006-01", "2026-04-26");
        addSeedUser(9, "fengshiyi", "123456", "冯十一", "男", 26, "fengshiyi@example.com", "陕西西安", "2000-06", "2026-05-05");
        addSeedUser(10, "chenshier", "123456", "陈十二", "女", 18, "chenshier@example.com", "湖南长沙", "2008-02", "2026-05-16");
    }

    public Optional<LoginUser> findLoginUser(String username) {
        return loginUsers.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<UserInfo> findUserInfo(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public List<UserInfo> listUsers() {
        return new ArrayList<>(users);
    }

    public UserInfo addUser(String username, String password, String email, String birthday) {
        int id = nextId++;
        LoginUser loginUser = new LoginUser(id, username, password, email, birthday);
        UserInfo userInfo = new UserInfo(id, username, username, "未填写", 0, email, "未填写", birthday, LocalDate.now().toString());
        loginUsers.add(loginUser);
        users.add(userInfo);
        return userInfo;
    }

    public void changePassword(LoginUser user, String newPassword) {
        user.setPassword(newPassword);
    }

    private void addSeedUser(Integer id, String username, String password, String name, String gender, Integer age, String email, String address, String birthday, String registerTime) {
        loginUsers.add(new LoginUser(id, username, password, email, birthday));
        users.add(new UserInfo(id, username, name, gender, age, email, address, birthday, registerTime));
    }
}
