package com.example.experiment2.mapper;

import com.example.experiment2.entity.BookBorrowRank;
import com.example.experiment2.entity.CategoryBorrowStat;
import com.example.experiment2.entity.DailyBorrowTrend;
import com.example.experiment2.entity.StatisticsSummary;
import com.example.experiment2.entity.UserBorrowStat;
import java.util.List;

public interface StatisticsMapper {

    StatisticsSummary findSummary();

    List<BookBorrowRank> findHotBooks();

    List<CategoryBorrowStat> findCategoryStats();

    List<UserBorrowStat> findUserStats();

    List<DailyBorrowTrend> findRecentTrends();
}
