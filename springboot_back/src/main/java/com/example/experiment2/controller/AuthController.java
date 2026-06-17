package com.example.experiment2.controller;

import com.example.experiment2.common.Result;
import com.example.experiment2.dto.AuthResponse;
import com.example.experiment2.dto.LoginRequest;
import com.example.experiment2.dto.PasswordRequest;
import com.example.experiment2.dto.RegisterRequest;
import com.example.experiment2.entity.SysUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
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
    public Result<AuthResponse> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/password")
    public Result<Void> changePassword(@RequestBody PasswordRequest request, HttpServletRequest servletRequest) {
        return userService.changePassword(request, currentUserId(servletRequest));
    }

    @GetMapping("/me")
    public Result<SysUser> me(HttpServletRequest request) {
        return userService.currentUser(currentUserId(request));
    }

    private Long currentUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("currentUserId");
    }
}
