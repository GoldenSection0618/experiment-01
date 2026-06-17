package com.example.experiment2.controller;

import com.example.experiment2.common.PageResult;
import com.example.experiment2.common.Result;
import com.example.experiment2.entity.Book;
import com.example.experiment2.entity.BookCategory;
import com.example.experiment2.service.BookService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Result<PageResult<Book>> listBooks(@RequestParam(required = false) String title,
                                              @RequestParam(required = false) String author,
                                              @RequestParam(required = false) Long categoryId,
                                              @RequestParam(required = false) Integer pageNum,
                                              @RequestParam(required = false) Integer pageSize) {
        return Result.success(bookService.searchBooks(title, author, categoryId, pageNum, pageSize));
    }

    @GetMapping("/categories")
    public Result<List<BookCategory>> listCategories() {
        return Result.success(bookService.listActiveCategories());
    }
}
