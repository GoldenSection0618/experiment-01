package com.example.experiment2.service.impl;

import com.example.experiment2.common.Result;
import com.example.experiment2.dto.AuthResponse;
import com.example.experiment2.dto.LoginRequest;
import com.example.experiment2.dto.PasswordRequest;
import com.example.experiment2.dto.RegisterRequest;
import com.example.experiment2.entity.SysUser;
import com.example.experiment2.mapper.SysUserMapper;
import com.example.experiment2.service.UserService;
import com.example.experiment2.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(SysUserMapper sysUserMapper, JwtUtil jwtUtil) {
        this.sysUserMapper = sysUserMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Result<AuthResponse> login(LoginRequest request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return Result.fail("用户名和密码不能为空");
        }

        SysUser user = sysUserMapper.findByUsername(request.getUsername());
        if (user == null) {
            return Result.fail("用户不存在");
        }
        if (!"NORMAL".equals(user.getStatus())) {
            return Result.fail("账号已被禁用");
        }
        if (!user.getPassword().equals(request.getPassword())) {
            return Result.fail("密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return Result.success("登录成功", new AuthResponse(token, user, user.getRole()));
    }

    @Override
    public Result<Void> register(RegisterRequest request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword()) || isBlank(request.getEmail())) {
            return Result.fail("注册信息不能为空");
        }
        if (request.getUsername().trim().length() < 3 || request.getUsername().trim().length() > 20) {
            return Result.fail("用户名长度应为 3 到 20 位");
        }
        if (request.getPassword().length() < 6) {
            return Result.fail("密码长度不能少于 6 位");
        }
        if (!request.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return Result.fail("邮箱格式不正确");
        }
        if (sysUserMapper.findByUsername(request.getUsername()) != null) {
            return Result.fail("用户名已存在");
        }
        if (sysUserMapper.findByEmail(request.getEmail()) != null) {
            return Result.fail("邮箱已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(request.getUsername().trim());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail().trim());
        user.setRole("USER");
        user.setStatus("NORMAL");
        sysUserMapper.insert(user);
        return Result.<Void>success("注册成功", null);
    }

    @Override
    public Result<Void> changePassword(PasswordRequest request, Long currentUserId) {
        if (currentUserId == null) {
            return Result.fail("登录状态已失效");
        }
        if (request == null || isBlank(request.getOldPassword()) || isBlank(request.getNewPassword()) || isBlank(request.getConfirmPassword())) {
            return Result.fail("修改密码信息不能为空");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return Result.fail("两次密码不一致");
        }
        if (request.getNewPassword().length() < 6) {
            return Result.fail("新密码长度不能少于 6 位");
        }

        SysUser user = sysUserMapper.findById(currentUserId);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        if (!user.getPassword().equals(request.getOldPassword())) {
            return Result.fail("旧密码错误");
        }

        int rows = sysUserMapper.updatePassword(currentUserId, request.getNewPassword());
        if (rows == 0) {
            return Result.fail("密码修改失败");
        }
        return Result.<Void>success("密码修改成功", null);
    }

    @Override
    public Result<SysUser> currentUser(Long currentUserId) {
        if (currentUserId == null) {
            return Result.fail("登录状态已失效");
        }
        SysUser user = sysUserMapper.findById(currentUserId);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        return Result.success(user);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
