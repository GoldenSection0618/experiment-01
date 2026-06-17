package com.example.experiment2.controller;

import com.example.experiment2.common.Result;
import com.example.experiment2.entity.StatisticsOverview;
import com.example.experiment2.service.StatisticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/overview")
    public Result<StatisticsOverview> overview(HttpServletRequest request) {
        return statisticsService.overview((String) request.getAttribute("currentRole"));
    }
}
