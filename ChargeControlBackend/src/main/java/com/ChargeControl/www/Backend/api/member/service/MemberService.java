package com.ChargeControl.www.Backend.api.member.service;

import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.api.member.dto.MemberSignInRequestDto;
import com.ChargeControl.www.Backend.api.member.dto.MemberSignUpRequestDto;

import java.util.Optional;

public interface MemberService {

    String signIn(MemberSignInRequestDto requestDto) throws Exception;

    Long signUp(MemberSignUpRequestDto requestDto) throws Exception;

    String generateToken(String username);

    Optional<Member> findByToken(String token);

    Member createRefreshToken(String email);

    void verifyRefreshTokenExpiration(String token);

}
