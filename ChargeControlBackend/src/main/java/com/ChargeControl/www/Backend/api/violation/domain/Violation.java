package com.ChargeControl.www.Backend.api.violation.domain;

import com.ChargeControl.www.Backend.common.entity.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Violation extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long violationId;

    private String carNumber;
    private String violationPlace;
    private String carImage;
    private String violationContent;
    private String result;

}
