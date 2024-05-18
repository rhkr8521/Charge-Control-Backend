package com.ChargeControl.www.Backend.api.chargerlocation.repository;

import com.ChargeControl.www.Backend.api.chargerlocation.domain.ChargerLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargerLocationRepository extends JpaRepository<ChargerLocation, Long> {
}
