package com.ChargeControl.www.Backend.api.answer.controller;

import com.ChargeControl.www.Backend.api.answer.dto.AnswerRequestDto;
import com.ChargeControl.www.Backend.api.answer.dto.AnswerResponseDto;
import com.ChargeControl.www.Backend.api.answer.service.AnswerService;
import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.api.member.domain.Role;
import com.ChargeControl.www.Backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/write")
    public ResponseEntity<?> createAnswer(@RequestBody AnswerRequestDto answerRequestDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 완료되지 않았습니다.");
        }

        if (!(authentication.getPrincipal() instanceof Member)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 인증 실패");
        }

        Member member = (Member) authentication.getPrincipal();

        if (member.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자만 답변을 등록할 수 있습니다.");
        }

        answerService.createAnswer(answerRequestDto, member);

        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("답변 등록 완료")
                .build());
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getAnswer(@PathVariable Long questionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 완료되지 않았습니다.");
        }

        if (!(authentication.getPrincipal() instanceof Member)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 인증 실패");
        }

        Member member = (Member) authentication.getPrincipal();
        AnswerResponseDto answerResponse = answerService.getAnswer(questionId, member);

        return ResponseEntity.ok(ApiResponse.<AnswerResponseDto>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("답변 조회 성공")
                .data(answerResponse)
                .build());
    }

    @PatchMapping("/modify/{questionId}")
    public ResponseEntity<?> updateAnswer(@PathVariable Long questionId, @RequestBody AnswerRequestDto answerRequestDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 완료되지 않았습니다.");
        }

        if (!(authentication.getPrincipal() instanceof Member)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 인증 실패");
        }

        Member member = (Member) authentication.getPrincipal();
        if (!member.getRole().equals(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        answerService.updateAnswer(questionId, answerRequestDto, member);

        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("답변 수정 완료")
                .build());
    }

    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long questionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 완료되지 않았습니다.");
        }

        if (!(authentication.getPrincipal() instanceof Member)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 인증 실패");
        }

        Member member = (Member) authentication.getPrincipal();
        answerService.deleteAnswer(questionId, member);

        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("답변 삭제 완료")
                .build());
    }
}
