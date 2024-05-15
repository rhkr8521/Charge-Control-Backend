package com.ChargeControl.www.Backend.api.question.domain;

import com.ChargeControl.www.Backend.api.answer.domain.Answer;
import com.ChargeControl.www.Backend.api.violation.domain.Violation;
import com.ChargeControl.www.Backend.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private String title;
    private String content;
    private String writer;
    private String carNumber;
    private Boolean isWrite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "violation_id")
    @JsonBackReference
    private Violation violation;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Answer answer;

    @Builder
    public Question(Long questionId, String title, String content, String writer, String carNumber, Boolean isWrite, Violation violation, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.carNumber = carNumber;
        this.isWrite = isWrite;
        this.violation = violation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Question modifyQuestion(String title, String content) {
        return Question.builder()
                .questionId(this.questionId)
                .title(title)
                .content(content)
                .writer(this.writer)
                .carNumber(this.carNumber)
                .isWrite(this.isWrite)
                .violation(this.violation)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }

    public Question markAnswered() {
        return Question.builder()
                .questionId(this.questionId)
                .title(this.title)
                .content(this.content)
                .writer(this.writer)
                .carNumber(this.carNumber)
                .isWrite(true)
                .violation(this.violation)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

}
