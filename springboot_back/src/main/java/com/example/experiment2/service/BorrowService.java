package com.example.experiment2.service;

import com.example.experiment2.common.Result;
import com.example.experiment2.entity.MyBorrowRecord;
import java.util.List;

public interface BorrowService {

    Result<Void> borrowBook(Long bookId, Long currentUserId, String currentRole);

    Result<List<MyBorrowRecord>> listMyBorrows(Long currentUserId, String currentRole);

    Result<Void> renewBorrow(Long recordId, Long currentUserId, String currentRole);

    Result<Void> returnBorrow(Long recordId, Long currentUserId, String currentRole);
}
