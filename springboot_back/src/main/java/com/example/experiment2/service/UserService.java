package com.example.experiment2.service;

import com.example.experiment2.common.Result;
import com.example.experiment2.dto.AuthResponse;
import com.example.experiment2.dto.LoginRequest;
import com.example.experiment2.dto.PasswordRequest;
import com.example.experiment2.dto.RegisterRequest;
import com.example.experiment2.entity.SysUser;

public interface UserService {

    Result<AuthResponse> login(LoginRequest request);

    Result<Void> register(RegisterRequest request);

    Result<Void> changePassword(PasswordRequest request, Long currentUserId);

    Result<SysUser> currentUser(Long currentUserId);
}
