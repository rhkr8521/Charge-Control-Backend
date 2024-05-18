package com.ChargeControl.www.Backend.api.chargerlocation.controller;

import com.ChargeControl.www.Backend.api.chargerlocation.dto.ChargerLocationResponseDto;
import com.ChargeControl.www.Backend.api.chargerlocation.service.ChargerLocationService;
import com.ChargeControl.www.Backend.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chargerlocation")
public class ChargerLocationController {

    private final ChargerLocationService chargerLocationService;

    public ChargerLocationController(ChargerLocationService chargerLocationService) {
        this.chargerLocationService = chargerLocationService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ChargerLocationResponseDto>>> getAllChargerLocations() {
        List<ChargerLocationResponseDto> locations = chargerLocationService.getAllChargerLocations();
        return ResponseEntity.ok(ApiResponse.<List<ChargerLocationResponseDto>>builder()
                .status(200) // HTTP 200 OK
                .success(true)
                .message("충전소 정보 조회 성공")
                .data(locations)
                .build());
    }
}
