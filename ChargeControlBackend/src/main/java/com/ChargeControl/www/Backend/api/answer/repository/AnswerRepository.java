package com.ChargeControl.www.Backend.api.answer.repository;

import com.ChargeControl.www.Backend.api.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByQuestion_QuestionId(Long questionId);
}
