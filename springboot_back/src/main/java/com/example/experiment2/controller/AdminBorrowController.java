package com.example.experiment2.controller;

import com.example.experiment2.common.PageResult;
import com.example.experiment2.common.Result;
import com.example.experiment2.entity.AdminBorrowRecord;
import com.example.experiment2.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/borrows")
public class AdminBorrowController {

    private final AdminService adminService;

    public AdminBorrowController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public Result<PageResult<AdminBorrowRecord>> list(@RequestParam(required = false) String username,
                                                      @RequestParam(required = false) String title,
                                                      @RequestParam(required = false) String status,
                                                      @RequestParam(required = false) String startTime,
                                                      @RequestParam(required = false) String endTime,
                                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest request) {
        return adminService.listBorrows(role(request), username, title, status, startTime, endTime, pageNum, pageSize);
    }

    private String role(HttpServletRequest request) {
        return (String) request.getAttribute("currentRole");
    }
}
