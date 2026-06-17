package com.example.experiment2.controller;

import com.example.experiment2.common.Result;
import com.example.experiment2.dto.AiBookRequest;
import com.example.experiment2.dto.AiBookResponse;
import com.example.experiment2.service.AiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/book-intro")
    public Result<AiBookResponse> introduceBook(@RequestBody AiBookRequest request) {
        if (request == null) {
            return Result.fail("请选择图书");
        }
        return aiService.introduceBook(request.getBookId());
    }
}
