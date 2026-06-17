package com.example.experiment2.entity;

import java.util.List;

public class StatisticsOverview {

    private StatisticsSummary summary;
    private List<BookBorrowRank> hotBooks;
    private List<CategoryBorrowStat> categoryStats;
    private List<UserBorrowStat> userStats;
    private List<DailyBorrowTrend> recentTrends;

    public StatisticsSummary getSummary() {
        return summary;
    }

    public void setSummary(StatisticsSummary summary) {
        this.summary = summary;
    }

    public List<BookBorrowRank> getHotBooks() {
        return hotBooks;
    }

    public void setHotBooks(List<BookBorrowRank> hotBooks) {
        this.hotBooks = hotBooks;
    }

    public List<CategoryBorrowStat> getCategoryStats() {
        return categoryStats;
    }

    public void setCategoryStats(List<CategoryBorrowStat> categoryStats) {
        this.categoryStats = categoryStats;
    }

    public List<UserBorrowStat> getUserStats() {
        return userStats;
    }

    public void setUserStats(List<UserBorrowStat> userStats) {
        this.userStats = userStats;
    }

    public List<DailyBorrowTrend> getRecentTrends() {
        return recentTrends;
    }

    public void setRecentTrends(List<DailyBorrowTrend> recentTrends) {
        this.recentTrends = recentTrends;
    }
}
