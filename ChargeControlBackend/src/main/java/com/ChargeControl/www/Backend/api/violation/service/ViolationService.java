package com.ChargeControl.www.Backend.api.violation.service;

import com.ChargeControl.www.Backend.api.violation.domain.Violation;
import com.ChargeControl.www.Backend.api.violation.repository.ViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViolationService {
    private final ViolationRepository violationRepository;

    String result = "true";

    public List<Violation> findViolationsByCarNumberAndResult(String carNumber, boolean isAdmin) {
        if (isAdmin) {
            return violationRepository.findByResult(result);
        } else {
            return violationRepository.findByCarNumberAndResult(carNumber, result);
        }
    }

    public Optional<Violation> findValidViolation(Long violationId, String carNumber, boolean isAdmin) {
        if (isAdmin) {
            return violationRepository.findByViolationIdAndResult(violationId, result);
        } else {
            return violationRepository.findById(violationId)
                    .filter(violation -> violation.getCarNumber().equals(carNumber) && "true".equals(violation.getResult()));
        }
    }
}
