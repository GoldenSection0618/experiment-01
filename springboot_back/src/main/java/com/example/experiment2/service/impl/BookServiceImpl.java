package com.example.experiment2.service.impl;

import com.example.experiment2.common.PageResult;
import com.example.experiment2.entity.Book;
import com.example.experiment2.entity.BookCategory;
import com.example.experiment2.mapper.BookCategoryMapper;
import com.example.experiment2.mapper.BookMapper;
import com.example.experiment2.service.BookService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookCategoryMapper categoryMapper;

    public BookServiceImpl(BookMapper bookMapper, BookCategoryMapper categoryMapper) {
        this.bookMapper = bookMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Book> listBooks() {
        return bookMapper.findAll();
    }

    @Override
    public PageResult<Book> searchBooks(String title, String author, Long categoryId, Integer pageNum, Integer pageSize) {
        int page = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int size = pageSize == null || pageSize < 1 ? 10 : Math.min(pageSize, 50);
        int offset = (page - 1) * size;
        return new PageResult<>(
                bookMapper.findUserPage(title, author, categoryId, offset, size),
                bookMapper.countUser(title, author, categoryId)
        );
    }

    @Override
    public List<BookCategory> listActiveCategories() {
        return categoryMapper.findActive();
    }
}
