package com.ChargeControl.www.Backend.api.question.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionRequestDto {
    private final Long violationId;
    private final String title;
    private final String content;

    @Builder
    public QuestionRequestDto(Long violationId, String title, String content) {
        this.violationId = violationId;
        this.title = title;
        this.content = content;
    }
}
