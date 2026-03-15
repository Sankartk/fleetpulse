package com.fleetpulse.web.dto;

import com.fleetpulse.domain.enums.MaintenanceStatus;
import com.fleetpulse.domain.enums.MaintenanceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MaintenanceRecordDto(
        Long id,
        Long vehicleId,
        String vehicleRegistration,
        String vehicleMakeModel,
        MaintenanceType maintenanceType,
        MaintenanceStatus status,
        LocalDate scheduledDate,
        LocalDate completedDate,
        Double mileageAtService,
        BigDecimal costAmount,
        String technicianName,
        String notes,
        LocalDateTime createdAt
) {}
