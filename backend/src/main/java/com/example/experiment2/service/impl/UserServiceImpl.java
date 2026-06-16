package com.example.experiment2.service.impl;

import com.example.experiment2.common.Result;
import com.example.experiment2.dto.LoginRequest;
import com.example.experiment2.dto.PasswordRequest;
import com.example.experiment2.dto.RegisterRequest;
import com.example.experiment2.entity.LoginUser;
import com.example.experiment2.entity.UserInfo;
import com.example.experiment2.service.UserService;
import com.example.experiment2.store.MockUserStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final MockUserStore userStore;

    public UserServiceImpl(MockUserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public Result<UserInfo> login(LoginRequest request) {
        if (request == null) {
            return Result.fail("登录信息不能为空");
        }
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return Result.fail("用户名和密码不能为空");
        }

        Optional<LoginUser> loginUser = userStore.findLoginUser(request.getUsername());
        if (loginUser.isEmpty()) {
            return Result.fail("用户不存在");
        }
        if (!loginUser.get().getPassword().equals(request.getPassword())) {
            return Result.fail("密码错误");
        }

        return userStore.findUserInfo(request.getUsername())
                .map(user -> Result.success("登录成功", user))
                .orElseGet(() -> Result.fail("用户信息不存在"));
    }

    @Override
    public Result<UserInfo> register(RegisterRequest request) {
        if (request == null) {
            return Result.fail("注册信息不能为空");
        }
        if (isBlank(request.getUsername()) || isBlank(request.getPassword()) || isBlank(request.getEmail()) || isBlank(request.getBirthday())) {
            return Result.fail("注册信息不能为空");
        }
        if (userStore.findLoginUser(request.getUsername()).isPresent()) {
            return Result.fail("用户名已存在");
        }

        UserInfo userInfo = userStore.addUser(request.getUsername(), request.getPassword(), request.getEmail(), request.getBirthday());
        return Result.success("注册成功", userInfo);
    }

    @Override
    public Result<Void> changePassword(PasswordRequest request) {
        if (request == null) {
            return Result.fail("修改密码信息不能为空");
        }
        if (isBlank(request.getUsername()) || isBlank(request.getEmail()) || isBlank(request.getNewPassword()) || isBlank(request.getConfirmPassword())) {
            return Result.fail("修改密码信息不能为空");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return Result.fail("两次密码不一致");
        }

        Optional<LoginUser> loginUser = userStore.findLoginUser(request.getUsername());
        if (loginUser.isEmpty()) {
            return Result.fail("用户不存在");
        }
        if (!loginUser.get().getEmail().equals(request.getEmail())) {
            return Result.fail("邮箱与账号不匹配");
        }

        userStore.changePassword(loginUser.get(), request.getNewPassword());
        return Result.<Void>success("密码修改成功", null);
    }

    @Override
    public Result<List<UserInfo>> listUsers() {
        return Result.success(userStore.listUsers());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
