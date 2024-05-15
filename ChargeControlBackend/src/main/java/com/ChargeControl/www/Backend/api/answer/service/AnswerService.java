package com.ChargeControl.www.Backend.api.answer.service;

import com.ChargeControl.www.Backend.api.answer.domain.Answer;
import com.ChargeControl.www.Backend.api.answer.dto.AnswerRequestDto;
import com.ChargeControl.www.Backend.api.answer.dto.AnswerResponseDto;
import com.ChargeControl.www.Backend.api.answer.repository.AnswerRepository;
import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.api.member.domain.Role;
import com.ChargeControl.www.Backend.api.question.domain.Question;
import com.ChargeControl.www.Backend.api.question.repository.QuestionRepository;
import com.ChargeControl.www.Backend.common.exception.BadRequestException;
import com.ChargeControl.www.Backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public void createAnswer(AnswerRequestDto answerRequestDto, Member member) {
        Question question = questionRepository.findById(answerRequestDto.getQuestionId())
                .orElseThrow(() -> new NotFoundException("해당하는 이의신청 게시물을 찾을 수 없습니다."));

        if (question.getIsWrite()) {
            throw new BadRequestException("이미 답변이 등록된 게시물입니다.");
        }

        Answer answer = Answer.builder()
                .writer(member.getName())
                .content(answerRequestDto.getContent())
                .question(question)
                .build();

        answerRepository.save(answer);

        Question updatedQuestion = question.markAnswered();
        questionRepository.save(updatedQuestion);
    }

    @Transactional(readOnly = true)
    public AnswerResponseDto getAnswer(Long questionId, Member member) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("해당하는 이의신청 게시물을 찾을 수 없습니다."));

        if (!(member.getRole().equals(Role.ADMIN) || question.getWriter().equals(member.getName()))) {
            throw new BadRequestException("해당 이의신청 게시물에 접근할 권한이 없습니다.");
        }

        Answer answer = answerRepository.findByQuestion_QuestionId(questionId)
                .orElseThrow(() -> new NotFoundException("해당 이의신청 게시물에 대한 답변이 없습니다."));

        return AnswerResponseDto.builder()
                .answerId(answer.getAnswerId())
                .writer(answer.getWriter())
                .content(answer.getContent())
                .createdAt(formatDateTime(answer.getCreatedAt()))
                .build();
    }

    @Transactional
    public void updateAnswer(Long questionId, AnswerRequestDto answerRequestDto, Member member) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("해당 답변을 수정할 권한이 없습니다.");
        }

        Answer answer = answerRepository.findByQuestion_QuestionId(questionId)
                .orElseThrow(() -> new NotFoundException("해당 이의신청 게시물에 대한 답변이 없습니다."));

        Answer modifiedAnswer = answer.modifyAnswer(answerRequestDto.getContent());

        answerRepository.save(modifiedAnswer);
    }

    @Transactional
    public void deleteAnswer(Long questionId, Member member) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("해당 답변을 삭제할 권한이 없습니다.");
        }

        Answer answer = answerRepository.findByQuestion_QuestionId(questionId)
                .orElseThrow(() -> new NotFoundException("해당 이의신청 게시물에 대한 답변이 없습니다."));

        answerRepository.delete(answer);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("해당하는 이의신청 게시물을 찾을 수 없습니다."));

        Question updatedQuestion = question.unmarkAnswered();

        questionRepository.save(updatedQuestion);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
        return dateTime.format(formatter);
    }
}
