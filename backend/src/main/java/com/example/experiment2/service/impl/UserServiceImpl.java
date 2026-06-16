package com.example.experiment2.service.impl;

import com.example.experiment2.common.Result;
import com.example.experiment2.dto.LoginRequest;
import com.example.experiment2.dto.PasswordRequest;
import com.example.experiment2.dto.RegisterRequest;
import com.example.experiment2.entity.LoginUser;
import com.example.experiment2.entity.UserInfo;
import com.example.experiment2.mapper.LoginUserMapper;
import com.example.experiment2.mapper.UserInfoMapper;
import com.example.experiment2.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final LoginUserMapper loginUserMapper;
    private final UserInfoMapper userInfoMapper;

    public UserServiceImpl(LoginUserMapper loginUserMapper, UserInfoMapper userInfoMapper) {
        this.loginUserMapper = loginUserMapper;
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public Result<UserInfo> login(LoginRequest request) {
        if (request == null) {
            return Result.fail("登录信息不能为空");
        }
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return Result.fail("用户名和密码不能为空");
        }

        LoginUser loginUser = loginUserMapper.findByUsername(request.getUsername());
        if (loginUser == null) {
            return Result.fail("用户不存在");
        }
        if (!loginUser.getPassword().equals(request.getPassword())) {
            return Result.fail("密码错误");
        }

        UserInfo userInfo = userInfoMapper.findByName(request.getUsername());
        if (userInfo == null) {
            userInfo = new UserInfo(LocalDate.now().toString(), request.getUsername(), "未填写", "未填写", "未填写", "未填写");
            userInfoMapper.insert(userInfo);
        }
        return Result.success("登录成功", userInfo);
    }

    @Override
    public Result<UserInfo> register(RegisterRequest request) {
        if (request == null) {
            return Result.fail("注册信息不能为空");
        }
        if (isBlank(request.getUsername()) || isBlank(request.getPassword()) || isBlank(request.getEmail()) || isBlank(request.getBirthday())) {
            return Result.fail("注册信息不能为空");
        }
        if (loginUserMapper.findByUsername(request.getUsername()) != null) {
            return Result.fail("用户名已存在");
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(request.getUsername());
        loginUser.setPassword(request.getPassword());
        loginUser.setEmail(request.getEmail());
        loginUser.setBirthday(normalizeBirthday(request.getBirthday()));
        loginUser.setMoney(0F);
        loginUser.setAvatar("");
        loginUserMapper.insert(loginUser);

        UserInfo userInfo = new UserInfo(LocalDate.now().toString(), request.getUsername(), "未填写", "未填写", "未填写", "未填写");
        userInfoMapper.insert(userInfo);
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

        LoginUser loginUser = loginUserMapper.findByUsername(request.getUsername());
        if (loginUser == null) {
            return Result.fail("用户不存在");
        }
        if (!loginUser.getEmail().equals(request.getEmail())) {
            return Result.fail("邮箱与账号不匹配");
        }

        loginUserMapper.updatePassword(request.getUsername(), request.getNewPassword());
        return Result.<Void>success("密码修改成功", null);
    }

    @Override
    public Result<List<UserInfo>> listUsers() {
        return Result.success(userInfoMapper.findAll());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalizeBirthday(String birthday) {
        if (birthday != null && birthday.length() == 7) {
            return birthday + "-01 00:00:00";
        }
        return birthday;
    }
}
