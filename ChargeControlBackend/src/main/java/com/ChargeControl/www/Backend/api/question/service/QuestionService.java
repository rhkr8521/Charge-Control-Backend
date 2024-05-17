package com.ChargeControl.www.Backend.api.question.service;

import com.ChargeControl.www.Backend.api.question.domain.Question;
import com.ChargeControl.www.Backend.api.question.dto.QuestionRequestDto;
import com.ChargeControl.www.Backend.api.question.dto.QuestionResponseDto;
import com.ChargeControl.www.Backend.api.question.repository.QuestionRepository;
import com.ChargeControl.www.Backend.api.violation.repository.ViolationRepository;
import com.ChargeControl.www.Backend.api.violation.domain.Violation;
import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.common.exception.BadRequestException;
import com.ChargeControl.www.Backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ViolationRepository violationRepository;

    @Transactional
    public QuestionResponseDto createQuestion(QuestionRequestDto questionRequestDto, Member member) {
        Violation violation = violationRepository.findById(questionRequestDto.getViolationId())
                .orElseThrow(() -> new NotFoundException("해당하는 위반데이터가 없습니다."));

        Question question = Question.builder()
                .title(questionRequestDto.getTitle())
                .content(questionRequestDto.getContent())
                .writer(member.getName())
                .carNumber(member.getCarNumber())
                .isWrite(false)
                .violation(violation)
                .build();

        question = questionRepository.save(question);
        return mapToDto(question);
    }

    public List<QuestionResponseDto> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<QuestionResponseDto> getQuestionsByWriter(String writer) {
        List<Question> questions = questionRepository.findByWriter(writer);
        return questions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public QuestionResponseDto getQuestionDetail(Long questionId, String writer, boolean isAdmin) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("해당 이의신청 정보를 찾을 수 없습니다."));

        if (!isAdmin && !question.getWriter().equals(writer)) {
            throw new NotFoundException("접근 권한이 없습니다.");
        }

        return mapToDto(question);
    }

    @Transactional
    public void modifyQuestion(Long questionId, QuestionRequestDto questionRequestDto, Member member) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("해당 이의신청 게시물을 찾을 수 없습니다."));

        if (!question.getWriter().equals(member.getName())) {
            throw new BadRequestException("해당 이의신청 게시물에 대한 수정 권한이 없습니다.");
        }

        Question updatedQuestion = question.modifyQuestion(questionRequestDto.getTitle(), questionRequestDto.getContent());
        questionRepository.save(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(Long questionId, Member member) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("해당 이의신청 게시물을 찾을 수 없습니다."));

        if (!question.getWriter().equals(member.getName())) {
            throw new BadRequestException("해당 이의신청 게시물에 대한 삭제 권한이 없습니다.");
        }

        questionRepository.delete(question);
    }

    public List<QuestionResponseDto> searchQuestions(String title, String content, String writer, String carNumber) {
        List<Question> questions = questionRepository.findAll();

        if (title != null) {
            questions = questions.stream().filter(q -> q.getTitle().contains(title)).collect(Collectors.toList());
        }

        if (content != null) {
            questions = questions.stream().filter(q -> q.getContent().contains(content)).collect(Collectors.toList());
        }

        if (writer != null) {
            questions = questions.stream().filter(q -> q.getWriter().contains(writer)).collect(Collectors.toList());
        }

        if (carNumber != null) {
            questions = questions.stream().filter(q -> q.getCarNumber().contains(carNumber)).collect(Collectors.toList());
        }

        return questions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private QuestionResponseDto mapToDto(Question question) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = question.getCreatedAt().format(formatter);

        return QuestionResponseDto.builder()
                .questionId(question.getQuestionId())
                .createdDate(formattedDate)
                .violationId(question.getViolation().getViolationId())
                .title(question.getTitle())
                .content(question.getContent())
                .writer(question.getWriter())
                .carNumber(question.getCarNumber())
                .isWrite(question.getIsWrite())
                .build();
    }
}
