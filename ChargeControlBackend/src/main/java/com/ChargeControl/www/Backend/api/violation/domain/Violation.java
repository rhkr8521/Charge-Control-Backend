package com.ChargeControl.www.Backend.api.violation.domain;

import com.ChargeControl.www.Backend.api.question.domain.Question;
import com.ChargeControl.www.Backend.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Violation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long violationId;

    private String carNumber;
    private String violationPlace;
    private String carImage;
    private String violationContent;
    private String result;

    @OneToMany(mappedBy = "violation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Question> questions;
}
