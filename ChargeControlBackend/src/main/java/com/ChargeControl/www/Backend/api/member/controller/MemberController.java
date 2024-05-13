package com.ChargeControl.www.Backend.api.member.controller;

import com.ChargeControl.www.Backend.api.member.dto.*;
import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.api.member.service.Impl.EmailService;
import com.ChargeControl.www.Backend.api.member.service.MemberService;
import com.ChargeControl.www.Backend.common.exception.*;
import com.ChargeControl.www.Backend.common.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody MemberSignInRequestDto request) throws Exception {
        String accessToken = memberService.signIn(request);
        String refreshToken = memberService.createRefreshToken(request.getEmail()).getRefreshToken();
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        ApiResponse<JwtResponse> response = ApiResponse.<JwtResponse>builder()
                .status(SuccessStatus.SIGNIN_SUCCESS.getStatusCode())
                .success(true)
                .message(SuccessStatus.SIGNIN_SUCCESS.getMessage())
                .data(jwtResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Long>> join(@Valid @RequestBody MemberSignUpRequestDto request) throws Exception {
        Long memberId = memberService.signUp(request);
        ApiResponse<Long> response = ApiResponse.success(SuccessStatus.SIGNUP_SUCCESS.getStatusCode(), SuccessStatus.SIGNUP_SUCCESS.getMessage());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        Optional<Member> memberOptional = memberService.findByToken(refreshTokenRequest.getToken());
        if (!memberOptional.isPresent()) {
            throw new BadRequestException("유효한 RefreshToken이 아닙니다.");
        }

        memberService.verifyRefreshTokenExpiration(refreshTokenRequest.getToken());
        Member member = memberOptional.get();

        String accessToken = memberService.generateToken(member);
        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshTokenRequest.getToken());

        ApiResponse<JwtResponse> response = ApiResponse.<JwtResponse>builder()
                .status(SuccessStatus.GET_NEW_TOKEN_SUCCESS.getStatusCode())
                .success(true)
                .message(SuccessStatus.GET_NEW_TOKEN_SUCCESS.getMessage())
                .data(jwtResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> getEmailForVerification(@RequestBody EmailRequest.EmailForVerificationRequest request) {
        LocalDateTime requestedAt = LocalDateTime.now();
        emailService.sendVerificationEmail(request.getEmail(), requestedAt);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.ACCEPTED.value())
                .success(true)
                .message("정상 발송")
                .build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PostMapping("/verification-code")
    public ResponseEntity<ApiResponse<String>> verificationByCode(@RequestBody EmailRequest.VerificationCodeRequest request) {
        LocalDateTime requestedAt = LocalDateTime.now();
        emailService.verifyEmail(request.getCode(), requestedAt);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("인증완료")
                .build();
        return ResponseEntity.ok(response);
    }

    // 현재 인증된 사용자의 상세 정보를 반환
    @GetMapping("/currentMember")
    public ResponseEntity<?> getCurrentMemberDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Member) {
            Member member = (Member) principal;

            MemberResponse memberResponse = MemberResponse.builder()
                    .email(member.getEmail())
                    .name(member.getName())
                    .carNumber(member.getCarNumber())
                    .role(member.getRole())
                    .build();

            return ResponseEntity.ok(memberResponse);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No valid member found in the security context");
    }


}
