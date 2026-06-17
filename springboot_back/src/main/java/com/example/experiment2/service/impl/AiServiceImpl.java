package com.example.experiment2.service.impl;

import com.example.experiment2.common.Result;
import com.example.experiment2.dto.AiBookResponse;
import com.example.experiment2.entity.Book;
import com.example.experiment2.mapper.BookMapper;
import com.example.experiment2.service.AiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AiServiceImpl implements AiService {

    private final BookMapper bookMapper;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${ai.local-chat-url:http://localhost:1234/api/v1/chat}")
    private String localChatUrl;

    @Value("${ai.model:google/gemma-4-e4b}")
    private String model;

    public AiServiceImpl(BookMapper bookMapper, ObjectMapper objectMapper) {
        this.bookMapper = bookMapper;
        this.objectMapper = objectMapper;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(30));
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public Result<AiBookResponse> introduceBook(Long bookId) {
        if (bookId == null) {
            return Result.fail("请选择图书");
        }
        Book book = bookMapper.findById(bookId);
        if (book == null || !"ON_SHELF".equals(book.getStatus())) {
            return Result.fail("只能询问已上架图书");
        }

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("model", model);
        request.put("reasoning", "off");
        request.put("system_prompt", "你是校园图书馆助手。只输出最终中文介绍，不要输出推理过程、计划、步骤或英文。");
        request.put("input", "请用100字左右介绍" + book.getAuthor() + "的《" + book.getTitle() + "》。");

        try {
            String rawResponse = restTemplate.postForObject(localChatUrl, request, String.class);
            String answer = cleanAnswer(parseAnswer(rawResponse));
            if (answer == null || answer.trim().isEmpty()) {
                return Result.fail("AI 未返回有效内容");
            }
            return Result.success(new AiBookResponse(answer.trim()));
        } catch (RestClientException exception) {
            return Result.fail("本地 AI 服务暂不可用");
        } catch (Exception exception) {
            return Result.fail("AI 返回内容解析失败");
        }
    }

    private String parseAnswer(String rawResponse) throws Exception {
        if (rawResponse == null || rawResponse.trim().isEmpty()) {
            return "";
        }
        JsonNode root = objectMapper.readTree(rawResponse);
        if (root.hasNonNull("output")) {
            JsonNode output = root.get("output");
            if (output.isTextual()) return output.asText();
            if (output.isArray()) return readContentFromArray(output);
            if (output.hasNonNull("content")) return output.get("content").asText();
        }
        if (root.hasNonNull("response")) return root.get("response").asText();
        if (root.hasNonNull("content")) return root.get("content").asText();
        JsonNode choices = root.get("choices");
        if (choices != null && choices.isArray() && !choices.isEmpty()) {
            JsonNode first = choices.get(0);
            if (first.hasNonNull("text")) return first.get("text").asText();
            JsonNode message = first.get("message");
            if (message != null && message.hasNonNull("content")) {
                return message.get("content").asText();
            }
        }
        return rawResponse;
    }

    private String readContentFromArray(JsonNode output) {
        StringBuilder answer = new StringBuilder();
        for (JsonNode item : output) {
            if (item.hasNonNull("content")) {
                if (!answer.isEmpty()) {
                    answer.append("\n");
                }
                answer.append(item.get("content").asText());
            } else if (item.isTextual()) {
                if (!answer.isEmpty()) {
                    answer.append("\n");
                }
                answer.append(item.asText());
            }
        }
        return answer.toString();
    }

    private String cleanAnswer(String answer) {
        if (answer == null) {
            return "";
        }
        String cleaned = answer.trim();
        String[] markers = {"**润色和整合为最终输出。**", "最终输出。", "最终输出：", "最终输出"};
        for (String marker : markers) {
            int index = cleaned.lastIndexOf(marker);
            if (index >= 0) {
                cleaned = cleaned.substring(index + marker.length()).trim();
                break;
            }
        }
        int bookNameIndex = cleaned.lastIndexOf("《");
        if (bookNameIndex > 0 && cleaned.substring(0, bookNameIndex).contains("草稿")) {
            cleaned = cleaned.substring(bookNameIndex).trim();
        }
        return cleaned.replace("**", "").trim();
    }
}
