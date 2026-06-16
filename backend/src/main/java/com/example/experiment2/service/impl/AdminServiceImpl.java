package com.example.experiment2.service.impl;

import com.example.experiment2.common.PageResult;
import com.example.experiment2.common.Result;
import com.example.experiment2.entity.AdminBorrowRecord;
import com.example.experiment2.entity.Book;
import com.example.experiment2.entity.BookCategory;
import com.example.experiment2.entity.SysUser;
import com.example.experiment2.mapper.BookCategoryMapper;
import com.example.experiment2.mapper.BookMapper;
import com.example.experiment2.mapper.BorrowRecordMapper;
import com.example.experiment2.mapper.SysUserMapper;
import com.example.experiment2.service.AdminService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final BookCategoryMapper categoryMapper;
    private final BookMapper bookMapper;
    private final SysUserMapper sysUserMapper;
    private final BorrowRecordMapper borrowRecordMapper;

    public AdminServiceImpl(BookCategoryMapper categoryMapper, BookMapper bookMapper, SysUserMapper sysUserMapper, BorrowRecordMapper borrowRecordMapper) {
        this.categoryMapper = categoryMapper;
        this.bookMapper = bookMapper;
        this.sysUserMapper = sysUserMapper;
        this.borrowRecordMapper = borrowRecordMapper;
    }

    @Override
    public Result<PageResult<BookCategory>> listCategories(String role, String name, String status, Integer pageNum, Integer pageSize) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return Result.fail(check.getMessage());
        int offset = offset(pageNum, pageSize);
        int size = pageSize(pageSize);
        return Result.success(new PageResult<>(categoryMapper.findPage(name, status, offset, size), categoryMapper.count(name, status)));
    }

    @Override
    public Result<List<BookCategory>> activeCategories(String role) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return Result.fail(check.getMessage());
        return Result.success(categoryMapper.findActive());
    }

    @Override
    public Result<Void> saveCategory(String role, BookCategory category) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return check;
        if (category == null || isBlank(category.getName())) return Result.fail("分类名称不能为空");
        if (categoryMapper.findByName(category.getName().trim()) != null) return Result.fail("分类名称已存在");
        category.setName(category.getName().trim());
        category.setStatus(isBlank(category.getStatus()) ? "ACTIVE" : category.getStatus());
        categoryMapper.insert(category);
        return Result.<Void>success("保存成功", null);
    }

    @Override
    public Result<Void> updateCategory(String role, BookCategory category) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return check;
        if (category == null || category.getId() == null || isBlank(category.getName())) return Result.fail("分类信息不完整");
        if (categoryMapper.findById(category.getId()) == null) return Result.fail("分类不存在");
        if (categoryMapper.findByNameExcludeId(category.getName().trim(), category.getId()) != null) return Result.fail("分类名称已存在");
        category.setName(category.getName().trim());
        category.setStatus(isBlank(category.getStatus()) ? "ACTIVE" : category.getStatus());
        categoryMapper.update(category);
        return Result.<Void>success("保存成功", null);
    }

    @Override
    public Result<Void> updateCategoryStatus(String role, Long id, String status) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return check;
        if (!"ACTIVE".equals(status) && !"DISABLED".equals(status)) return Result.fail("分类状态不正确");
        categoryMapper.updateStatus(id, status);
        return Result.<Void>success("状态已更新", null);
    }

    @Override
    public Result<PageResult<Book>> listBooks(String role, String title, String author, Long categoryId, String status, Integer pageNum, Integer pageSize) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return Result.fail(check.getMessage());
        int offset = offset(pageNum, pageSize);
        int size = pageSize(pageSize);
        return Result.success(new PageResult<>(
                bookMapper.findAdminPage(title, author, categoryId, status, offset, size),
                bookMapper.countAdmin(title, author, categoryId, status)
        ));
    }

    @Override
    public Result<Void> saveBook(String role, Book book) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return check;
        Result<Void> valid = validateBook(book, false);
        if (!valid.isSuccess()) return valid;
        book.setAvailableStock(book.getTotalStock());
        book.setStatus(isBlank(book.getStatus()) ? "ON_SHELF" : book.getStatus());
        bookMapper.insert(book);
        return Result.<Void>success("保存成功", null);
    }

    @Override
    public Result<Void> updateBook(String role, Book book) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return check;
        Result<Void> valid = validateBook(book, true);
        if (!valid.isSuccess()) return valid;

        Book old = bookMapper.findById(book.getId());
        int borrowedCount = old.getTotalStock() - old.getAvailableStock();
        if (book.getTotalStock() < borrowedCount) {
            return Result.fail("总库存不能小于已借出数量");
        }
        book.setAvailableStock(book.getTotalStock() - borrowedCount);
        bookMapper.updateAdmin(book);
        return Result.<Void>success("保存成功", null);
    }

    @Override
    public Result<Void> updateBookStatus(String role, Long id, String status) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return check;
        if (!"ON_SHELF".equals(status) && !"OFF_SHELF".equals(status)) return Result.fail("图书状态不正确");
        bookMapper.updateStatus(id, status);
        return Result.<Void>success("状态已更新", null);
    }

    @Override
    public Result<PageResult<SysUser>> listUsers(String role, String username, String email, String userRole, String status, Integer pageNum, Integer pageSize) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return Result.fail(check.getMessage());
        int offset = offset(pageNum, pageSize);
        int size = pageSize(pageSize);
        return Result.success(new PageResult<>(
                sysUserMapper.findAdminPage(username, email, userRole, status, offset, size),
                sysUserMapper.countAdmin(username, email, userRole, status)
        ));
    }

    @Override
    public Result<Void> updateUserStatus(String role, Long id, String status) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return check;
        if (!"NORMAL".equals(status) && !"DISABLED".equals(status)) return Result.fail("用户状态不正确");
        SysUser user = sysUserMapper.findById(id);
        if (user == null) return Result.fail("用户不存在");
        if ("ADMIN".equals(user.getRole())) return Result.fail("不能禁用管理员账号");
        sysUserMapper.updateStatus(id, status);
        return Result.<Void>success("状态已更新", null);
    }

    @Override
    public Result<PageResult<AdminBorrowRecord>> listBorrows(String role, String username, String title, String status, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        Result<Void> check = checkAdmin(role);
        if (!check.isSuccess()) return Result.fail(check.getMessage());
        borrowRecordMapper.markAllOverdue(LocalDateTime.now());
        int offset = offset(pageNum, pageSize);
        int size = pageSize(pageSize);
        return Result.success(new PageResult<>(
                borrowRecordMapper.findAdminPage(username, title, status, startTime, endTime, offset, size),
                borrowRecordMapper.countAdmin(username, title, status, startTime, endTime)
        ));
    }

    private Result<Void> validateBook(Book book, boolean update) {
        if (book == null || isBlank(book.getTitle()) || isBlank(book.getAuthor()) || isBlank(book.getIsbn()) || book.getCategoryId() == null || book.getTotalStock() == null) {
            return Result.fail("图书信息不完整");
        }
        if (book.getTotalStock() < 0) return Result.fail("总库存不能小于 0");
        BookCategory category = categoryMapper.findById(book.getCategoryId());
        if (category == null || !"ACTIVE".equals(category.getStatus())) return Result.fail("请选择有效分类");
        if (update) {
            if (book.getId() == null || bookMapper.findById(book.getId()) == null) return Result.fail("图书不存在");
            if (bookMapper.findByIsbnExcludeId(book.getIsbn(), book.getId()) != null) return Result.fail("ISBN 已存在");
        } else if (bookMapper.findByIsbn(book.getIsbn()) != null) {
            return Result.fail("ISBN 已存在");
        }
        return Result.<Void>success(null);
    }

    private Result<Void> checkAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            return Result.fail("无管理员权限");
        }
        return Result.<Void>success(null);
    }

    private int offset(Integer pageNum, Integer pageSize) {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        return (page - 1) * pageSize(pageSize);
    }

    private int pageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) return 10;
        return Math.min(pageSize, 100);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
