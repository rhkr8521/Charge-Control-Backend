package com.ChargeControl.www.Backend.api.answer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AnswerRequestDto {
    private final Long questionId;
    private final String content;

    @Builder
    public AnswerRequestDto(Long questionId, String content) {
        this.questionId = questionId;
        this.content = content;
    }
}
