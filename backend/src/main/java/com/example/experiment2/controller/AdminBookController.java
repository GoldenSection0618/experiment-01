package com.example.experiment2.controller;

import com.example.experiment2.common.PageResult;
import com.example.experiment2.common.Result;
import com.example.experiment2.entity.Book;
import com.example.experiment2.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/books")
public class AdminBookController {

    private final AdminService adminService;

    public AdminBookController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public Result<PageResult<Book>> list(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) String author,
                                         @RequestParam(required = false) Long categoryId,
                                         @RequestParam(required = false) String status,
                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         HttpServletRequest request) {
        return adminService.listBooks(role(request), title, author, categoryId, status, pageNum, pageSize);
    }

    @PostMapping
    public Result<Void> save(@RequestBody Book book, HttpServletRequest request) {
        return adminService.saveBook(role(request), book);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Book book, HttpServletRequest request) {
        book.setId(id);
        return adminService.updateBook(role(request), book);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status, HttpServletRequest request) {
        return adminService.updateBookStatus(role(request), id, status);
    }

    private String role(HttpServletRequest request) {
        return (String) request.getAttribute("currentRole");
    }
}
