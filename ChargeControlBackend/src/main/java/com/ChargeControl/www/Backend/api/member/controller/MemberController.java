package com.ChargeControl.www.Backend.api.member.controller;

import com.ChargeControl.www.Backend.api.member.dto.JwtResponse;
import com.ChargeControl.www.Backend.api.member.dto.MemberSignInRequestDto;
import com.ChargeControl.www.Backend.api.member.dto.MemberSignUpRequestDto;
import com.ChargeControl.www.Backend.api.member.dto.RefreshTokenRequest;
import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.api.member.service.MemberService;
import com.ChargeControl.www.Backend.common.exception.*;
import com.ChargeControl.www.Backend.common.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody MemberSignInRequestDto request) throws Exception {
        String accessToken = memberService.signIn(request);
        String refreshToken = memberService.createRefreshToken(request.getEmail()).getToken();
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
        memberService.verifyRefreshTokenExpiration(refreshTokenRequest.getToken());

        Member member = memberService.findByToken(refreshTokenRequest.getToken())
                .orElseThrow(TokenNotFoundException::new);

        String accessToken = memberService.generateToken(member.getEmail());
        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshTokenRequest.getToken());

        ApiResponse<JwtResponse> response = ApiResponse.<JwtResponse>builder()
                .status(SuccessStatus.GET_NEW_TOKEN_SUCCESS.getStatusCode())
                .success(true)
                .message(SuccessStatus.GET_NEW_TOKEN_SUCCESS.getMessage())
                .data(jwtResponse)
                .build();

        return ResponseEntity.ok(response);
    }


}
