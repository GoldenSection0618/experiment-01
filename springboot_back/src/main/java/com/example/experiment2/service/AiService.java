package com.example.experiment2.service;

import com.example.experiment2.common.Result;
import com.example.experiment2.dto.AiBookResponse;

public interface AiService {

    Result<AiBookResponse> introduceBook(Long bookId);
}
