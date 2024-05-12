package com.ChargeControl.www.Backend.api.member.service.Impl;

import com.ChargeControl.www.Backend.api.member.domain.EmailVerification;
import com.ChargeControl.www.Backend.api.member.repository.EmailVerificationRepository;
import com.ChargeControl.www.Backend.common.exception.VerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String serviceEmail;

    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    public void sendVerificationEmail(String email, LocalDateTime requestedAt) {

        //기존에 있는 Email 삭제
        emailVerificationRepository.findByEmail(email)
                .ifPresent(emailVerification -> emailVerificationRepository.delete(emailVerification));

        String code = UUID.randomUUID().toString();
        EmailVerification verification = EmailVerification.builder()
                .email(email)
                .code(code)
                .expirationTimeInMinutes(5)
                .isVerified(false)
                .build();
        emailVerificationRepository.save(verification);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(serviceEmail);
        mailMessage.setTo(email);
        mailMessage.setSubject("ChargeControl 회원가입 인증코드 입니다.");
        mailMessage.setText(verification.generateCodeMessage());
        mailSender.send(mailMessage);
    }

    public void verifyEmail(String code, LocalDateTime requestedAt) {
        EmailVerification verification = emailVerificationRepository.findByCode(code)
                .orElseThrow(() -> new VerificationException("올바른 인증코드를 입력하세요."));

        if (verification.isExpired(requestedAt)) {
            throw new VerificationException("인증코드가 만료되었습니다, 재인증 해주세요.");
        }

        verification.setIsVerified(true);
        emailVerificationRepository.save(verification);
    }
}
