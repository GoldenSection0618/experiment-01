package com.example.experiment2.controller;

import com.example.experiment2.common.Result;
import com.example.experiment2.dto.LoginRequest;
import com.example.experiment2.dto.PasswordRequest;
import com.example.experiment2.dto.RegisterRequest;
import com.example.experiment2.entity.UserInfo;
import com.example.experiment2.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<UserInfo> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/register")
    public Result<UserInfo> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/password")
    public Result<Void> changePassword(@RequestBody PasswordRequest request) {
        return userService.changePassword(request);
    }
}
