package com.example.experiment2.dto;

public class AiBookResponse {

    private String answer;

    public AiBookResponse() {
    }

    public AiBookResponse(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
