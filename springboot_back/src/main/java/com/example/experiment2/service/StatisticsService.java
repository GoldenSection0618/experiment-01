package com.example.experiment2.service;

import com.example.experiment2.common.Result;
import com.example.experiment2.entity.StatisticsOverview;

public interface StatisticsService {

    Result<StatisticsOverview> overview(String currentRole);
}
