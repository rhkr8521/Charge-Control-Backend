package com.ChargeControl.www.Backend.api.question.repository;

import com.ChargeControl.www.Backend.api.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByWriter(String writer);
}