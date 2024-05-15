package com.ChargeControl.www.Backend.api.answer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AnswerResponseDto {
    private final Long answerId;
    private final String writer;
    private final String content;
    private final String createdAt;

    @Builder
    public AnswerResponseDto(Long answerId, String writer, String content, String createdAt) {
        this.answerId = answerId;
        this.writer = writer;
        this.content = content;
        this.createdAt = createdAt;
    }
}
