package com.example.experiment2.entity;

import java.math.BigDecimal;
import java.util.List;

public class UserDebtInfo {

    private Long userId;
    private BigDecimal debtAmount;
    private boolean hasUnreturnedOverdue;
    private List<String> unreturnedBooks;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(BigDecimal debtAmount) {
        this.debtAmount = debtAmount;
    }

    public boolean isHasUnreturnedOverdue() {
        return hasUnreturnedOverdue;
    }

    public void setHasUnreturnedOverdue(boolean hasUnreturnedOverdue) {
        this.hasUnreturnedOverdue = hasUnreturnedOverdue;
    }

    public List<String> getUnreturnedBooks() {
        return unreturnedBooks;
    }

    public void setUnreturnedBooks(List<String> unreturnedBooks) {
        this.unreturnedBooks = unreturnedBooks;
    }
}
