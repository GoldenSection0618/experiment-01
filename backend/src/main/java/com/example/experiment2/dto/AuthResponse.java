package com.example.experiment2.dto;

import com.example.experiment2.entity.SysUser;

public class AuthResponse {

    private String token;
    private SysUser userInfo;
    private String role;

    public AuthResponse() {
    }

    public AuthResponse(String token, SysUser userInfo, String role) {
        this.token = token;
        this.userInfo = userInfo;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SysUser getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SysUser userInfo) {
        this.userInfo = userInfo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
