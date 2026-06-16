package com.example.experiment2.controller;

import com.example.experiment2.common.Result;
import com.example.experiment2.entity.UserInfo;
import com.example.experiment2.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Result<List<UserInfo>> listUsers() {
        return userService.listUsers();
    }
}
