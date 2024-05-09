package com.ChargeControl.www.Backend.api.member.repository;

import com.ChargeControl.www.Backend.api.member.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByEmail(String email);
    Optional<EmailVerification> findByCode(String code);
}
