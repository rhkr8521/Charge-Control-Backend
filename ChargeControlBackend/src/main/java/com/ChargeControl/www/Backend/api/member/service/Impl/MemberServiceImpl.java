package com.ChargeControl.www.Backend.api.member.service.Impl;

import com.ChargeControl.www.Backend.api.member.repository.EmailVerificationRepository;
import com.ChargeControl.www.Backend.api.member.domain.EmailVerification;
import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.api.member.dto.MemberSignInRequestDto;
import com.ChargeControl.www.Backend.api.member.dto.MemberSignUpRequestDto;
import com.ChargeControl.www.Backend.api.member.repository.MemberRepository;
import com.ChargeControl.www.Backend.api.member.service.MemberService;
import com.ChargeControl.www.Backend.api.member.jwt.JwtUtil;
import com.ChargeControl.www.Backend.common.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Value("${spring.mail.username")
    private String serviceEmail;
    private final Integer EXPIRATION_TIME_IN_MINUTES = 5;

    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    //private Long accessTokenValidityMs = 900_000L; // 엑세스토큰 15분 유효시간
    private Long accessTokenValidityMs = 7 * 24 * 60 * 60 * 1000L; // 엑세스토큰 일주일 유효시간
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long signUp(MemberSignUpRequestDto requestDto) throws Exception {
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new MemberException("이미 존재하는 이메일입니다.");
        }

        EmailVerification emailVerification = emailVerificationRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new VerificationException("이메일 인증을 진행해주세요."));

        if (!emailVerification.isVerified()) {
            throw new VerificationException("인증코드로 인증해주세요.");
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .carNumber(requestDto.getCarNumber())
                .phoneNumber(requestDto.getPhoneNumber())
                .build();

        // 특정 도메인일 경우 Admin 권한 부여
        if (requestDto.getEmail().endsWith("@chungbuk.ac.kr")) {
            member.addAdminAuthority();
        } else {
            member.addUserAuthority();
        }

        member = memberRepository.save(member); // 생성된 Member 객체 저장

        return member.getMemberId();
    }

    @Override
    public String generateToken(Member member) {
        return JwtUtil.createJwt(member, secretKey, accessTokenValidityMs);
    }

    public String signIn(MemberSignInRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberException("가입되어 있지 않은 이메일입니다."));
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new MemberException("잘못된 비밀번호 입니다.");
        }

        return generateToken(member);
    }

    public Member createRefreshToken(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + email));

        String newRefreshToken = UUID.randomUUID().toString();
        Instant newExpiryDate = Instant.now().plusMillis(7 * 24 * 60 * 60 * 1000); // 리프레시토큰 일주일 유효시간

        member.updateRefreshToken(newRefreshToken, newExpiryDate);

        return memberRepository.save(member);
    }

    public void verifyRefreshTokenExpiration(String token) {
        Member member = findByToken(token)
                .orElseThrow(() -> new BadRequestException("유효한 RefreshToken이 아닙니다."));

        if (member.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("인증이 만료되었습니다. 재로그인 하십시오.");
        }
    }

    public Optional<Member> findByToken(String token) {
        return memberRepository.findByRefreshToken(token);
    }

}