package com.fleetpulse.web.dto;

import com.fleetpulse.domain.enums.DriverStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DriverDto(
        Long id,
        String employeeId,
        String firstName,
        String lastName,
        String fullName,
        String licenseNumber,
        LocalDate licenseExpiryDate,
        String contactPhone,
        DriverStatus status,
        LocalDateTime createdAt
) {}
