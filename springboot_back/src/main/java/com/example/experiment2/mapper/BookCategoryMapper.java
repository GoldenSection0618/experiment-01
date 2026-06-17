package com.example.experiment2.mapper;

import com.example.experiment2.entity.BookCategory;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BookCategoryMapper {

    List<BookCategory> findPage(@Param("name") String name,
                                @Param("status") String status,
                                @Param("offset") int offset,
                                @Param("pageSize") int pageSize);

    long count(@Param("name") String name, @Param("status") String status);

    List<BookCategory> findActive();

    BookCategory findById(Long id);

    BookCategory findByName(String name);

    BookCategory findByNameExcludeId(@Param("name") String name, @Param("id") Long id);

    int insert(BookCategory category);

    int update(BookCategory category);

    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
