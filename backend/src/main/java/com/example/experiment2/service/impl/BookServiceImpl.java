package com.example.experiment2.service.impl;

import com.example.experiment2.entity.Book;
import com.example.experiment2.mapper.BookMapper;
import com.example.experiment2.service.BookService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public List<Book> listBooks() {
        return bookMapper.findAll();
    }
}
