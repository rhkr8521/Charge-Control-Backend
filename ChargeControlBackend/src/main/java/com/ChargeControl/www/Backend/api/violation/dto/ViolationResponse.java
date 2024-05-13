package com.ChargeControl.www.Backend.api.violation.dto;

import com.ChargeControl.www.Backend.api.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViolationResponse {

    private long id;
    private String carNumber;
    private String carImage;
    private String violationContent;
    private String violationPlace;
}
