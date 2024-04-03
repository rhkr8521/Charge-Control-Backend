package com.ChargeControl.www.Backend.api.violation.domain;

import com.ChargeControl.www.Backend.common.entity.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.Getter;
@Getter
@Entity
public class violation extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ViolationContent violationContent;

    @Column(name = "violation_place", nullable = false)
    private String violationPlace;

    @Column(name = "car_image", nullable = false)
    private String carImage;

    public enum ViolationContent {
        SINGLE, DOUBLE
    }

}
