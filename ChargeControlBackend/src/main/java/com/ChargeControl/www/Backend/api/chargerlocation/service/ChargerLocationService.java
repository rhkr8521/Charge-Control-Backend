package com.ChargeControl.www.Backend.api.chargerlocation.service;

import com.ChargeControl.www.Backend.api.chargerlocation.dto.ChargerLocationResponseDto;
import com.ChargeControl.www.Backend.api.chargerlocation.repository.ChargerLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChargerLocationService {
    private final ChargerLocationRepository chargerLocationRepository;

    public ChargerLocationService(ChargerLocationRepository chargerLocationRepository) {
        this.chargerLocationRepository = chargerLocationRepository;
    }

    public List<ChargerLocationResponseDto> getAllChargerLocations() {
        return chargerLocationRepository.findAll().stream()
                .map(location -> new ChargerLocationResponseDto(
                        location.getId(),
                        location.getLatitude(),
                        location.getLongitude(),
                        location.getName(),
                        location.getPrice()
                )).collect(Collectors.toList());
    }
}
