package com.ChargeControl.www.Backend.api.chargerlocation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class ChargerLocationResponseDto {
    private Long id;
    private double latitude;
    private double longitude;
    private String name;
    private int price;
}
