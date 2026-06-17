package com.example.experiment2.entity;

public class StatisticsSummary {

    private Long totalBooks;
    private Long borrowedCount;
    private Long overdueCount;
    private Long userCount;

    public Long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(Long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public Long getBorrowedCount() {
        return borrowedCount;
    }

    public void setBorrowedCount(Long borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    public Long getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(Long overdueCount) {
        this.overdueCount = overdueCount;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }
}
