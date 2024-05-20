package com.ChargeControl.www.Backend.api.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ChargeControl.www.Backend.api.member.domain.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByRefreshToken(String token);
    Member findByCarNumber(String carNumber);
}