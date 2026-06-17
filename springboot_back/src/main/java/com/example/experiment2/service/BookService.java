package com.example.experiment2.service;

import com.example.experiment2.common.PageResult;
import com.example.experiment2.entity.Book;
import com.example.experiment2.entity.BookCategory;
import java.util.List;

public interface BookService {

    List<Book> listBooks();

    PageResult<Book> searchBooks(String title, String author, Long categoryId, Integer pageNum, Integer pageSize);

    List<BookCategory> listActiveCategories();
}
