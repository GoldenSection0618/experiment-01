package com.example.experiment2.controller;

import com.example.experiment2.common.Result;
import com.example.experiment2.entity.Book;
import com.example.experiment2.service.BookService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Result<List<Book>> listBooks() {
        return Result.success(bookService.listBooks());
    }
}
