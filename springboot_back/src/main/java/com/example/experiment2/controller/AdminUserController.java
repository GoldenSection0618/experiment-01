package com.example.experiment2.controller;

import com.example.experiment2.common.PageResult;
import com.example.experiment2.common.Result;
import com.example.experiment2.entity.SysUser;
import com.example.experiment2.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminService adminService;

    public AdminUserController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public Result<PageResult<SysUser>> list(@RequestParam(required = false) String username,
                                            @RequestParam(required = false) String email,
                                            @RequestParam(required = false) String role,
                                            @RequestParam(required = false) String status,
                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            HttpServletRequest request) {
        return adminService.listUsers(role(request), username, email, role, status, pageNum, pageSize);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status, HttpServletRequest request) {
        return adminService.updateUserStatus(role(request), id, status);
    }

    private String role(HttpServletRequest request) {
        return (String) request.getAttribute("currentRole");
    }
}
