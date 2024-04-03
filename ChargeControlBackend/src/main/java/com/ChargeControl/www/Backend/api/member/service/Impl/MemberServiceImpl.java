package com.ChargeControl.www.Backend.api.member.service.Impl;

import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.api.member.dto.MemberSignInRequestDto;
import com.ChargeControl.www.Backend.api.member.dto.MemberSignUpRequestDto;
import com.ChargeControl.www.Backend.api.member.repository.MemberRepository;
import com.ChargeControl.www.Backend.api.member.service.MemberService;
import com.ChargeControl.www.Backend.api.member.jwt.JwtUtil;
import com.ChargeControl.www.Backend.common.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    private Long accessTokenValidityMs = 3600_000L; // 1 hour for accessToken

    @Override
    @Transactional
    public Long signUp(MemberSignUpRequestDto requestDto) throws Exception {
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .carNumber(requestDto.getCarNumber())
                // 여기에 추가 필드를 설정할 수 있습니다.
                .build();
        member.addUserAuthority(); // 필요한 권한 설정

        member = memberRepository.save(member); // 생성된 Member 객체 저장

        return member.getMemberId();
    }


    @Override
    public String generateToken(String username) {
        return JwtUtil.createJwt(username, secretKey, accessTokenValidityMs);
    }
    public String signIn(MemberSignInRequestDto requestDto){

        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(EmailNotFoundException::new);
        if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
            throw new InvalidPasswordException();
        }

        return JwtUtil.createJwt(member.getName(), secretKey, accessTokenValidityMs);
    }

    public Member createRefreshToken(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + email));

        String newRefreshToken = UUID.randomUUID().toString();
        Instant newExpiryDate = Instant.now().plusMillis(600000);

        member.setToken(newRefreshToken);
        member.setExpiryDate(newExpiryDate);

        return memberRepository.save(member);
    }

    public Optional<Member> findByToken(String token){
        return memberRepository.findByToken(token);
    }

    @Override
    public void verifyRefreshTokenExpiration(String token) {
        Member member = findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token."));

        if (member.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired. Please log in again.");
        }
    }

}