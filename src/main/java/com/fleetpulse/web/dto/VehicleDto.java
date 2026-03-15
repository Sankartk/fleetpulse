package com.fleetpulse.web.dto;

import com.fleetpulse.domain.enums.FuelType;
import com.fleetpulse.domain.enums.VehicleStatus;
import com.fleetpulse.domain.enums.VehicleType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record VehicleDto(
        Long id,
        String registrationNumber,
        String make,
        String model,
        Integer year,
        VehicleType vehicleType,
        VehicleStatus status,
        FuelType fuelType,
        LocalDate purchaseDate,
        Double currentMileage,
        Double nextServiceMileage,
        Long assignedDriverId,
        String assignedDriverName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
