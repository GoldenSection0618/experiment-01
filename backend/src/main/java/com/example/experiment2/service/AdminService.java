package com.example.experiment2.service;

import com.example.experiment2.common.PageResult;
import com.example.experiment2.common.Result;
import com.example.experiment2.entity.AdminBorrowRecord;
import com.example.experiment2.entity.Book;
import com.example.experiment2.entity.BookCategory;
import com.example.experiment2.entity.SysUser;

public interface AdminService {

    Result<PageResult<BookCategory>> listCategories(String role, String name, String status, Integer pageNum, Integer pageSize);

    Result<java.util.List<BookCategory>> activeCategories(String role);

    Result<Void> saveCategory(String role, BookCategory category);

    Result<Void> updateCategory(String role, BookCategory category);

    Result<Void> updateCategoryStatus(String role, Long id, String status);

    Result<PageResult<Book>> listBooks(String role, String title, String author, Long categoryId, String status, Integer pageNum, Integer pageSize);

    Result<Void> saveBook(String role, Book book);

    Result<Void> updateBook(String role, Book book);

    Result<Void> updateBookStatus(String role, Long id, String status);

    Result<PageResult<SysUser>> listUsers(String role, String username, String email, String userRole, String status, Integer pageNum, Integer pageSize);

    Result<Void> updateUserStatus(String role, Long id, String status);

    Result<PageResult<AdminBorrowRecord>> listBorrows(String role, String username, String title, String status, String startTime, String endTime, Integer pageNum, Integer pageSize);
}
