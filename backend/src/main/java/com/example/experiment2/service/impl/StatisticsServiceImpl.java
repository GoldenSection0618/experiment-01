package com.example.experiment2.service.impl;

import com.example.experiment2.common.Result;
import com.example.experiment2.entity.StatisticsOverview;
import com.example.experiment2.mapper.BorrowRecordMapper;
import com.example.experiment2.mapper.StatisticsMapper;
import com.example.experiment2.service.StatisticsService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper statisticsMapper;
    private final BorrowRecordMapper borrowRecordMapper;

    public StatisticsServiceImpl(StatisticsMapper statisticsMapper, BorrowRecordMapper borrowRecordMapper) {
        this.statisticsMapper = statisticsMapper;
        this.borrowRecordMapper = borrowRecordMapper;
    }

    @Override
    public Result<StatisticsOverview> overview(String currentRole) {
        if (!"ADMIN".equals(currentRole)) {
            return Result.fail("无管理员权限");
        }
        borrowRecordMapper.markAllOverdue(LocalDateTime.now());

        StatisticsOverview overview = new StatisticsOverview();
        overview.setSummary(statisticsMapper.findSummary());
        overview.setHotBooks(statisticsMapper.findHotBooks());
        overview.setCategoryStats(statisticsMapper.findCategoryStats());
        overview.setUserStats(statisticsMapper.findUserStats());
        overview.setRecentTrends(statisticsMapper.findRecentTrends());
        return Result.success(overview);
    }
}
