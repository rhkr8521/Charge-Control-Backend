package com.ChargeControl.www.Backend.api.question.controller;

import com.ChargeControl.www.Backend.api.member.domain.Role;
import com.ChargeControl.www.Backend.api.question.dto.QuestionRequestDto;
import com.ChargeControl.www.Backend.api.question.dto.QuestionResponseDto;
import com.ChargeControl.www.Backend.api.question.service.QuestionService;
import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/write")
    public ResponseEntity<?> createQuestion(@RequestBody QuestionRequestDto questionRequestDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 완료되지 않았습니다.");
        }

        if (!(authentication.getPrincipal() instanceof Member)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 인증 실패");
        }

        Member member = (Member) authentication.getPrincipal();
        questionService.createQuestion(questionRequestDto, member);

        return ResponseEntity.ok(ApiResponse.<QuestionResponseDto>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("이의신청 완료")
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllQuestions(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 완료되지 않았습니다.");
        }

        if (!(authentication.getPrincipal() instanceof Member)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 인증 실패");
        }

        Member member = (Member) authentication.getPrincipal();
        List<QuestionResponseDto> questionResponses;

        if (member.getRole() == Role.ADMIN) {
            questionResponses = questionService.getAllQuestions();
        } else {
            questionResponses = questionService.getQuestionsByWriter(member.getName());
        }

        return ResponseEntity.ok(ApiResponse.<List<QuestionResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("이의신청 전체 조회 성공")
                .data(questionResponses)
                .build());
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getQuestionDetail(@PathVariable Long questionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 완료되지 않았습니다.");
        }

        if (!(authentication.getPrincipal() instanceof Member)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 인증 실패");
        }

        Member member = (Member) authentication.getPrincipal();
        boolean isAdmin = member.getRole() == Role.ADMIN;

        try {
            QuestionResponseDto questionResponse = questionService.getQuestionDetail(questionId, member.getName(), isAdmin);
            return ResponseEntity.ok(ApiResponse.<QuestionResponseDto>builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .message("이의신청 상세 조회 성공")
                    .data(questionResponse)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PatchMapping("/modify/{questionId}")
    public ResponseEntity<?> modifyQuestion(@PathVariable Long questionId, @RequestBody QuestionRequestDto questionRequestDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 완료되지 않았습니다.");
        }

        if (!(authentication.getPrincipal() instanceof Member)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 인증 실패");
        }

        Member member = (Member) authentication.getPrincipal();

        questionService.modifyQuestion(questionId, questionRequestDto, member);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("이의신청 수정 완료")
                .build());
    }

    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long questionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 완료되지 않았습니다.");
        }

        if (!(authentication.getPrincipal() instanceof Member)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 인증 실패");
        }

        Member member = (Member) authentication.getPrincipal();
        questionService.deleteQuestion(questionId, member);

        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("이의신청 삭제 완료")
                .build());
    }

}
