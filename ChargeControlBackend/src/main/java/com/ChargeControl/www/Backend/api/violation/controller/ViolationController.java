package com.ChargeControl.www.Backend.api.violation.controller;

import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.api.member.domain.Role;
import com.ChargeControl.www.Backend.api.violation.domain.Violation;
import com.ChargeControl.www.Backend.api.violation.dto.ViolationResponseDto;
import com.ChargeControl.www.Backend.api.violation.service.ViolationService;
import com.ChargeControl.www.Backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/violation")
@RequiredArgsConstructor
public class ViolationController {
    private final ViolationService violationService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ViolationResponseDto>>> getAllViolations(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Member member = (Member) authentication.getPrincipal();
        boolean isAdmin = member.getRole().equals(Role.ADMIN);
        List<Violation> violations = violationService.findViolationsByCarNumberAndResult(member.getCarNumber(), isAdmin);
        List<ViolationResponseDto> violationResponses = mapViolationsToResponses(violations);

        return ResponseEntity.ok(
                ApiResponse.<List<ViolationResponseDto>>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("조회 성공")
                        .data(violationResponses)
                        .build()
        );
    }

    @GetMapping("/{violationId}")
    public ResponseEntity<ApiResponse<ViolationResponseDto>> getViolationDetail(@PathVariable Long violationId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Member member = (Member) authentication.getPrincipal();
        boolean isAdmin = member.getRole().equals(Role.ADMIN);
        return violationService.findValidViolation(violationId, member.getCarNumber(), isAdmin)
                .map(violation -> ResponseEntity.ok(
                        ApiResponse.<ViolationResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("조회 성공")
                                .data(mapViolationToResponse(violation))
                                .build()
                ))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiResponse.<ViolationResponseDto>builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .success(false)
                                .message("해당 조건에 맞는 위반 정보가 없습니다.")
                                .build()
                ));
    }

    private List<ViolationResponseDto> mapViolationsToResponses(List<Violation> violations) {
        return violations.stream().map(this::mapViolationToResponse).collect(Collectors.toList());
    }

    private ViolationResponseDto mapViolationToResponse(Violation violation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
        String formattedDate = violation.getCreatedAt().format(formatter);

        return ViolationResponseDto.builder()
                .id(violation.getViolationId())
                .carNumber(violation.getCarNumber())
                .carImage(violation.getCarImage())
                .violationContent(violation.getViolationContent())
                .violationPlace(violation.getViolationPlace())
                .violationDate(formattedDate)
                .build();
    }
}
