package com.fleetpulse.web.dto;

import com.fleetpulse.domain.enums.AlertSeverity;
import com.fleetpulse.domain.enums.AlertType;

import java.time.LocalDateTime;

public record AlertDto(
        Long id,
        Long vehicleId,
        String vehicleRegistration,
        AlertType alertType,
        AlertSeverity severity,
        String message,
        boolean resolved,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt
) {}
