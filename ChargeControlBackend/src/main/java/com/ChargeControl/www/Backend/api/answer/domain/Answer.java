package com.ChargeControl.www.Backend.api.answer.domain;

import com.ChargeControl.www.Backend.api.question.domain.Question;
import com.ChargeControl.www.Backend.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    private String writer;
    private String content;

    @OneToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private Question question;

    @Builder
    public Answer(Long answerId, Question question, String content, String writer,LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.answerId = answerId;
        this.question = question;
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Answer modifyAnswer(String content) {
        return Answer.builder()
                .answerId(this.answerId)
                .question(this.question)
                .content(content)
                .writer(this.writer)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}