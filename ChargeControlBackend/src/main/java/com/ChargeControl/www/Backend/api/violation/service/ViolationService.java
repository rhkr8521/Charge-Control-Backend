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

    public List<Violation> findViolationsByCarNumber(String carNumber) {
        return violationRepository.findByCarNumberAndResult(carNumber, "true");
    }

    public Optional<Violation> findValidViolationByCarNumberAndResult(Long violationId, String carNumber) {
        return violationRepository.findById(violationId)
                .filter(violation -> violation.getCarNumber().equals(carNumber) && violation.getResult().equals("true"));
    }

}
