package com.ChargeControl.www.Backend.api.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponseDto {

    private long questionId;
    private String createdDate;
    private long violationId;
    private String title;
    private String content;
    private String writer;
    private String carNumber;
    private Boolean isWrite;


}
