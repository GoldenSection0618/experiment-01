package com.example.experiment2.service;

import com.example.experiment2.common.Result;
import com.example.experiment2.dto.LoginRequest;
import com.example.experiment2.dto.PasswordRequest;
import com.example.experiment2.dto.RegisterRequest;
import com.example.experiment2.entity.UserInfo;

import java.util.List;

public interface UserService {

    Result<UserInfo> login(LoginRequest request);

    Result<UserInfo> register(RegisterRequest request);

    Result<Void> changePassword(PasswordRequest request);

    Result<List<UserInfo>> listUsers();
}
