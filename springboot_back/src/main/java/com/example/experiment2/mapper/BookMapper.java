package com.example.experiment2.mapper;

import com.example.experiment2.entity.Book;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BookMapper {

    List<Book> findAll();

    List<Book> findUserPage(@Param("title") String title,
                            @Param("author") String author,
                            @Param("categoryId") Long categoryId,
                            @Param("offset") int offset,
                            @Param("pageSize") int pageSize);

    long countUser(@Param("title") String title,
                   @Param("author") String author,
                   @Param("categoryId") Long categoryId);

    List<Book> findAdminPage(@Param("title") String title,
                             @Param("author") String author,
                             @Param("categoryId") Long categoryId,
                             @Param("status") String status,
                             @Param("offset") int offset,
                             @Param("pageSize") int pageSize);

    long countAdmin(@Param("title") String title,
                    @Param("author") String author,
                    @Param("categoryId") Long categoryId,
                    @Param("status") String status);

    Book findById(Long id);

    Book findByIsbn(String isbn);

    Book findByIsbnExcludeId(@Param("isbn") String isbn, @Param("id") Long id);

    int insert(Book book);

    int updateAdmin(Book book);

    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
