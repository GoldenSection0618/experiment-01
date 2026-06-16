package com.example.experiment2.controller;

import com.example.experiment2.common.PageResult;
import com.example.experiment2.common.Result;
import com.example.experiment2.entity.BookCategory;
import com.example.experiment2.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final AdminService adminService;

    public AdminCategoryController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public Result<PageResult<BookCategory>> list(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String status,
                                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest request) {
        return adminService.listCategories(role(request), name, status, pageNum, pageSize);
    }

    @GetMapping("/active")
    public Result<List<BookCategory>> active(HttpServletRequest request) {
        return adminService.activeCategories(role(request));
    }

    @PostMapping
    public Result<Void> save(@RequestBody BookCategory category, HttpServletRequest request) {
        return adminService.saveCategory(role(request), category);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody BookCategory category, HttpServletRequest request) {
        category.setId(id);
        return adminService.updateCategory(role(request), category);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status, HttpServletRequest request) {
        return adminService.updateCategoryStatus(role(request), id, status);
    }

    private String role(HttpServletRequest request) {
        return (String) request.getAttribute("currentRole");
    }
}
