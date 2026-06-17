package com.example.experiment2.service.impl;

import com.example.experiment2.common.Result;
import com.example.experiment2.entity.Book;
import com.example.experiment2.entity.BorrowRecord;
import com.example.experiment2.entity.BorrowRule;
import com.example.experiment2.entity.MyBorrowRecord;
import com.example.experiment2.mapper.BorrowRecordMapper;
import com.example.experiment2.service.BorrowService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRecordMapper borrowRecordMapper;

    public BorrowServiceImpl(BorrowRecordMapper borrowRecordMapper) {
        this.borrowRecordMapper = borrowRecordMapper;
    }

    @Override
    @Transactional
    public Result<Void> borrowBook(Long bookId, Long currentUserId, String currentRole) {
        Result<Void> roleResult = checkUserRole(currentUserId, currentRole);
        if (!roleResult.isSuccess()) {
            return roleResult;
        }

        Book book = borrowRecordMapper.findBookById(bookId);
        if (book == null) {
            return Result.fail("图书不存在");
        }
        if (!"ON_SHELF".equals(book.getStatus())) {
            return Result.fail("图书已下架");
        }
        if (book.getAvailableStock() == null || book.getAvailableStock() <= 0) {
            return Result.fail("图书库存不足");
        }
        if (borrowRecordMapper.countActiveBorrow(currentUserId, bookId) > 0) {
            return Result.fail("不能重复借阅同一本未归还图书");
        }

        BorrowRule rule = borrowRecordMapper.findActiveRule();
        if (rule == null) {
            return Result.fail("没有可用的借阅规则");
        }

        int stockRows = borrowRecordMapper.decreaseBookStock(bookId);
        if (stockRows == 0) {
            return Result.fail("图书库存不足或已下架");
        }

        // 借阅记录固定保存当时使用的规则，后续规则调整不影响历史应还时间。
        LocalDateTime now = LocalDateTime.now();
        BorrowRecord record = new BorrowRecord();
        record.setUserId(currentUserId);
        record.setBookId(bookId);
        record.setRuleId(rule.getId());
        record.setBorrowTime(now);
        record.setDueTime(now.plusDays(rule.getBorrowDays()));
        record.setReturnTime(null);
        record.setRenewCount(0);
        record.setOverdueDays(0);
        record.setFineAmount(BigDecimal.ZERO);
        record.setStatus("BORROWED");
        borrowRecordMapper.insert(record);
        return Result.<Void>success("借阅成功", null);
    }

    @Override
    public Result<List<MyBorrowRecord>> listMyBorrows(Long currentUserId, String currentRole) {
        Result<Void> roleResult = checkUserRole(currentUserId, currentRole);
        if (!roleResult.isSuccess()) {
            return Result.fail(roleResult.getMessage());
        }
        borrowRecordMapper.markMyOverdue(currentUserId, LocalDateTime.now());
        return Result.success(borrowRecordMapper.findMyBorrows(currentUserId));
    }

    @Override
    @Transactional
    public Result<Void> renewBorrow(Long recordId, Long currentUserId, String currentRole) {
        Result<Void> roleResult = checkUserRole(currentUserId, currentRole);
        if (!roleResult.isSuccess()) {
            return roleResult;
        }

        BorrowRecord record = borrowRecordMapper.findByIdAndUserId(recordId, currentUserId);
        if (record == null) {
            return Result.fail("借阅记录不存在或无权操作");
        }
        if ("RETURNED".equals(record.getStatus())) {
            return Result.fail("该记录已归还，不能续借");
        }
        if ("OVERDUE".equals(record.getStatus())) {
            return Result.fail("逾期记录不能续借");
        }
        if (!"BORROWED".equals(record.getStatus())) {
            return Result.fail("当前状态不能续借");
        }
        // 续借前重新按服务器时间判断，避免依赖列表页的逾期刷新结果。
        if (LocalDateTime.now().isAfter(record.getDueTime())) {
            borrowRecordMapper.markMyOverdue(currentUserId, LocalDateTime.now());
            return Result.fail("借阅已逾期，不能续借");
        }

        BorrowRule rule = borrowRecordMapper.findRuleById(record.getRuleId());
        if (rule == null) {
            return Result.fail("借阅规则不存在");
        }
        if (record.getRenewCount() >= rule.getMaxRenewCount()) {
            return Result.fail("已达到最大续借次数");
        }

        int newRenewCount = record.getRenewCount() + 1;
        LocalDateTime newDueTime = record.getDueTime().plusDays(rule.getRenewDays());
        int rows = borrowRecordMapper.renewBorrow(recordId, currentUserId, newDueTime, newRenewCount);
        if (rows == 0) {
            return Result.fail("续借失败");
        }
        return Result.<Void>success("续借成功", null);
    }

    @Override
    @Transactional
    public Result<Void> returnBorrow(Long recordId, Long currentUserId, String currentRole) {
        Result<Void> roleResult = checkUserRole(currentUserId, currentRole);
        if (!roleResult.isSuccess()) {
            return roleResult;
        }

        BorrowRecord record = borrowRecordMapper.findByIdAndUserId(recordId, currentUserId);
        if (record == null) {
            return Result.fail("借阅记录不存在或无权操作");
        }
        if ("RETURNED".equals(record.getStatus())) {
            return Result.fail("该记录已归还，不能重复操作");
        }
        if (!"BORROWED".equals(record.getStatus()) && !"OVERDUE".equals(record.getStatus())) {
            return Result.fail("当前状态不能归还");
        }

        BorrowRule rule = borrowRecordMapper.findRuleById(record.getRuleId());
        if (rule == null) {
            return Result.fail("借阅规则不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        int overdueDays = calculateOverdueDays(record.getDueTime(), now);
        BigDecimal fineAmount = rule.getFinePerDay().multiply(BigDecimal.valueOf(overdueDays));

        int recordRows = borrowRecordMapper.returnBorrow(recordId, currentUserId, now, "RETURNED", overdueDays, fineAmount);
        if (recordRows == 0) {
            return Result.fail("归还失败");
        }

        int stockRows = borrowRecordMapper.increaseBookStock(record.getBookId());
        if (stockRows == 0) {
            // 归还记录和库存恢复必须同时成功，否则会造成库存与未归还记录不一致。
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("库存状态异常，归还失败");
        }
        return Result.<Void>success("归还成功", null);
    }

    private Result<Void> checkUserRole(Long currentUserId, String currentRole) {
        if (currentUserId == null) {
            return Result.fail("登录状态已失效");
        }
        if (!"USER".equals(currentRole)) {
            return Result.fail("只有普通用户可以进行借阅操作");
        }
        return Result.<Void>success(null);
    }

    private int calculateOverdueDays(LocalDateTime dueTime, LocalDateTime returnTime) {
        if (!returnTime.isAfter(dueTime)) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(dueTime.toLocalDate(), returnTime.toLocalDate());
        return (int) Math.max(days, 1);
    }
}
