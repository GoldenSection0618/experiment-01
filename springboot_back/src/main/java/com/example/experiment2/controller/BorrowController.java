package com.example.experiment2.controller;

import com.example.experiment2.common.Result;
import com.example.experiment2.entity.MyBorrowRecord;
import com.example.experiment2.service.BorrowService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/{bookId}")
    public Result<Void> borrowBook(@PathVariable Long bookId, HttpServletRequest request) {
        return borrowService.borrowBook(bookId, currentUserId(request), currentRole(request));
    }

    @GetMapping("/my")
    public Result<List<MyBorrowRecord>> listMyBorrows(HttpServletRequest request) {
        return borrowService.listMyBorrows(currentUserId(request), currentRole(request));
    }

    @PostMapping("/{recordId}/renew")
    public Result<Void> renewBorrow(@PathVariable Long recordId, HttpServletRequest request) {
        return borrowService.renewBorrow(recordId, currentUserId(request), currentRole(request));
    }

    @PostMapping("/{recordId}/return")
    public Result<Void> returnBorrow(@PathVariable Long recordId, HttpServletRequest request) {
        return borrowService.returnBorrow(recordId, currentUserId(request), currentRole(request));
    }

    private Long currentUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("currentUserId");
    }

    private String currentRole(HttpServletRequest request) {
        return (String) request.getAttribute("currentRole");
    }
}
