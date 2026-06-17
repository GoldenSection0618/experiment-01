package com.example.experiment2.mapper;

import com.example.experiment2.entity.Book;
import com.example.experiment2.entity.AdminBorrowRecord;
import com.example.experiment2.entity.BorrowRecord;
import com.example.experiment2.entity.BorrowRule;
import com.example.experiment2.entity.MyBorrowRecord;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BorrowRecordMapper {

    Book findBookById(Long bookId);

    BorrowRule findActiveRule();

    BorrowRule findRuleById(Long ruleId);

    int countActiveBorrow(@Param("userId") Long userId, @Param("bookId") Long bookId);

    int decreaseBookStock(Long bookId);

    int increaseBookStock(Long bookId);

    int insert(BorrowRecord record);

    List<MyBorrowRecord> findMyBorrows(Long userId);

    BorrowRecord findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int renewBorrow(@Param("id") Long id,
                    @Param("userId") Long userId,
                    @Param("dueTime") LocalDateTime dueTime,
                    @Param("renewCount") Integer renewCount);

    int returnBorrow(@Param("id") Long id,
                     @Param("userId") Long userId,
                     @Param("returnTime") LocalDateTime returnTime,
                     @Param("status") String status,
                     @Param("overdueDays") Integer overdueDays,
                     @Param("fineAmount") java.math.BigDecimal fineAmount);

    int markMyOverdue(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    int markAllOverdue(@Param("now") LocalDateTime now);

    java.math.BigDecimal sumUserFine(Long userId);

    List<String> findUnreturnedOverdueBookTitles(Long userId);

    int clearUserFine(Long userId);

    List<AdminBorrowRecord> findAdminPage(@Param("username") String username,
                                          @Param("title") String title,
                                          @Param("status") String status,
                                          @Param("startTime") String startTime,
                                          @Param("endTime") String endTime,
                                          @Param("offset") int offset,
                                          @Param("pageSize") int pageSize);

    long countAdmin(@Param("username") String username,
                    @Param("title") String title,
                    @Param("status") String status,
                    @Param("startTime") String startTime,
                    @Param("endTime") String endTime);
}
